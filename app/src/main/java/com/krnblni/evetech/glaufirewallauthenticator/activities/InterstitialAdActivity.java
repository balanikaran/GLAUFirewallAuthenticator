package com.krnblni.evetech.glaufirewallauthenticator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.krnblni.evetech.glaufirewallauthenticator.R;
import com.krnblni.evetech.glaufirewallauthenticator.helpers.InterstitialAdManager;

public class InterstitialAdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial_ad);

        InterstitialAdManager interstitialAdManager = new InterstitialAdManager();
        InterstitialAd interstitialAd = interstitialAdManager.getAd();

        if(interstitialAd.isLoaded()){
            interstitialAd.show();
            interstitialAd.setAdListener(new AdListener(){
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    finish();
                }
            });
        }
    }
}
