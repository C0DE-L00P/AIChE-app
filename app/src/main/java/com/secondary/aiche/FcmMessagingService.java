package com.secondary.aiche;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.secondary.aiche.Chat.Chat;
import com.secondary.aiche.Events.Events;
import com.secondary.aiche.Internship.Internship;
import com.secondary.aiche.Knowledge.CoursesType;
import com.secondary.aiche.Study.Study;

import androidx.core.app.NotificationCompat;

public class FcmMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size()>0){
            String title,message,img_url,type;

            type = remoteMessage.getData().get("type");
            title = remoteMessage.getData().get("title");
            message = remoteMessage.getData().get("message");
            img_url = remoteMessage.getData().get("img_url");
            Intent intent;

            if (type.equals("Event")) { intent = new Intent(this, Events.class);}
            else if(type.equals("Internship")){ intent = new Intent(this, Internship.class);}
            else if (type.equals("Knowledge")){ intent = new Intent(this, CoursesType.class);}
            else if (type.equals("Study")){ intent = new Intent(this, Study.class);}
            else if ((type.equals("Chat"))){ intent = new Intent(this, Chat.class);}
            else {intent = new Intent(this, MainActivity.class);}

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
            Uri sounduri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.event_notify);
            //Uri sounduri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.drawable.ic_notification)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                    .setContentIntent(pendingIntent)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setDefaults(~Notification.DEFAULT_SOUND)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setSound(sounduri);

            com.android.volley.toolbox.ImageRequest imageRequest = new com.android.volley.toolbox.ImageRequest(img_url , new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {

                    builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(response));
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(0,builder.build());
                }
            },0,0,null,Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            MySingleton.getmInstance(this).addToRequestQue(imageRequest);
        }
    }
}