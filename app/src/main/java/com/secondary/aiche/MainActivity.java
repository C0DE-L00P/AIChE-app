package com.secondary.aiche;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.secondary.aiche.Chat.Chat;
import com.secondary.aiche.Chat.MessageManager;
import com.secondary.aiche.Events.Events;
import com.secondary.aiche.FirstRun.Slider;
import com.secondary.aiche.Internship.Internship;
import com.secondary.aiche.Knowledge.CoursesType;
import com.secondary.aiche.Study.Study;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import io.fabric.sdk.android.Fabric;

import static com.secondary.aiche.Chat.Chat.RC_SIGN_IN;


public class MainActivity extends Activity {

CardView eventsCard,trainingCard,studyCard,knowledgeCard;
ImageView aichelogo,supportBtm,notificationBtm,aichetext;


    FirebaseDatabase database;
    DatabaseReference mMessageDatabaseReference;
    public static DatabaseReference mMessageDatabaseReference4;
    public static int expertNum;
    public FirebaseAuth mFirebaseAuth;
    public FirebaseAuth.AuthStateListener mAuthStateListener;
    public static FirebaseUser user;
    public static String userID;
    public static boolean isExpert;
    public static MediaPlayer popSound,messageManagerSound,receivingLatestSound;
    public static GradientDrawable gradiantBlue,gradiantOrange;
    boolean authIsOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        if(!previouslyStarted){ //عشان تعرف لو اول مره يفتح البرنامج ولا لأ
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
            edit.apply();
            Log.i("first run","true");
            startActivity(new Intent(MainActivity.this, Slider.class));  //to Slider
        }

        authIsOn = true;

        //String token = FirebaseInstanceId.getInstance().getToken();

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        setContentView(R.layout.main_grid_activity);
        //Log.d("Firebase", "token "+ FirebaseInstanceId.getInstance().getToken());

        isExpert = false;
        database = FirebaseDatabase.getInstance();
        mMessageDatabaseReference = database.getReference("Experts");
        mMessageDatabaseReference4 = database.getReference("Blacklist");

        mFirebaseAuth = FirebaseAuth.getInstance();

        popSound = MediaPlayer.create(this, R.raw.blop);
        messageManagerSound = MediaPlayer.create(this, R.raw.soft_bells);
        receivingLatestSound = MediaPlayer.create(this, R.raw.get_message);

        //receivingLatestSound.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);

        // To Play it as notification tone
        //popSound.setAudioStreamType(AudioManager.STREAM_ALARM);
        //messageManagerSound .setAudioStreamType(AudioManager.STREAM_RING);
        //receivingLatestSound.setAudioStreamType(AudioManager.STREAM_SYSTEM);

        aichelogo = findViewById(R.id.aiche_logo);
        aichetext = findViewById(R.id.aiche_text);
        aichelogo.animate().alpha(1f).setDuration(2000);
        aichetext.animate().alpha(1f).setDuration(2500);

        eventsCard = findViewById(R.id.eventsCard);
        trainingCard = findViewById(R.id.trainingCard);
        studyCard = findViewById(R.id.studyCard);
        knowledgeCard = findViewById(R.id.knowledgeCard);
        notificationBtm = findViewById(R.id.notificationBtm);

        eventsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Events.class);
                startActivity(intent);
            }
        });

        trainingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Internship.class);
                startActivity(intent);
            }
        });

        studyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Study.class);
                startActivity(intent);
            }
        });

        knowledgeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CoursesType.class);
                startActivity(intent);
            }
        });


        @NonNull final SharedPreferences stateSaved = getSharedPreferences("NotificationState", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = stateSaved.edit();

        //state = 1 means notification is on
        //state = 0 means notification is off

        if(stateSaved.getInt("reqActivity", 1) == 1){
            notificationBtm.setImageResource(R.drawable.sound_on);
            FirebaseMessaging.getInstance().subscribeToTopic("all");
        }else{
            notificationBtm.setImageResource(R.drawable.sound_off);
            FirebaseMessaging.getInstance().unsubscribeFromTopic("all");
        }

        notificationBtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stateSaved.getInt("reqActivity", 1) == 0){
                    notificationBtm.setImageResource(R.drawable.sound_on);
                    FirebaseMessaging.getInstance().subscribeToTopic("all");
                    editor.putInt("reqActivity", 1);
                    editor.apply();

                }else{
                    notificationBtm.setImageResource(R.drawable.sound_off);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("all");
                    editor.putInt("reqActivity", 0);
                    editor.apply();
                }
            }
        });


        gradiantBlue = new GradientDrawable();
        gradiantBlue.setColors(new int[] {
                Color.parseColor("#278dde"),
                Color.parseColor("#024ccb")
        });
        gradiantBlue.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradiantBlue.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);

        gradiantOrange = new GradientDrawable();
        gradiantOrange.setColors(new int[] {
                Color.parseColor("#ffd900"),
                Color.parseColor("#ff9b00")
        });
        gradiantOrange.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradiantOrange.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);

    }

    //دول كانوا صورتين لجوجل وصفحة الفيس عشان الدعم وحاليا غير مستخدمين بس سايبهم للمستقبل لو احتاجت اضيفهم

    /*
    public void gmail_contact (View view){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("https://mail.google.com/mail/u/0/#inbox"));
        startActivity(intent);
    }

    public void facebook_contact(View view){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("https://www.facebook.com/AicheSuez/?fb_dtsg_ag=AdzAwP_FPwVjU03kOBdbCP8KJ-atgqd5SmYZukPeaN0SMg%3AAdxZLsr_oXXi95MsR1Wk9Z586kwsRFR-1s1OBW9C0nOJgg"));
        startActivity(intent);
    }
*/


    public void Submit(View view){

        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            mAuthStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    user = mFirebaseAuth.getCurrentUser();
                    if (user != null) {
                        userID = user.getUid();
                        expert_method();
                        messageManagerSound.start();
                        authIsOn = true; //To show only one auth screen not to get a crash from multiple logins
                    } else {
                        if (authIsOn){
                            authIsOn = false;
                        startActivityForResult(
                                AuthUI.getInstance()
                                        .createSignInIntentBuilder()
                                        .setIsSmartLockEnabled(false)
                                        .setAvailableProviders(Collections.singletonList(
                                                new AuthUI.IdpConfig.GoogleBuilder().build()))
                                        .build(),
                                RC_SIGN_IN);
                        }
                    }
                }
            };
            mFirebaseAuth.addAuthStateListener(mAuthStateListener);

        } else {
            Toast.makeText(MainActivity.this, "Connection Error or TEData Subscriber", Toast.LENGTH_SHORT).show();
        }
    }

    public void expert_method() {

        Query queryRef = mMessageDatabaseReference.orderByChild("UiD").equalTo(userID);

        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    //Expert login
                    Intent intent = new Intent(MainActivity.this, MessageManager.class);
                    startActivity(intent);
                } else {
                    Query _blacklistCheck = mMessageDatabaseReference4.orderByChild("UiD").equalTo(userID);
                    _blacklistCheck.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                Toast.makeText(MainActivity.this,"This Account is Blocked",Toast.LENGTH_SHORT).show();
                                AuthUI.getInstance().signOut(MainActivity.this);
                            } else {
                                //Asker login

                                Intent intent = new Intent(MainActivity.this, Chat.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void WhoAreWe(View view){

        AlertDialog.Builder alertadd = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater factory = LayoutInflater.from(MainActivity.this);
        final View v = factory.inflate(R.layout.who_are_we, null);
        alertadd.setView(v);
        alertadd.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        authIsOn = true;
    }
}