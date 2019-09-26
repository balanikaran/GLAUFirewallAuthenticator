package com.krnblni.evetech.glaufirewallauthenticator.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.krnblni.evetech.glaufirewallauthenticator.R;

public class SplashScreenActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        scheduleSplashScreen();
    }

    private void scheduleSplashScreen() {

        Handler handler = new Handler();

        SharedPreferences sharedPreferences = getSharedPreferences("initial_setup", MODE_PRIVATE);
        boolean initialSetupBoolean = sharedPreferences.getBoolean("initial_setup", false);


        if (initialSetupBoolean) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, InitialSetupActivity.class);
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 500L);

    }
}
