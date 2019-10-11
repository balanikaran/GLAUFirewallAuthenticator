package com.krnblni.evetech.glaufirewallauthenticator.application;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import com.krnblni.evetech.glaufirewallauthenticator.helpers.InterstitialAdManager;

public class App extends Application {

    String notificationChannelIdForHelperService = "1000";
    String notificationChannelNameForHelperService = "Notification channel for Helper Foreground Service";

    @Override
    public void onCreate() {
        createNotificationChannel();
        super.onCreate();
    }

    private void createNotificationChannel() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannelForHelperService = new NotificationChannel(
                    notificationChannelIdForHelperService,
                    notificationChannelNameForHelperService,
                    NotificationManager.IMPORTANCE_MIN
            );

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannelForHelperService);
            }
        }
    }

}
