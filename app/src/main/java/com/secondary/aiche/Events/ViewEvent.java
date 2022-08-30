package com.secondary.aiche.Events;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.secondary.aiche.Module;
import com.secondary.aiche.ObservableScrollView;
import com.secondary.aiche.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * Created by Mohamed Gamal on 11/08/2018.
 */

public class ViewEvent extends Activity {

    ImageView eventImage;
    TextView title;
    TextView date;
    TextView details;
    ScrollView scrollView;
    DatabaseReference databaseReference;
    Module model;
    String imageUrl;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //To Manage form of the activity

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        setContentView(R.layout.view_event_activity);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                pullToRefresh.setRefreshing(false);
            }
        });

        eventImage = findViewById(R.id.eventImage);
        title = findViewById(R.id.title);
        date = findViewById(R.id.date);
        details = findViewById(R.id.details);
        scrollView = findViewById(R.id.scroll_view);

        SharedPreferences EventPrefs = getApplicationContext().getSharedPreferences("EventChosen", Context.MODE_PRIVATE);
        String EventToGet = String.valueOf(EventPrefs.getLong("EventToView", 0));

            databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-app-6fac7.firebaseio.com/").child("Events").child(EventToGet);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    model = dataSnapshot.getValue(Module.class);

                    title.setText(model.getTitle());
                    date.setText(model.getTime());
                    details.setText(model.getDetail());
                    imageUrl = model.getImage();
                    loadImagefromUrl(imageUrl);
                    url = model.getLink();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            //To put the required text & Images for the views
            //انا مستعمل EventId-1 عشان الهيدر (الصوره اللي في العنوان) بيتحسب علي انه اول عنصر في الليست


            //To make the button Clickable to link

            final ImageView going_button = (ImageView) findViewById(R.id.going_button);
            going_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
            });


            //To make the animation work on any screen size

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            final int width = size.x;


            //Detect scrolling and give an animation for it

            ObservableScrollView observableScrollView = (ObservableScrollView) findViewById(R.id.scroll_view);
            observableScrollView.setOnScrollViewListener(new ObservableScrollView.OnScrollViewListener() {
                public void onScrollChanged(ObservableScrollView v, int l, int t, int oldl, int oldt) {
                    if (t > 70) {
                        going_button.animate().translationX(190f * width / 720).setDuration(1000);
                    } else {
                        going_button.animate().translationX(0f).setDuration(1000);
                    }
                }
            });
    }

    public void Back2Events(View view) {
        super.onBackPressed();
    }


    private void loadImagefromUrl(String url) {
        Picasso.with(this).load(url).error(R.drawable.exclamation_mark)
                .into(eventImage, new com.squareup.picasso.Callback() {

                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    public void refresh() {
        SharedPreferences EventPrefs = getApplicationContext().getSharedPreferences("EventChosen", Context.MODE_PRIVATE);
        String EventToGet = String.valueOf(EventPrefs.getLong("EventToView", 0));

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-app-6fac7.firebaseio.com/").child("Events").child(EventToGet);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                model = dataSnapshot.getValue(Module.class);

                title.setText(model.getTitle());
                date.setText(model.getTime());
                details.setText(model.getDetail());
                imageUrl = model.getImage();
                loadImagefromUrl(imageUrl);
                url = model.getLink();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
