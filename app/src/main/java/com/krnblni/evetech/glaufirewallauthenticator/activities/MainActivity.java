package com.krnblni.evetech.glaufirewallauthenticator.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    boolean inflateRespectedFragmentPage(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

        return true;
    }
}
