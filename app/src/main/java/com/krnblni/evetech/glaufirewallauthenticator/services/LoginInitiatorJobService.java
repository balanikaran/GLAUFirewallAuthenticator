package com.krnblni.evetech.glaufirewallauthenticator.services;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class LoginInitiatorJobService extends JobService {

    String TAG = "Logging - LoginInitiatorJobService";

    @Override
    public boolean onStartJob(JobParameters job) {
        Log.e(TAG, "onStartJob: " + "called" );
        if(isHelperServiceRunning()){
            Intent loginForegroundServiceIntent = new Intent(getApplicationContext(), LoginForegroundService.class);
            getApplicationContext().startService(loginForegroundServiceIntent);
        }else {
            Log.e(TAG, "onStartJob: " + "helper service not running, job cancelled" );
            FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));
            firebaseJobDispatcher.cancel(job.getTag());
        }
        jobFinished(job, false);
        return false;
    }

    @Override
    public boolean onStopJob(com.firebase.jobdispatcher.JobParameters job) {
        Log.e(TAG, "onStopJob: " + "called" );
        return false;
    }

    private boolean isHelperServiceRunning() {

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
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
