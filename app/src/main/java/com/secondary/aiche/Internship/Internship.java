package com.secondary.aiche.Internship;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.secondary.aiche.Events.Events;
import com.secondary.aiche.Module;
import com.secondary.aiche.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class Internship extends Activity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    String url;
    ImageView imageView;
    CardView cardView;
    Module model;
    DatabaseReference databaseReference;
    SharedPreferences whichTraining;
    View header;
    Events.colorEventThread _colorEventThread;
    public static WeakReference<Internship> internshipWeakReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        internshipWeakReference = new WeakReference<Internship>(this);

        setContentView(R.layout.list_view);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                pullToRefresh.setRefreshing(false);
            }
        });

        mListView = (ListView) findViewById(R.id.list_view);


        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-app-6fac7.firebaseio.com/").child("Training");
        header = getLayoutInflater().inflate(R.layout.training_header, mListView, false);
        mListView.addHeaderView(header, null, false);

            FirebaseListAdapter<Module> firebaseListAdapter = new FirebaseListAdapter<Module>(
                    internshipWeakReference.get(),
                    Module.class,
                    R.layout.list_item,
                    databaseReference
            ) {
                @Override
                protected void populateView(View v, Module model, int position) {
                    imageView = (ImageView) v.findViewById(R.id.imageView1);
                    cardView = (CardView) v.findViewById(R.id.card_view);
                    loadImagefromUrl(model.getImage());

                    String str_date = model.getTimeStamp();
                    DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = null;
                    try {
                        date = (Date) formatter.parse(str_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    _colorEventThread = new Events.colorEventThread(imageView,date);
                    _colorEventThread.start();

                }
            };

//mark: waiting for header image

            mListView.setAdapter(firebaseListAdapter);
            mListView.setOnItemClickListener(internshipWeakReference.get());

    }


    private void loadImagefromUrl(String url) {
        Picasso.with(getApplicationContext()).load(url).error(R.drawable.exclamation_mark)
                .into(imageView, new com.squareup.picasso.Callback() {

                    @Override
                    public void onSuccess() {
                        cardView.setPreventCornerOverlap(false);
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {


        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {


            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                whichTraining = getSharedPreferences("TrainingChosen", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = whichTraining.edit();
                long i_express = i - dataSnapshot.getChildrenCount();
                editor.putLong("TrainingToView", i_express);
                editor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

            Intent intent = new Intent(getApplicationContext(), ViewInternship.class);
            startActivity(intent);

        }else{Toast.makeText(getApplicationContext(),"Connection Error",Toast.LENGTH_SHORT).show();}

    }

    public void refresh() {

        recreate();

        // الكام سطر الملغين دول ليهم نفس دور الrecreate اللي فوق

        /*
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-app-6fac7.firebaseio.com/").child("Training");
        header = getLayoutInflater().inflate(R.layout.internship_header, mListView, false);

        FirebaseListAdapter<Module> firebaseListAdapter = new FirebaseListAdapter<Module>(
                this,
                Module.class,
                R.layout.list_item,
                databaseReference
        ) {
            @Override
            protected void populateView(View v, Module model, int position) {
                imageView = (ImageView) v.findViewById(R.id.imageView1);
                cardView = (CardView) v.findViewById(R.id.card_view);
                loadImagefromUrl(model.getImage());

                String str_date=model.getTimeStamp();
                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                Date date = null;
                try {
                    date = (Date)formatter.parse(str_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(System.currentTimeMillis() > date.getTime() + 86400) {//بازود عليها يوم
                    ColorMatrix matrix = new ColorMatrix();
                    matrix.setSaturation(0);

                    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                    imageView.setColorFilter(filter);
                }
            }
        };*/

    }
}