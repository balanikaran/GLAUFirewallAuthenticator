package com.krnblni.evetech.glaufirewallauthenticator.activities;

import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.krnblni.evetech.glaufirewallauthenticator.R;
import com.krnblni.evetech.glaufirewallauthenticator.fragments.AboutFragment;
import com.krnblni.evetech.glaufirewallauthenticator.fragments.DashboardFragment;
import com.krnblni.evetech.glaufirewallauthenticator.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    final String TAG = "Logging - MainActivity";

    FrameLayout navigationPageContainer;
    Toolbar toolbar;
    TextView pageHeadingTextView;
    AdView bannerAdView;

    AboutFragment aboutFragment = new AboutFragment();
    DashboardFragment dashboardFragment = new DashboardFragment();
    SettingsFragment settingsFragment = new SettingsFragment();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_about:
                    pageHeadingTextView.setText(R.string.about);
                    return inflateRespectedFragmentPage(aboutFragment);
                case R.id.navigation_dashboard:
                    pageHeadingTextView.setText(R.string.dashboard);
                    return inflateRespectedFragmentPage(dashboardFragment);
                case R.id.navigation_settings:
                    pageHeadingTextView.setText(R.string.settings);
                    return inflateRespectedFragmentPage(settingsFragment);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeMobileAdsLoadAndShow();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pageHeadingTextView = findViewById(R.id.pageHeadingTextView);
        navigationPageContainer = findViewById(R.id.container);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.getMenu().getItem(1).setChecked(true);
        pageHeadingTextView.setText(R.string.dashboard);
        inflateRespectedFragmentPage(dashboardFragment);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void initializeMobileAdsLoadAndShow() {
        Log.e(TAG, "initializeMobileAdsLoadAndShow: " + "the ad was loaded." );
        MobileAds.initialize(this, "ca-app-pub-2369194065944897~3644623004");
        bannerAdView = findViewById(R.id.bannerAdView);
        AdRequest bannerAdRequest = new AdRequest.Builder().build();
        bannerAdView.loadAd(bannerAdRequest);
        Log.e(TAG, "initializeMobileAdsLoadAndShow: " + "the ad was loaded" );
    }

    boolean inflateRespectedFragmentPage(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

        return true;
    }
}
