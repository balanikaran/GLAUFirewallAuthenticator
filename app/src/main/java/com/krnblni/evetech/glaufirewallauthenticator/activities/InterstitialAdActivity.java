package com.krnblni.evetech.glaufirewallauthenticator.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.krnblni.evetech.glaufirewallauthenticator.R;
import com.krnblni.evetech.glaufirewallauthenticator.helpers.InterstitialAdManager;

public class InterstitialAdActivity extends AppCompatActivity {

    String TAG = "Logging - InterstitialAdActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial_ad);
        Log.e(TAG, "onCreate: " + "activity started" );
        InterstitialAdManager interstitialAdManager = InterstitialAdManager.getInstance();
        InterstitialAd interstitialAd = interstitialAdManager.getInterstitialAd();
        if (interstitialAd == null){
            Log.e(TAG, "onCreate: " + "ad is null" );
            finish();
        }

        if(interstitialAd != null && interstitialAd.isLoaded()){
            interstitialAd.show();
            interstitialAd.setAdListener(new AdListener(){
                @Override
                public void onAdClosed() {
                    finish();
                }
            });
        }
    }
}
