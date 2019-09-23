package com.krnblni.evetech.glaufirewallauthenticator.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.krnblni.evetech.glaufirewallauthenticator.R;
import com.krnblni.evetech.glaufirewallauthenticator.activities.MainActivity;
import com.krnblni.evetech.glaufirewallauthenticator.receivers.WifiConnectionStateReceiver;

public class HelperForegroundService extends Service {

    String TAG = "Logging - HelperForegroundService: ";
    String notificationChannelIdForHelperService = "1000";
    int foregroundServiceID = 100;

    WifiConnectionStateReceiver wifiConnectionStateReceiver = new WifiConnectionStateReceiver();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        Log.e(TAG, "onCreate: " + "called");

        Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent mainActivityPendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                300,
                mainActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        Notification foregroundServiceNotification = new NotificationCompat.Builder(getApplicationContext(),
                notificationChannelIdForHelperService)
                .setSmallIcon(R.drawable.ic_stat_app_icon_notification)
                .setContentTitle("Service is up and running 😉")
                .setContentText("Having trouble? Try logging in again!")
                .setContentIntent(mainActivityPendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.notification_info_text)))
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
        try {
            unregisterReceiver(wifiConnectionStateReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
