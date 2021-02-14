package com.krnblni.evetech.glaufirewallauthenticator.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.krnblni.evetech.glaufirewallauthenticator.R;
import com.krnblni.evetech.glaufirewallauthenticator.helpers.InterstitialAdManager;

public class InterstitialAdActivity extends AppCompatActivity {

    String TAG = "Logging - InterstitialAdActivity";
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial_ad);
        Log.e(TAG, "onCreate: " + "activity started" );

        InterstitialAdManager interstitialAdManager = InterstitialAdManager.getInstance();
        interstitialAd = interstitialAdManager.getInterstitialAd();

        if (interstitialAd == null){
            Log.e(TAG, "onCreate: " + "ad is null" );
            finish();
        }

        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
                Log.e(TAG, "onAdDismissedFullScreenContent: " + "Ad dismissed" );
                finish();
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);
                interstitialAd = null;
                Log.e(TAG, "onAdFailedToShowFullScreenContent: " + "Ad failed to show" );
                finish();
            }

            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
                Log.e(TAG, "onAdShowedFullScreenContent: " + "Ad shown" );
            }
        });

        if (interstitialAd != null) {
            interstitialAd.show(this);
        } else {
            Log.e(TAG, "onCreate: " + "Ad was not ready yet, found null" );
        }
    }
}
