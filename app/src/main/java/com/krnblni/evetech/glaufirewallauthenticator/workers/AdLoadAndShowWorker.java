package com.krnblni.evetech.glaufirewallauthenticator.workers;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.krnblni.evetech.glaufirewallauthenticator.services.HelperForegroundService;
import com.krnblni.evetech.glaufirewallauthenticator.services.LoadAdForegroundService;

public class AdLoadAndShowWorker extends Worker {

    private String TAG = "Logging - AdLoadAndShowWorker";

    public AdLoadAndShowWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Log.e(TAG, "doWork: " + "called");
        if (isHelperServiceRunning()) {
            Intent loadAdForegroundServiceIntent = new Intent(getApplicationContext(), LoadAdForegroundService.class);
            getApplicationContext().startService(loadAdForegroundServiceIntent);
        } else {
            Log.e(TAG, "doWork: " + "helper service not running, job cancelled!");
            WorkManager.getInstance(getApplicationContext()).cancelWorkById(this.getId());
        }

        return Result.success();
    }

    private boolean isHelperServiceRunning() {
        ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
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
