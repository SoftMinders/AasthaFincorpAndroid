package com.af.aasthafincorp.Services;


import static com.af.aasthafincorp.Utility.ConstantClass.CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.af.aasthafincorp.Activity.LoginActivity;
import com.af.aasthafincorp.R;
import com.af.aasthafincorp.Utility.ConstantClass;
import com.af.aasthafincorp.Utility.SaveSharedPreferences;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessageService";
    Bitmap bitmap;
    boolean b = false;
    String imgUri = "";
    String title, message, click_action;
    Intent intent;
    private static final int REQUEST_CODE = 1;
    private static final int NOTIFICATION_ID = 6578;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "All Message Data: " + remoteMessage);
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        /*{icon=myicon, image=https://sachosatrambhajan.in/./album/132 Sala Jashan e Melo-III.jpg, sound=mySound, title=SachoSatram!}*/
        title = remoteMessage.getData().get("title");
        message = remoteMessage.getData().get("body");
        click_action = remoteMessage.getData().get("click_action");

        SaveSharedPreferences.setClickAction(this,click_action);


        if (remoteMessage.getData().containsKey("image")) {
            imgUri = remoteMessage.getData().get("image");
            if (!imgUri.isEmpty()) {
                bitmap = getBitmapfromUrl(imgUri);
                b = true;
            }
        }

        if(remoteMessage.getNotification().getBody().equalsIgnoreCase("New notice assign to you!")){
            Log.d("In if:","sendNotificationUrl");
        }

        sendNotification(title, message, click_action);
        showNotifications(title,message);
        //sendNotification1(title, message);
        /*new generatePictureStyleNotification(this,"Title", "Message",
                "http://api.androidhive.info/images/sample.jpg").execute();*/
    }
    private void sendNotification(String title, String message, String click_action) {
        Log.d(TAG,"Click Action Firebase="+click_action);
        //intent = new Intent(this, WebdemoActivity.class);


        if (SaveSharedPreferences.getUserObject(this).isEmpty()) {
            intent = new Intent(this, LoginActivity.class);
        }
        //title = title.replace("<br>", "\n");
        //message = message.replace("<br>", "\n");



        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_new_logo))/*Notification icon image*/
                .setSmallIcon(R.drawable.ic_launcher_new_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setShowWhen(true)
                .setColorized(true)

                .setColor(getResources().getColor(R.color.main_prim))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))

                .setSound(defaultSoundUri)
                .setVibrate(new long[]{1000})
                .setContentIntent(pendingIntent);
                  /*  if (b){
                        notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                                //.setBigContentTitle(messageBody)
                                .bigPicture(getBitmapfromUrl(imageUri)));
                    }*/


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    ConstantClass.CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(false);
            channel.enableVibration(true);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            notificationManager.createNotificationChannel(channel);
        }
        else {

            // startForeground(1, notification);
        }


        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        assert notificationManager != null;
        notificationManager.notify(m, notificationBuilder.build());
    }
    private Bitmap getBitmapfromUrl(String imageUri) {

        try {
            URL url = new URL(imageUri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    private void showNotifications(String title, String msg) {


    }



}
