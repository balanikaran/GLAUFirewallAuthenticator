package com.krnblni.evetech.glaufirewallauthenticator.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.krnblni.evetech.glaufirewallauthenticator.R;
import com.krnblni.evetech.glaufirewallauthenticator.activities.MainActivity;
import com.krnblni.evetech.glaufirewallauthenticator.helpers.InterstitialAdManager;
import com.krnblni.evetech.glaufirewallauthenticator.receivers.WifiConnectionStateReceiver;
import com.krnblni.evetech.glaufirewallauthenticator.workers.AdLoadAndShowWorker;

import java.util.concurrent.TimeUnit;

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
                .setContentText("Status: Awaiting Update")
                .setContentIntent(mainActivityPendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .setBigContentTitle("Status: Awaiting Update")
                        .bigText(getString(R.string.notification_info_text))
                )
                .build();

        startForeground(foregroundServiceID, foregroundServiceNotification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e(TAG, "onStartCommand: " + "service started " + startId);

        IntentFilter intentFilter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(wifiConnectionStateReceiver, intentFilter);
        createSingletonAdManagerObject();

//        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(AdLoadAndShowWorker.class).setInitialDelay(10, TimeUnit.SECONDS).build();
//        WorkManager.getInstance(getApplicationContext()).enqueue(oneTimeWorkRequest);

        PeriodicWorkRequest periodicAdWork = new PeriodicWorkRequest.Builder(AdLoadAndShowWorker.class, 1, TimeUnit.HOURS, 20, TimeUnit.MINUTES).addTag("periodicAdsLoadWork").build();
        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork("periodicAdWorkName", ExistingPeriodicWorkPolicy.KEEP, periodicAdWork);

        return START_STICKY;
    }

    private void createSingletonAdManagerObject() {
        InterstitialAdManager interstitialAdManager = InterstitialAdManager.getInstance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: " + "called");
        try {
            WorkManager.getInstance(getApplicationContext()).cancelAllWorkByTag("periodicAdsLoadWork");
            unregisterReceiver(wifiConnectionStateReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
