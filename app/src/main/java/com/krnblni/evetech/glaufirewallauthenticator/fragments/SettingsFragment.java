package com.krnblni.evetech.glaufirewallauthenticator.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsFragment extends Fragment {

    TextView settingsNameTextView, settingsProfile1TextView, settingsProfile2TextView, settingsProfile3TextView, settingsVersionTextView;
    LinearLayout settingsNameLinearLayout, settingsProfile1LinearLayout, settingsProfile2LinearLayout, settingsProfile3LinearLayout;
    Context context;

    SharedPreferences sharedPreferences;

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

        settingsProfile2LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfileViaDialog(v);
            }
        });

        settingsProfile3LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfileViaDialog(v);
            }
        });

        return view;
    }

    private void editProfileViaDialog(View v) {

        int profileNumber = 0;

        if (v.getId() == R.id.settingsProfile1LinearLayout) {
            profileNumber = 1;
        } else if (v.getId() == R.id.settingsProfile2LinearLayout) {
            profileNumber = 2;
        } else if (v.getId() == R.id.settingsProfile3LinearLayout) {
            profileNumber = 3;
        }

        final String userName1, userName2, userName3;

        userName1 = sharedPreferences.getString("username1", "null");
        userName2 = sharedPreferences.getString("username2", "null");
        userName3 = sharedPreferences.getString("username3", "null");


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

                            if (
                                    (finalProfileNumber == 1
                                            && (TextUtils.equals(alertDialogUserNameEditText.getText().toString().toLowerCase(), userName2)
                                            || TextUtils.equals(alertDialogUserNameEditText.getText().toString().toLowerCase(), userName3)))

                                            || (finalProfileNumber == 2
                                            && (TextUtils.equals(alertDialogUserNameEditText.getText().toString().toLowerCase(), userName1)
                                            || TextUtils.equals(alertDialogUserNameEditText.getText().toString().toLowerCase(), userName3)))

                                            || (finalProfileNumber == 3
                                            && (TextUtils.equals(alertDialogUserNameEditText.getText().toString().toLowerCase(), userName1)
                                            || TextUtils.equals(alertDialogUserNameEditText.getText().toString().toLowerCase(), userName2)))

                                    ) {

                                alertDialogTextInputLayoutUserName.setError("Same profiles are not allowed");

                                Toast.makeText(context, "Same profiles are not allowed", Toast.LENGTH_SHORT).show();

                            } else {
                                alertDialogTextInputLayoutUserName.setErrorEnabled(false);
                                userName = true;
                            }
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

                    }
                }).create();

        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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

        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        alertDialog.show();
    }

    private void setValues() {

        settingsNameTextView.setText(sharedPreferences.getString("name", "null"));
        settingsProfile1TextView.setText(sharedPreferences.getString("username1", "null"));
        settingsProfile2TextView.setText(sharedPreferences.getString("username2", "null"));
        settingsProfile3TextView.setText(sharedPreferences.getString("username3", "null"));


        settingsVersionTextView.setText("Version: " + BuildConfig.VERSION_NAME);
    }

    private void findIds(View view) {

        settingsNameTextView = view.findViewById(R.id.settingsNameTextView);
        settingsProfile1TextView = view.findViewById(R.id.settingsProfile1TextView);
        settingsProfile2TextView = view.findViewById(R.id.settingsProfile2TextView);
        settingsProfile3TextView = view.findViewById(R.id.settingsProfile3TextView);

        settingsVersionTextView = view.findViewById(R.id.settingsVersionTextView);

        settingsNameLinearLayout = view.findViewById(R.id.settingsNameLinearLayout);
        settingsProfile1LinearLayout = view.findViewById(R.id.settingsProfile1LinearLayout);
        settingsProfile2LinearLayout = view.findViewById(R.id.settingsProfile2LinearLayout);
        settingsProfile3LinearLayout = view.findViewById(R.id.settingsProfile3LinearLayout);


    }
}
