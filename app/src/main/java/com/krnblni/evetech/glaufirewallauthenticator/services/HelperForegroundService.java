package com.krnblni.evetech.glaufirewallauthenticator.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.krnblni.evetech.glaufirewallauthenticator.R;
import com.krnblni.evetech.glaufirewallauthenticator.receivers.WifiConnectionStateReceiver;

public class HelperForegroundService extends Service {

    String TAG = "Logging - HelperForegroundService: ";
    String notificationChannelIdForHelperService = "1000";
    int foregroundServiceID = 200;

    WifiConnectionStateReceiver wifiConnectionStateReceiver = new WifiConnectionStateReceiver();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        Log.e(TAG, "onCreate: " + "called");

        Notification foregroundServiceNotification = new NotificationCompat.Builder(getApplicationContext(),
                notificationChannelIdForHelperService)
                .setSmallIcon(R.drawable.ic_stat_app_icon_notification)
                .setContentTitle("Service up and running ;)")
                .build();

        startForeground(foregroundServiceID, foregroundServiceNotification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e(TAG, "onStartCommand: " + "service started " + startId);

        IntentFilter intentFilter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(wifiConnectionStateReceiver, intentFilter);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: " + "called");
        unregisterReceiver(wifiConnectionStateReceiver);
    }
}
