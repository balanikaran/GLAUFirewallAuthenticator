package com.krnblni.evetech.glaufirewallauthenticator.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.krnblni.evetech.glaufirewallauthenticator.R;
import com.krnblni.evetech.glaufirewallauthenticator.activities.InterstitialAdActivity;
import com.krnblni.evetech.glaufirewallauthenticator.helpers.InterstitialAdManager;

public class LoadAdForegroundService extends Service {

    String TAG = "Logging - LoadAdForegroundService";
    int foregroundServiceID = 250;
    String notificationChannelIdForAdsAndService = "2000";
    private InterstitialAd interstitialAd;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate: " + "called");
        Notification foregroundServiceNotification = new NotificationCompat.Builder(getApplicationContext(),
                notificationChannelIdForAdsAndService)
                .setSmallIcon(R.drawable.ic_stat_app_icon_notification)
                .setContentTitle("GLAU FireAuth")
                .setContentText("Loading online contents...")
                .setGroup("adsServiceGroup")
                .build();
        startForeground(foregroundServiceID, foregroundServiceNotification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: " + "service started");
        loadAd();
        return START_STICKY;
    }

    private void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(getApplicationContext(),
                getApplicationContext().getString(R.string.ad_interstitial_id_1),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd loadedInterstitialAd) {
                        super.onAdLoaded(loadedInterstitialAd);
                        Log.e(TAG, "onAdLoaded: " + "ad loaded");

                        interstitialAd = loadedInterstitialAd;

                        InterstitialAdManager interstitialAdManager = InterstitialAdManager.getInstance();
                        interstitialAdManager.setInterstitialAd(interstitialAd);

                        Intent interstitialAdActivityIntent = new Intent(getApplicationContext(), InterstitialAdActivity.class);
                        PendingIntent interstitialAdActivityPendingIntent = PendingIntent.getActivity(getApplicationContext(), 500, interstitialAdActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), notificationChannelIdForAdsAndService)
                                .setSmallIcon(R.drawable.web_hi_res_512)
                                .setContentTitle("Show your appreciation!")
                                .setContentText("This is to support the development of the app.")
                                .setOngoing(true)
                                .setGroup("adsServiceGroup")
                                .setContentIntent(interstitialAdActivityPendingIntent)
                                .setAutoCancel(true);

                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                        notificationManagerCompat.notify(450, builder.build());
                        stopSelf();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.e(TAG, "onAdFailedToLoad: " + "ad failed to load, code = " + loadAdError.toString());
                        stopSelf();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: " + "called");
    }
}
