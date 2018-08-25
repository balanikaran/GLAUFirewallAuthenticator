package com.krnblni.evetech.glaufirewallauthenticator.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;

import com.krnblni.evetech.glaufirewallauthenticator.services.HelperForegroundService;

public class BootCompleteRunForegroundServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("initial_setup", Context.MODE_PRIVATE);
            if (sharedPreferences.getBoolean("foregroundServiceStateUserPreference", false)) {
                Intent helperForegroundServiceIntent = new Intent(context, HelperForegroundService.class);
                ContextCompat.startForegroundService(context, helperForegroundServiceIntent);
            }
        }
    }
}
