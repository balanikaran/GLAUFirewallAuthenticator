package com.krnblni.evetech.glaufirewallauthenticator.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.krnblni.evetech.glaufirewallauthenticator.services.LoginForegroundService;
import com.krnblni.evetech.glaufirewallauthenticator.services.LoginInitiatorJobService;

public class WifiConnectionStateReceiver extends BroadcastReceiver {

    String TAG = "Logging - WifiConnectionStateReceiver ";
    Intent loginForegroundServiceIntent;

    FirebaseJobDispatcher firebaseJobDispatcher;

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

                firebaseJobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
                Job job = firebaseJobDispatcher.newJobBuilder()
                        .setService(LoginInitiatorJobService.class)
                        .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                        .setRecurring(true)
                        .setTag("reInitiateLoginJobServiceTag")
                        .setTrigger(Trigger.executionWindow(360, 480))
                        .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                        .setReplaceCurrent(true)
                        .build();

                firebaseJobDispatcher.mustSchedule(job);
            } else if (!networkInfo.isConnected()) {
                Log.e(TAG, "onReceive: " + "disconnected");
                context.stopService(loginForegroundServiceIntent);
                try{
                    firebaseJobDispatcher.cancel("reInitiateLoginJobServiceTag");
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }

    }
}
