package com.krnblni.evetech.glaufirewallauthenticator.helpers;

import android.util.Log;

import com.google.android.gms.ads.InterstitialAd;

public class InterstitialAdManager {

    private static String TAG = "Logging - InterstitialAdManager";

    private InterstitialAd interstitialAd;

    // Singleton Class support
    private static InterstitialAdManager interstitialAdManagerSingleton;

    private InterstitialAdManager() {
    }

    public static InterstitialAdManager getInstance() {
        if (interstitialAdManagerSingleton == null) {
            Log.e(TAG, "getInstance: " + "instance is null..." );
            interstitialAdManagerSingleton = new InterstitialAdManager();
        }
        return interstitialAdManagerSingleton;
    }

    // Normal class support
    public InterstitialAd getInterstitialAd() {
        return this.interstitialAd;
    }

    public void setInterstitialAd(InterstitialAd interstitialAd) {
        this.interstitialAd = interstitialAd;
    }

}
