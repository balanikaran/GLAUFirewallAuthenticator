package com.krnblni.evetech.glaufirewallauthenticator.workers;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.krnblni.evetech.glaufirewallauthenticator.services.LoginForegroundService;

import androidx.work.Worker;

public class ReInitiateLoginWorker extends Worker {
    @NonNull
    @Override
    public Result doWork() {

        Intent loginForegroundServiceIntent = new Intent(getApplicationContext(), LoginForegroundService.class);
        getApplicationContext().startService(loginForegroundServiceIntent);

        return Result.SUCCESS;
    }
}
