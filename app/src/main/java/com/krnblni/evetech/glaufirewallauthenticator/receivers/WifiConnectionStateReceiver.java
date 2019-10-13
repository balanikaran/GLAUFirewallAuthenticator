package com.krnblni.evetech.glaufirewallauthenticator.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.krnblni.evetech.glaufirewallauthenticator.services.LoginForegroundService;
import com.krnblni.evetech.glaufirewallauthenticator.workers.AdLoadAndShowWorker;
import com.krnblni.evetech.glaufirewallauthenticator.workers.LoginInitiatorWorker;

import java.util.concurrent.TimeUnit;

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

            assert networkInfo != null;
            if (networkInfo.isConnected()) {
                Log.e(TAG, "onReceive: " + "connected");
                context.stopService(loginForegroundServiceIntent);
                context.startService(loginForegroundServiceIntent);

                PeriodicWorkRequest periodicLoginWork = new PeriodicWorkRequest.Builder(LoginInitiatorWorker.class, 15, TimeUnit.MINUTES, 5, TimeUnit.MINUTES).addTag("loginInitiatorWork").build();
                WorkManager.getInstance(context).enqueueUniquePeriodicWork("periodicLoginWorkName", ExistingPeriodicWorkPolicy.KEEP, periodicLoginWork);

            } else if (!networkInfo.isConnected()) {
                Log.e(TAG, "onReceive: " + "disconnected");
                context.stopService(loginForegroundServiceIntent);
                try {
                    WorkManager.getInstance(context).cancelAllWorkByTag("loginInitiatorWork");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
