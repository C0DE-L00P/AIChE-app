package com.secondary.aiche.Chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.secondary.aiche.MainActivity;
import com.secondary.aiche.R;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static com.secondary.aiche.Chat.Chat.ANONYMOUS;
import static com.secondary.aiche.MainActivity.gradiantBlue;
import static com.secondary.aiche.MainActivity.popSound;
import static com.secondary.aiche.MainActivity.user;
import static com.secondary.aiche.MainActivity.userID;

public class MessageManager extends AppCompatActivity {


    private ListView mMessageListView;
    private MessageManagerAdapter mMessageAdapter;
    FirebaseDatabase database;
    DatabaseReference mMessageDatabaseReference;
    FirebaseStorage mFirebaseStorage;
    StorageReference mChatPhotoStorageReference;
    ChildEventListener mChildEventListener;
    public String mUsername;
    public static List<String> latestMessagesNames;
    public static int _position;
    public static boolean updateNeeded;
    private Toolbar mTopToolbar;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_manager);

        editor = getSharedPreferences("chatNotificationState", MODE_PRIVATE).edit();
        prefs = getSharedPreferences("chatNotificationState", MODE_PRIVATE);


        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        mTopToolbar.setBackground(gradiantBlue);
        setSupportActionBar(mTopToolbar);

        MainActivity.isExpert = true;
        updateNeeded = false;
        mUsername = user.getDisplayName();
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recreate();
                pullToRefresh.setRefreshing(false);
            }
        });


        // Initialize references to views
        mMessageListView = (ListView) findViewById(R.id.recycler_view_manager);
        database = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        mMessageDatabaseReference = database.getReference("latest_messages");
        mChatPhotoStorageReference = mFirebaseStorage.getReference().child("latest_chat_photos");

        latestMessagesNames = new ArrayList<>();

        onSignInitialize();

        final List<FriendlyMessage> latestMessages = new ArrayList<>();
        mMessageAdapter = new MessageManagerAdapter(MessageManager.this,R.layout.message_manager_item, latestMessages);
        mMessageListView.setAdapter(mMessageAdapter);
        mMessageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                _position = i;
                Intent intent = new Intent(MessageManager.this, Chat.class);
                startActivity(intent);
            }
        });
    }


    private void onSignInitialize() {


        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    final FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                    Query query = MainActivity.mMessageDatabaseReference4.orderByChild("UiD").equalTo(friendlyMessage.getUID());
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()){
                                mMessageAdapter.add(friendlyMessage);
                                latestMessagesNames.add(friendlyMessage.getUID());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    recreate();
                    if (prefs.getBoolean("notificationIsOn", true))
                        if (!userID.equals( MessageManager.latestMessagesNames.get(MessageManager._position))){
                            MainActivity.receivingLatestSound.start();
                        }else {
                    }
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            mMessageDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void onSignOut() {
        mUsername = ANONYMOUS;
        mMessageAdapter.clear();
        if (mChildEventListener != null) {
            mMessageDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.message_manager_menu, menu);
        if (prefs.getBoolean("notificationIsOn", true)){menu.getItem(0).setIcon(R.drawable.bell_pressed);}
        else{menu.getItem(0).setIcon(R.drawable.bell);}
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.logout) {
            //SignOut
            MainActivity.isExpert = false;
            AuthUI.getInstance().signOut(this);
            onSignOut();
        }else if (item.getItemId() == R.id.notification){
            if (prefs.getBoolean("notificationIsOn", true)){//True Condition to False
                item.setIcon(R.drawable.bell);
                Toast.makeText(this,"notify off",Toast.LENGTH_SHORT).show();
                editor.putBoolean("notificationIsOn", false);
                editor.apply();
            }else{//False Condition to True

                item.setIcon(R.drawable.bell_pressed);
                popSound.start();
                Toast.makeText(this,"notify on",Toast.LENGTH_SHORT).show();
                editor.putBoolean("notificationIsOn", true);
                editor.apply();
                }
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (updateNeeded){
            recreate();
        }
    }

    public static class thread extends Thread {
        TextView name;
        LinearLayout _LinearLayout;


        thread(TextView name, LinearLayout _LinearLayout) {
            this.name = name;
            this._LinearLayout = _LinearLayout;
        }


        @Override
        public void run() {
            super.run();

            try {
                if (!name.getText().toString().equals("")) {
                    if (user.getDisplayName().equals(name.getText().toString())) {
                        _LinearLayout.setBackground(MainActivity.gradiantBlue);
                        //_LinearLayout.setBackgroundColor(Color.parseColor("#FF0079E0"));


                    } else {
                       // _LinearLayout.setBackgroundColor(Color.parseColor("#fa8612"));
                        _LinearLayout.setBackground(MainActivity.gradiantOrange);
                    }

                }
            } catch (Exception e) {
            }

        }
    }
}
//old