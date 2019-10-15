package com.krnblni.evetech.glaufirewallauthenticator.receivers;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.krnblni.evetech.glaufirewallauthenticator.R;
import com.krnblni.evetech.glaufirewallauthenticator.activities.MainActivity;
import com.krnblni.evetech.glaufirewallauthenticator.services.LoginForegroundService;
import com.krnblni.evetech.glaufirewallauthenticator.workers.LoginInitiatorWorker;

import java.util.concurrent.TimeUnit;

public class WifiConnectionStateReceiver extends BroadcastReceiver {

    String TAG = "Logging - WifiConnectionStateReceiver ";
    Intent loginForegroundServiceIntent;

    int helperForegroundServiceID = 100;
    String notificationChannelIdForHelperService = "1000";

    @Override
    public void onReceive(Context context, Intent intent) {

        loginForegroundServiceIntent = new Intent(context, LoginForegroundService.class);
        Log.e(TAG, "onReceive: " + "called");

        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {

            NetworkInfo networkInfo =
                    intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

            assert networkInfo != null;
            if (networkInfo.isConnected()) {
                Log.e(TAG, "onReceive: " + "connected");
                updateNotification("Wi-Fi Connected, Awaiting Update", context);
                context.stopService(loginForegroundServiceIntent);
                context.startService(loginForegroundServiceIntent);

                PeriodicWorkRequest periodicLoginWork = new PeriodicWorkRequest.Builder(LoginInitiatorWorker.class, 15, TimeUnit.MINUTES, 5, TimeUnit.MINUTES).build();
                WorkManager.getInstance(context).enqueueUniquePeriodicWork("periodicLoginWorkName", ExistingPeriodicWorkPolicy.KEEP, periodicLoginWork);

            } else if (!networkInfo.isConnected()) {
                Log.e(TAG, "onReceive: " + "disconnected");
                context.stopService(loginForegroundServiceIntent);
                WorkManager.getInstance(context).cancelUniqueWork("periodicLoginWorkName");
                updateNotification("Wi-Fi Disconnected", context);
            }
        }

    }

    public void updateNotification(String notificationMessage, Context context) {
        Intent mainActivityIntent = new Intent(context, MainActivity.class);
        PendingIntent mainActivityPendingIntent = PendingIntent.getActivity(
                context,
                300,
                mainActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        if (notificationMessage == null || notificationMessage.equals("")) {
            notificationMessage = "Unknown";
        }
        Notification foregroundServiceNotification = new NotificationCompat.Builder(context,
                notificationChannelIdForHelperService)
                .setSmallIcon(R.drawable.ic_stat_app_icon_notification)
                .setContentTitle("Service is up and running 😉")
                .setContentText("Status: " + notificationMessage)
                .setContentIntent(mainActivityPendingIntent)
                .setGroup("helperServiceGroup")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .setBigContentTitle("Status: " + notificationMessage)
                        .bigText(context.getString(R.string.notification_info_text))
                ).build();
        NotificationManagerCompat.from(context).notify(helperForegroundServiceID, foregroundServiceNotification);
    }
}
