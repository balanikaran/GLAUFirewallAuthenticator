package com.krnblni.evetech.glaufirewallauthenticator.fragments;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.krnblni.evetech.glaufirewallauthenticator.R;
import com.krnblni.evetech.glaufirewallauthenticator.services.HelperForegroundService;
import com.krnblni.evetech.glaufirewallauthenticator.services.LoginForegroundService;
import com.polyak.iconswitch.IconSwitch;

public class DashboardFragment extends Fragment {

    String TAG = "Logging - DashboardFragment ";

    IconSwitch serviceIconSwitch;
    TextView statusInfoActiveTextView, statusInfoInactiveTextView;

    Context context;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    FirebaseJobDispatcher firebaseJobDispatcher;

    Intent helperForegroundServiceIntent;
    Intent loginForegroundServiceIntent;

    @Override
    public void onAttach(Context context) {
        Log.e(TAG, "onAttach: " + "called");
        this.context = context;
        super.onAttach(context);

        helperForegroundServiceIntent = new Intent(context, HelperForegroundService.class);
        loginForegroundServiceIntent = new Intent(context, LoginForegroundService.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.e(TAG, "onCreateView: " + "called");

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        sharedPreferences = context.getSharedPreferences("initial_setup", Context.MODE_PRIVATE);

        firebaseJobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));

        serviceIconSwitch = view.findViewById(R.id.serviceIconSwitch);
        statusInfoActiveTextView = view.findViewById(R.id.statusInfoActiveTextView);
        statusInfoInactiveTextView = view.findViewById(R.id.statusInfoInactiveTextView);

        if (isHelperServiceRunning()) {
            editor = sharedPreferences.edit();
            editor.putBoolean("foregroundServiceStateUserPreference", true);
            editor.apply();
            statusInfoActiveTextView.setVisibility(View.VISIBLE);
            statusInfoInactiveTextView.setVisibility(View.INVISIBLE);
            serviceIconSwitch.setChecked(IconSwitch.Checked.RIGHT);
        } else {
            editor = sharedPreferences.edit();
            editor.putBoolean("foregroundServiceStateUserPreference", false);
            editor.apply();
            statusInfoActiveTextView.setVisibility(View.INVISIBLE);
            statusInfoInactiveTextView.setVisibility(View.VISIBLE);
            serviceIconSwitch.setChecked(IconSwitch.Checked.LEFT);
        }

        serviceIconSwitch.setCheckedChangeListener(new IconSwitch.CheckedChangeListener() {
            @Override
            public void onCheckChanged(IconSwitch.Checked current) {
                Log.e(TAG, "onCheckChanged: " + current);
                editor = sharedPreferences.edit();
                if (current == IconSwitch.Checked.RIGHT) {
                    editor.putBoolean("foregroundServiceStateUserPreference", true);
                    statusInfoActiveTextView.setVisibility(View.VISIBLE);
                    statusInfoInactiveTextView.setVisibility(View.INVISIBLE);
                    ContextCompat.startForegroundService(context, helperForegroundServiceIntent);
                } else if (current == IconSwitch.Checked.LEFT) {
                    editor.putBoolean("foregroundServiceStateUserPreference", false);
                    statusInfoActiveTextView.setVisibility(View.INVISIBLE);
                    statusInfoInactiveTextView.setVisibility(View.VISIBLE);
                    context.stopService(helperForegroundServiceIntent);
                    context.stopService(loginForegroundServiceIntent);
                    firebaseJobDispatcher.cancel("reInitiateLoginJobServiceTag");
                }
                editor.apply();
            }
        });
        return view;
    }

    private boolean isHelperServiceRunning() {

        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
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