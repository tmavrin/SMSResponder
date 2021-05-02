package com.example.smsresponder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.provider.Telephony;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class SmsBackgroundService extends Service {
    private SmsBroadcastReceiver smsBroadcastReceiver;

    @Override
    public void onCreate() {
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }
        super.onCreate();
    }

    public SmsBackgroundService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("service", "start");
        if(smsBroadcastReceiver == null) {
            smsBroadcastReceiver = new SmsBroadcastReceiver("13880", "13880");
            registerReceiver(smsBroadcastReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));
        }
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        unregisterReceiver(smsBroadcastReceiver);
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.e("service", "bind");
        return null;
    }
}