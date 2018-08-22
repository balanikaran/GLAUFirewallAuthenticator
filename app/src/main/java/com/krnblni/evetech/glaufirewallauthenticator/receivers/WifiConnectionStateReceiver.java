package com.krnblni.evetech.glaufirewallauthenticator.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.krnblni.evetech.glaufirewallauthenticator.services.LoginForegroundService;
import com.krnblni.evetech.glaufirewallauthenticator.workers.ReInitiateLoginWorker;

import java.util.concurrent.TimeUnit;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class WifiConnectionStateReceiver extends BroadcastReceiver {

    String TAG = "Logging - WifiConnectionStateReceiver ";
    Intent loginForegroundServiceIntent;

    @Override
    public void onReceive(Context context, Intent intent) {

        loginForegroundServiceIntent = new Intent(context, LoginForegroundService.class);
        Log.e(TAG, "onReceive: " + "called");

        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {

            NetworkInfo networkInfo =
                    intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {
                Log.e(TAG, "onReceive: " + "connected");
                context.stopService(loginForegroundServiceIntent);
                context.startService(loginForegroundServiceIntent);
                PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                        ReInitiateLoginWorker.class, PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS,
                        PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS, TimeUnit.MILLISECONDS
                ).addTag("LoginPeriodicWorkRequest").build();
                WorkManager.getInstance().enqueueUniquePeriodicWork(
                        "ReInitiateLogin",
                        ExistingPeriodicWorkPolicy.REPLACE,
                        periodicWorkRequest
                );
            } else if (!networkInfo.isConnected()) {
                Log.e(TAG, "onReceive: " + "disconnected");
                context.stopService(loginForegroundServiceIntent);
                WorkManager.getInstance().cancelAllWorkByTag("LoginPeriodicWorkRequest");
            }
        }

    }
}
