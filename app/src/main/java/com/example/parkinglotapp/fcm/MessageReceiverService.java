package com.example.parkinglotapp.fcm;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.parkinglotapp.MainActivity;
import com.example.parkinglotapp.NotificationActivity;
import com.example.parkinglotapp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MessageReceiverService extends FirebaseMessagingService {

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
        Log.d("AAA1","onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Destroy", Toast.LENGTH_SHORT).show();
        Log.d("AAA1","onDestroy");
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d("AAA1","da nhan duoc");
        if(remoteMessage.getNotification()!=null){
            String title=remoteMessage.getNotification().getTitle();
            String content=remoteMessage.getNotification().getBody();
            NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            String NOTIFICATION_CHANNEL_ID="PARKING LOT";
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                @SuppressLint("WrongConstant")
                NotificationChannel notificationChannel=new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                        "Parking lot",
                        NotificationManager.IMPORTANCE_MAX);
                notificationChannel.setDescription("test test test");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            NotificationCompat.Builder builder=new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            builder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_message)
                    .setContentTitle(title)
                    .setContentText(content);
//            startForeground(1,builder.build());
            notificationManager.notify(1,builder.build());
            Intent intent=new Intent(getApplicationContext(), NotificationActivity.class);
            intent.putExtra("test","somethings");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
