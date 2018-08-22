package com.krnblni.evetech.glaufirewallauthenticator.receivers;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.krnblni.evetech.glaufirewallauthenticator.services.HelperForegroundService;

public class RestartForegroundServiceReceiver extends BroadcastReceiver {

    String TAG = "Logging - RestartForegroundServiceReceiver ";
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        Log.e(TAG, "onReceive: " + "called");

        SharedPreferences sharedPreferences = context.getSharedPreferences("initial_setup", Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean("foregroundServiceStateUserPreference", false)) {
            Intent helperServiceIntent = new Intent(context, HelperForegroundService.class);
            ContextCompat.startForegroundService(context, helperServiceIntent);
        } else {
            Log.e(TAG, "onReceive: " + "called" + "service already running");
        }

    }

    private boolean isHelperServiceRunning() {

        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (HelperForegroundService.class.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }
}
