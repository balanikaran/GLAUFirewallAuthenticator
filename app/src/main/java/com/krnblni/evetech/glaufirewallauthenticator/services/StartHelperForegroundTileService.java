package com.krnblni.evetech.glaufirewallauthenticator.services;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.widget.Toast;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.krnblni.evetech.glaufirewallauthenticator.R;
import com.krnblni.evetech.glaufirewallauthenticator.helpers.InterstitialAdManager;

@TargetApi(Build.VERSION_CODES.N)
public class StartHelperForegroundTileService extends TileService {

    String TAG = "Logging - StartHelperForegroundTileService";
    Tile glauFireAuthTile;
    Intent helperForegroundServiceIntent, loginForegroundServiceIntent, interstitialAdForegroundServiceIntent;
    FirebaseJobDispatcher firebaseJobDispatcher;

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        glauFireAuthTile = getQsTile();
        Log.e(TAG, "onTileAdded: " + "called");
        if (isHelperServiceRunning()) {
            glauFireAuthTile.setState(Tile.STATE_ACTIVE);
        } else {
            glauFireAuthTile.setState(Tile.STATE_INACTIVE);
        }
        glauFireAuthTile.updateTile();
    }

    @Override
    public void onClick() {
        super.onClick();
        Log.e(TAG, "onClick: " + "called");
        glauFireAuthTile = getQsTile();
        SharedPreferences sharedPreferences = getSharedPreferences("initial_setup", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        firebaseJobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));

        Log.e(TAG, "onClick: " + getQsTile().getState());

        helperForegroundServiceIntent = new Intent(getApplicationContext(), HelperForegroundService.class);
        loginForegroundServiceIntent = new Intent(getApplicationContext(), LoginForegroundService.class);

        if (getQsTile().getState() == Tile.STATE_ACTIVE) {
            editor.putBoolean("foregroundServiceStateUserPreference", false);
            getApplicationContext().stopService(helperForegroundServiceIntent);
            getApplicationContext().stopService(loginForegroundServiceIntent);
            firebaseJobDispatcher.cancel("reInitiateLoginJobServiceTag");
            glauFireAuthTile.setState(Tile.STATE_INACTIVE);
            glauFireAuthTile.updateTile();
        } else if (getQsTile().getState() == Tile.STATE_INACTIVE) {
            if (sharedPreferences.getBoolean("initial_setup", false)) {
                editor.putBoolean("foregroundServiceStateUserPreference", true);
                ContextCompat.startForegroundService(getApplicationContext(), helperForegroundServiceIntent);

                glauFireAuthTile.setState(Tile.STATE_ACTIVE);
                glauFireAuthTile.updateTile();

            } else {
                Toast.makeText(this, "Complete the initial setup first!", Toast.LENGTH_SHORT).show();
            }
        }
        editor.apply();
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
        Log.e(TAG, "onTileRemoved: " + "called");
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        Log.e(TAG, "onStartListening: " + "called");
        glauFireAuthTile = getQsTile();
        if (isHelperServiceRunning()) {
            glauFireAuthTile.setState(Tile.STATE_ACTIVE);
        } else {
            glauFireAuthTile.setState(Tile.STATE_INACTIVE);
        }
        glauFireAuthTile.updateTile();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
        Log.e(TAG, "onStopListening: " + "called");
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
