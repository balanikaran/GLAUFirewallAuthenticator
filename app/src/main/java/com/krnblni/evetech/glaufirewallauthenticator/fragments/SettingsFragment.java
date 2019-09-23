package com.krnblni.evetech.glaufirewallauthenticator.fragments;


import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krnblni.evetech.glaufirewallauthenticator.BuildConfig;
import com.krnblni.evetech.glaufirewallauthenticator.R;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsFragment extends Fragment {

    private TextView settingsNameTextView, settingsProfile1TextView, settingsVersionTextView;
    private LinearLayout settingsNameLinearLayout, settingsProfile1LinearLayout,
            settingsHavingProblemsLinearLayout, settingsRateOnGooglePlayLinearLayout, settingsViewPrivacyPolicyLinearLayout,
            settingsGithubRepoLinkLinearLayout;
    private Context context;
    private SharedPreferences sharedPreferences;

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        sharedPreferences = context.getSharedPreferences("initial_setup", Context.MODE_PRIVATE);

        findIds(view);
        setValues();

        settingsNameLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNameViaDialog();
            }
        });

        settingsProfile1LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfileViaDialog(v);
            }
        });

        settingsHavingProblemsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                troubleShootingDialog();
            }
        });

        settingsRateOnGooglePlayLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMarketPageForApp();
            }
        });

        settingsViewPrivacyPolicyLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPrivacyPolicyWebPage();
            }
        });

        settingsGithubRepoLinkLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGithubRepository();
            }
        });

        return view;
    }

    private void openGithubRepository() {
        Uri uri = Uri.parse("https://github.com/krnblni/GLAUFirewallAuthenticator");
        Intent githubRepoIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(githubRepoIntent);
    }

    private void openPrivacyPolicyWebPage() {
        Uri uri = Uri.parse("https://www.freeprivacypolicy.com/privacy/view/7e565d8445e144f227709d97a645fee6");
        Intent privacyPolicyIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(privacyPolicyIntent);
    }

    private void openMarketPageForApp() {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent marketIntent = new Intent(Intent.ACTION_VIEW, uri);
        marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(marketIntent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    private void troubleShootingDialog() {
        TextView searchForDeviceTextView;
        View dialogView = LayoutInflater.from(context).inflate(R.layout.alert_dialog_this_might_help, null, false);
        searchForDeviceTextView = dialogView.findViewById(R.id.searchForDeviceTextView);
        searchForDeviceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String manufacturer = Build.MANUFACTURER;
                String model = Build.MODEL;
                Intent searchIntent = new Intent(Intent.ACTION_WEB_SEARCH);
                searchIntent.putExtra(SearchManager.QUERY, "How to turn off battery optimization on " + manufacturer + " " + model);
                startActivity(searchIntent);
            }
        });
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("This might help you!")
                .setCancelable(true)
                .setView(dialogView)
                .create();
        alertDialog.show();
    }

    private void editProfileViaDialog(View v) {
        int profileNumber = 0;

        if (v.getId() == R.id.settingsProfile1LinearLayout) {
            profileNumber = 1;
        }

        View dialogView = LayoutInflater.from(context).inflate(R.layout.alert_dialog_edit_profile, null, false);

        final EditText alertDialogUserNameEditText = dialogView.findViewById(R.id.alertDialogUserNameEditText);
        alertDialogUserNameEditText.setText(sharedPreferences.getString("username" + profileNumber, "null"));
        alertDialogUserNameEditText.setSelection(alertDialogUserNameEditText.getText().length());

        final EditText alertDialogPasswordEditText = dialogView.findViewById(R.id.alertDialogPasswordEditText);
        alertDialogPasswordEditText.setText(sharedPreferences.getString("password" + profileNumber, "null"));
        alertDialogPasswordEditText.setSelection(alertDialogPasswordEditText.getText().length());

        final TextInputLayout alertDialogTextInputLayoutUserName = dialogView.findViewById(R.id.alertDialogTextInputLayoutUserName);
        final TextInputLayout alertDialogTextInputLayoutPassword = dialogView.findViewById(R.id.alertDialogTextInputLayoutPassword);

        final int finalProfileNumber = profileNumber;
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("Edit Profile")
                .setCancelable(false)
                .setView(dialogView)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        boolean userName = false;
                        boolean password = false;

                        if (!TextUtils.isEmpty(alertDialogUserNameEditText.getText().toString().toLowerCase())) {
                            alertDialogTextInputLayoutUserName.setErrorEnabled(false);
                            userName = true;
                        } else {
                            alertDialogTextInputLayoutUserName.setError("This cannot be empty");
                            Toast.makeText(context, "Invalid Username", Toast.LENGTH_SHORT).show();
                        }


                        if (!TextUtils.isEmpty(alertDialogPasswordEditText.getText())) {
                            password = true;
                            alertDialogTextInputLayoutPassword.setErrorEnabled(false);
                        } else {
                            alertDialogTextInputLayoutPassword.setError("This cannot be empty");
                            Toast.makeText(context, "Invalid Password", Toast.LENGTH_SHORT).show();
                        }

                        if (userName && password) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username" + finalProfileNumber, alertDialogUserNameEditText.getText().toString().toLowerCase());
                            editor.putString("password" + finalProfileNumber, alertDialogPasswordEditText.getText().toString().toLowerCase());
                            editor.apply();
                            Toast.makeText(context, "Profile Updated", Toast.LENGTH_SHORT).show();
                            setValues();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Operation Cancelled!", Toast.LENGTH_SHORT).show();
                    }
                }).create();

        Objects.requireNonNull(alertDialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alertDialog.show();

    }

    private void editNameViaDialog() {

        View dialogView = LayoutInflater.from(context).inflate(R.layout.alert_dialog_edit_name, null, true);

        final EditText alertDialogNameEditText = dialogView.findViewById(R.id.alertDialogNameEditText);
        alertDialogNameEditText.setText(sharedPreferences.getString("name", "null"));
        alertDialogNameEditText.setSelection(alertDialogNameEditText.getText().length());

        final TextInputLayout alertDialogTextInputLayoutName = dialogView.findViewById(R.id.alertDialogTextInputLayoutName);

        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("Edit Name")
                .setCancelable(false)
                .setView(dialogView)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Pattern namePattern = Pattern.compile("^[a-zA-Z ]+$");
                        Matcher nameMatcher = namePattern.matcher(alertDialogNameEditText.getText().toString());
                        if (nameMatcher.matches()) {
                            alertDialogTextInputLayoutName.setErrorEnabled(false);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("name", alertDialogNameEditText.getText().toString());
                            editor.apply();
                            Toast.makeText(context, "Name updated!", Toast.LENGTH_SHORT).show();
                            setValues();
                        } else {
                            alertDialogTextInputLayoutName.setError("Invalid Name");
                            Toast.makeText(context, "Invalid Name Input", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();

        Objects.requireNonNull(alertDialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        alertDialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void setValues() {

        settingsNameTextView.setText(sharedPreferences.getString("name", "null"));
        settingsProfile1TextView.setText(sharedPreferences.getString("username1", "null"));

        settingsVersionTextView.setText("Version: " + BuildConfig.VERSION_NAME);
    }

    private void findIds(View view) {

        settingsNameTextView = view.findViewById(R.id.settingsNameTextView);
        settingsProfile1TextView = view.findViewById(R.id.settingsProfile1TextView);

        settingsVersionTextView = view.findViewById(R.id.settingsVersionTextView);

        settingsNameLinearLayout = view.findViewById(R.id.settingsNameLinearLayout);
        settingsProfile1LinearLayout = view.findViewById(R.id.settingsProfile1LinearLayout);

        settingsHavingProblemsLinearLayout = view.findViewById(R.id.settingsHavingProblemsLinearLayout);
        settingsRateOnGooglePlayLinearLayout = view.findViewById(R.id.settingsRateOnGooglePlayLinearLayout);
        settingsViewPrivacyPolicyLinearLayout = view.findViewById(R.id.settingsViewPrivacyPolicyLinearLayout);

        settingsGithubRepoLinkLinearLayout = view.findViewById(R.id.settingsGithubRepoLinkLinearLayout);
    }
}
