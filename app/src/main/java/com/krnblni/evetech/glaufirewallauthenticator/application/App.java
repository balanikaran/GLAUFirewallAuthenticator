package com.krnblni.evetech.glaufirewallauthenticator.application;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import com.krnblni.evetech.glaufirewallauthenticator.helpers.InterstitialAdManager;

public class App extends Application {

    String notificationChannelIdForHelperService = "1000";
    String notificationChannelIdForAdsAndService = "2000";
    String notificationChannelNameForHelperService = "Helper Foreground Service Channel";
    String notificationChannelNameForAdsAndService = "Support Service Channel";

    @Override
    public void onCreate() {
        createNotificationChannels();
        super.onCreate();
    }

    private void createNotificationChannels() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannelForHelperService = new NotificationChannel(
                    notificationChannelIdForHelperService,
                    notificationChannelNameForHelperService,
                    NotificationManager.IMPORTANCE_MIN
            );

            NotificationChannel notificationChannelForAdsAndService = new NotificationChannel(
                    notificationChannelIdForAdsAndService,
                    notificationChannelNameForAdsAndService,
                    NotificationManager.IMPORTANCE_LOW
            );


            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannelForHelperService);
                notificationManager.createNotificationChannel(notificationChannelForAdsAndService);
            }
        }
    }

}
