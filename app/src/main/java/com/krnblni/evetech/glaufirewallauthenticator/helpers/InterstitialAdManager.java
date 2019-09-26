package com.krnblni.evetech.glaufirewallauthenticator.helpers;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.krnblni.evetech.glaufirewallauthenticator.R;
import com.krnblni.evetech.glaufirewallauthenticator.activities.InterstitialAdActivity;

public class InterstitialAdManager {

    private static InterstitialAd interstitialAd;

    public void createAndLoadAd(final Context context) {
        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId(context.getString(R.string.ad_interstitial_id_1));
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

                Intent interstitialAdActivityIntent = new Intent(context, InterstitialAdActivity.class);
                PendingIntent interstitialAdActivityPendingIntent = PendingIntent.getActivity(context, 500, interstitialAdActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1000")
                        .setSmallIcon(R.drawable.ic_stat_app_icon_notification)
                        .setContentTitle("Click here to close this notification.")
                        .setOngoing(true)
                        .setContentIntent(interstitialAdActivityPendingIntent)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(450, builder.build());
            }
        });
    }

    public InterstitialAd getAd() {
        return interstitialAd;
    }
}
