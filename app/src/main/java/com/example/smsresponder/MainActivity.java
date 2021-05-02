package com.example.smsresponder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int MY_PERMISSIONS_REQUEST_SMS_RECEIVE = 10;

    Intent serviceIntent;
    private SmsBackgroundService service;

    private TextView textView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!this.isSmsPermissionGranted()){
         this.requestReadAndSendSmsPermission();
        }

        setContentView(R.layout.activity_main);
        service = new SmsBackgroundService();
        serviceIntent = new Intent(this ,service.getClass());

        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);

        updateText();
    }

    public void buttonClick(View view){
        if (isMyServiceRunning(service.getClass())) {
            stopService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
        updateText();
    }

    private void updateText(){
        if (!isMyServiceRunning(service.getClass())) {
            textView.setText("Service not running");
            textView.setTextColor(Color.rgb(255,0,0));
            button.setText("Start Service");
        } else {
            textView.setText("Service running");
            textView.setTextColor(Color.rgb(0,255,0));
            button.setText("Stop Service");
        }
    }


    private void requestReadAndSendSmsPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SMS_RECEIVE);
    }

    public boolean isSmsPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }

}