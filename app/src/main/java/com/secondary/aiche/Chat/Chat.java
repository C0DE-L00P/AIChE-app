/**
 * Copyright Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.secondary.aiche.Chat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.secondary.aiche.MainActivity;
import com.secondary.aiche.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.secondary.aiche.MainActivity.gradiantOrange;
import static com.secondary.aiche.MainActivity.isExpert;
import static com.secondary.aiche.MainActivity.popSound;
import static com.secondary.aiche.MainActivity.user;
import static com.secondary.aiche.MainActivity.userID;

public class Chat extends AppCompatActivity {

    private static final String TAG = "Chat";

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;

    private Toolbar mTopToolbar;
    private ListView mMessageListView;
    private MessageAdapter mMessageAdapter;
    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private EditText mMessageEditText;
    private Button mSendButton;
    FirebaseDatabase database;
    DatabaseReference mMessageDatabaseReference, mMessageDatabaseReference2, mMessageDatabaseReference3;
    FirebaseStorage mFirebaseStorage;
    public FirebaseAuth mFirebaseAuth;
    public FirebaseAuth.AuthStateListener mAuthStateListener;
    StorageReference mChatPhotoStorageReference, mChatPhotoStorageReference2;
    ChildEventListener mChildEventListener;
    public static final int RC_SIGN_IN = 1;
    private static final int RC_PHOTO_PICKER = 2;
    public String mUsername;
    public static long counter;
    boolean isAnswered;
    DateFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        MessageManager.updateNeeded = true;

        mTopToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.my_toolbar);
        mTopToolbar.setBackground(gradiantOrange);
        setSupportActionBar(mTopToolbar);


        mUsername = ANONYMOUS;

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = mFirebaseAuth.getCurrentUser();
                if (user != null) {
                } else {
                    finish();
                }
            }
        };

        df = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.ENGLISH);


        // Initialize references to views
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageListView = (ListView) findViewById(R.id.messageListView);
        mPhotoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton);
        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);
        database = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mMessageDatabaseReference = database.getReference();


        if (isExpert) {
            userID = MessageManager.latestMessagesNames.get(MessageManager._position);
            mMessageDatabaseReference = database.getReference("messages").child(MessageManager.latestMessagesNames.get(MessageManager._position));
            mChatPhotoStorageReference = mFirebaseStorage.getReference().child("chat_photos").child(MessageManager.latestMessagesNames.get(MessageManager._position));

            mMessageDatabaseReference2 = database.getReference("latest_messages").child(MessageManager.latestMessagesNames.get(MessageManager._position));
            mChatPhotoStorageReference2 = mFirebaseStorage.getReference().child("latest_chat_photos").child(MessageManager.latestMessagesNames.get(MessageManager._position));
        } else {
            mMessageDatabaseReference = database.getReference("messages").child(userID);
            mChatPhotoStorageReference = mFirebaseStorage.getReference().child("chat_photos").child(userID);
        }

        onSignInitialize(user.getDisplayName());

        // Initialize message ListView and its adapter
        final List<FriendlyMessage> friendlyMessages = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(this, R.layout.item_message, friendlyMessages);
        mMessageListView.setAdapter(mMessageAdapter);


        mMessageDatabaseReference3 = database.getReference("counter");
        mMessageDatabaseReference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long post = dataSnapshot.getValue(Long.class);
                counter = post;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        // ImagePickerButton shows an image picker to upload a image for a message
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                mMessageDatabaseReference2 = database.getReference("latest_messages").child(Long.toString(counter));
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        // Enable Send button when there's text to send
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});


        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO: Send messages on click
                String message = mMessageEditText.getText().toString();
                String time = df.format(System.currentTimeMillis());

                FriendlyMessage test = new FriendlyMessage(message, mUsername, null, userID,time);
                mMessageDatabaseReference.push().setValue(test);


                mMessageDatabaseReference2 = database.getReference("latest_messages");
                final Query queryRef = mMessageDatabaseReference2.orderByChild("uid").equalTo(userID);

                queryRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        long u = Long.parseLong(dataSnapshot.getKey());
                        long y = counter;
                        if (y != u) {

                            mMessageDatabaseReference2 = database.getReference("latest_messages").child(dataSnapshot.getKey());
                            mMessageDatabaseReference2.removeValue();
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                mMessageDatabaseReference2 = database.getReference("latest_messages").child(Long.toString(counter));
                if (isAnswered){test = new FriendlyMessage(message, mUsername
                        , "https://firebasestorage.googleapis.com/v0/b/fir-app-6fac7.appspot.com/o/chat_photos%2Fcheck.png?alt=media&token=430a460e-db01-46c5-b161-fb62e62e27e2"
                        , userID
                        ,time);}
                mMessageDatabaseReference2.setValue(test);
                MainActivity.popSound.start();
                mMessageDatabaseReference3.setValue(counter - 1);

                // Clear input box
                mMessageEditText.setText("");
            }
        });


    }

    private void onSignInitialize(String username) {
        mUsername = username;
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                    mMessageAdapter.add(friendlyMessage);
                    scrollMyListViewToBottom();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
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
        isAnswered = false;
        mMessageAdapter.clear();
        if (mChildEventListener != null) {
            mMessageDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.message_manager_menu, menu);
        if (isExpert) {
            getMenuInflater().inflate(R.menu.chat_menu_expert, menu);
        } else {
            getMenuInflater().inflate(R.menu.chat_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.logout) {
            //SignOut
            isExpert = false;
            AuthUI.getInstance().signOut(this);
            onSignOut();

            Intent intent = new Intent(Chat.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (item.getItemId() == R.id.block) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            MainActivity.mMessageDatabaseReference4.push().child("UiD").setValue(userID);
                            Toast.makeText(Chat.this,"This user has been blocked",Toast.LENGTH_SHORT).show();
                            finish();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(Chat.this);
            builder.setMessage("Are you sure you want to block this user?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

        } else if (item.getItemId() == R.id.check) {

            if (item.getTitle().equals("Check")) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

                                MainActivity.popSound.start();
                                item.setIcon(R.drawable.checked);
                                item.setTitle("Checked");

                                mMessageDatabaseReference2 = database.getReference("latest_messages");

                                mMessageDatabaseReference2
                                        .orderByChild("uid")
                                        .equalTo(userID)
                                        .addChildEventListener(new ChildEventListener() {
                                             @Override
                                             public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                                 if (dataSnapshot.exists()){
                                                     FriendlyMessage test = new FriendlyMessage(
                                                             dataSnapshot.getValue(FriendlyMessage.class).getText()
                                                             , dataSnapshot.getValue(FriendlyMessage.class).getName()
                                                             , "https://firebasestorage.googleapis.com/v0/b/fir-app-6fac7.appspot.com/o/chat_photos%2Fcheck.png?alt=media&token=430a460e-db01-46c5-b161-fb62e62e27e2"
                                                             , userID
                                                            ,dataSnapshot.getValue(FriendlyMessage.class).getTime());
                                                     mMessageDatabaseReference2 = database.getReference("latest_messages").child(dataSnapshot.getKey());
                                                     mMessageDatabaseReference2.setValue(test);
                                                     isAnswered = true;
                                                 }
                                             }

                                             @Override
                                             public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                             }

                                             @Override
                                             public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                             }

                                             @Override
                                             public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                             }

                                             @Override
                                             public void onCancelled(@NonNull DatabaseError databaseError) {

                                             }
                                         });
                                        Toast.makeText(Chat.this, "Happy for helping :)", Toast.LENGTH_SHORT).show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(Chat.this);
                builder.setMessage("Mark as answered?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            } else {
                MainActivity.popSound.start();
                item.setIcon(R.drawable.check);
                item.setTitle("Check");

                mMessageDatabaseReference2 = database.getReference("latest_messages");
                mMessageDatabaseReference2
                        .orderByChild("uid")
                        .equalTo(userID)
                        .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                if (dataSnapshot.exists()){
                                    FriendlyMessage test = new FriendlyMessage(
                                            dataSnapshot.getValue(FriendlyMessage.class).getText(),
                                            dataSnapshot.getValue(FriendlyMessage.class).getName()
                                            , null
                                            , userID
                                            ,dataSnapshot.getValue(FriendlyMessage.class).getTime()
                                    );
                                    mMessageDatabaseReference2 = database.getReference("latest_messages").child(dataSnapshot.getKey());
                                    mMessageDatabaseReference2.setValue(test);
                                    isAnswered = false;
                                }
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                Toast.makeText(Chat.this, "Marked as not answered", Toast.LENGTH_SHORT).show();
            }
        } else {

            AlertDialog.Builder alertadd = new AlertDialog.Builder(Chat.this);
            LayoutInflater factory = LayoutInflater.from(Chat.this);
            final View v = factory.inflate(R.layout.experts, null);
            alertadd.setView(v);
            alertadd.show();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        if (isExpert){userID = MessageManager.latestMessagesNames.get(MessageManager._position);}
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }

        if (isExpert){userID =user.getUid();}

        /*
        mMessageAdapter.clear();
        if (mChildEventListener != null) {
            mMessageDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }*/
    }

    private void scrollMyListViewToBottom() {
        mMessageListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                mMessageListView.setSelection(mMessageAdapter.getCount() - 1);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_CANCELED) {
                finish();
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(Chat.this, "this is result ok", Toast.LENGTH_LONG).show();
            }
        } else if (resultCode == RESULT_OK && requestCode == RC_PHOTO_PICKER) {
            Uri selectedImg = data.getData();
            mProgressBar.setVisibility(View.VISIBLE);
            StorageReference photoRef = mChatPhotoStorageReference.child(selectedImg.getLastPathSegment());
            photoRef.putFile(selectedImg).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    Uri downloadUrl = urlTask.getResult();

                    final String sdownload_url = String.valueOf(downloadUrl);
                    final String time = df.format(System.currentTimeMillis());

                    FriendlyMessage friendlyMessage = new FriendlyMessage(null, mUsername, sdownload_url, userID,time);
                    mMessageDatabaseReference.push().setValue(friendlyMessage);
                    mProgressBar.setVisibility(View.INVISIBLE);

                    FriendlyMessage test = new FriendlyMessage("*Sent a photo*", mUsername, null, userID,time);
                    if (isAnswered){test = new FriendlyMessage("*Sent a photo*", mUsername
                            ,"https://firebasestorage.googleapis.com/v0/b/fir-app-6fac7.appspot.com/o/chat_photos%2Fcheck.png?alt=media&token=430a460e-db01-46c5-b161-fb62e62e27e2"
                            ,userID,time);}
                    mMessageDatabaseReference2.setValue(test);
                    mMessageDatabaseReference3.setValue(counter - 1);
                    MainActivity.popSound.start();

                    mMessageDatabaseReference2 = database.getReference("latest_messages");
                    final Query queryRef = mMessageDatabaseReference2.orderByChild("uid").equalTo(userID);

                    queryRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            long u = Long.parseLong(dataSnapshot.getKey());
                            long y = counter;
                            if (y != u) {

                                mMessageDatabaseReference2 = database.getReference("latest_messages").child(dataSnapshot.getKey());
                                mMessageDatabaseReference2.removeValue();
                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
        }

    }


    // To color username in the chat

    public static class thread extends Thread {
        TextView name;

        thread(TextView name) {
            this.name = name;
        }


        @Override
        public void run() {
            super.run();
            try {
                if (!name.getText().toString().equals("")) {
                    if (user.getDisplayName().equals(name.getText().toString())) {
                        name.setTextColor(Color.parseColor("#3261fa"));
                    } else {
                        name.setTextColor(Color.parseColor("#fa8612"));
                    }
                }
            } catch (Exception e) {
            }
        }
    }
}

