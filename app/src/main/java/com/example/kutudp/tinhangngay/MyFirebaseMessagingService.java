package com.example.kutudp.tinhangngay;

import android.annotation.SuppressLint;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService{
    private static final String TAG = "MyFirebaseMessagingService";
    private Activity currentActivity;



    @SuppressLint("LongLogTag")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG,"FROM"+ remoteMessage.getFrom());
        //Check if the message contains data
        if(remoteMessage.getData().size() > 0){
            Log.d(TAG,"MESSAGE DATA"+ remoteMessage.getData());
        }
        //Check if the message contains  notification

        if(remoteMessage.getNotification() != null){
            Log.d(TAG,"MESSAGE BODY"+ remoteMessage.getNotification().getBody());
            MainActivity.setNotify(remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody());

        }
    }
    /* Display the notification
    * @param body
    * */
    @SuppressLint("NewApi")
    private void sendNotification(String body) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        //set sound of notification
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder nBuilder = new Notification.Builder(this).setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("TinHangNgay thông báo")
                .setContentText("TinHangNgay vừa cập nhập "+ body +" tin mới")
                .setSound(notificationSound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0/*ID of notification*/, nBuilder.build());

    }
}
