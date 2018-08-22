package com.krnblni.evetech.glaufirewallauthenticator.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.krnblni.evetech.glaufirewallauthenticator.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InitialSetupActivity extends AppCompatActivity {

    TextView initialSetupPageHeadingTextView;

    EditText initialSetupNameEditText, initialSetupUserName1EditText, initialSetupPassword1EditText,
            initialSetupUserName2EditText, initialSetupPassword2EditText, initialSetupUserName3EditText,
            initialSetupPassword3EditText;

    TextInputLayout initialSetupTextInputLayoutName, initialSetupTextInputLayoutUserName1, initialSetupTextInputLayoutUserName2,
            initialSetupTextInputLayoutUserName3, initialSetupTextInputLayoutPassword1, initialSetupTextInputLayoutPassword2,
            initialSetupTextInputLayoutPassword3;

    Button initialSetupDoneButton;

    String userName1, userName2, userName3, password1, password2, password3, name;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_setup);

        SharedPreferences sharedPreferences = getSharedPreferences("initial_setup", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        findIds();

        initialSetupDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    editor.putString("name", name);
                    editor.putString("username1", userName1);
                    editor.putString("password1", password1);
                    editor.putString("username2", userName2);
                    editor.putString("password2", password2);
                    editor.putString("username3", userName3);
                    editor.putString("password3", password3);
                    editor.putBoolean("initial_setup", true);
                    editor.apply();

                    Intent intent = new Intent(InitialSetupActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private boolean validateInputs() {

        boolean nameBoolean = false;
        boolean username1Boolean = false;
        boolean password1Boolean = false;
        boolean username2Boolean = false;
        boolean password2Boolean = false;
        boolean username3Boolean = false;
        boolean password3Boolean = false;

        Pattern namePattern = Pattern.compile("^[a-zA-Z ]+$");
        Matcher nameMatcher = namePattern.matcher(initialSetupNameEditText.getText().toString());
        if (nameMatcher.matches()) {
            nameBoolean = true;
            name = initialSetupNameEditText.getText().toString();
            initialSetupTextInputLayoutName.setErrorEnabled(false);
        } else {
            initialSetupTextInputLayoutName.setError("Invalid Name");
        }

        if (!TextUtils.isEmpty(initialSetupUserName1EditText.getText().toString().toLowerCase())) {
            if (TextUtils.equals(initialSetupUserName1EditText.getText().toString().toLowerCase(),
                    initialSetupUserName2EditText.getText().toString().toLowerCase())
                    || TextUtils.equals(initialSetupUserName1EditText.getText(), initialSetupUserName3EditText.getText())) {
                initialSetupTextInputLayoutUserName1.setError("Same profiles are not allowed");
            } else {
                username1Boolean = true;
                userName1 = initialSetupUserName1EditText.getText().toString().toLowerCase();
                initialSetupTextInputLayoutUserName1.setErrorEnabled(false);
            }
        } else {
            initialSetupTextInputLayoutUserName1.setError("This cannot be empty");
        }

        if (!TextUtils.isEmpty(initialSetupUserName2EditText.getText().toString().toLowerCase())) {
            if (TextUtils.equals(initialSetupUserName2EditText.getText().toString().toLowerCase(),
                    initialSetupUserName1EditText.getText().toString().toLowerCase())
                    || TextUtils.equals(initialSetupUserName2EditText.getText(), initialSetupUserName3EditText.getText())) {
                initialSetupTextInputLayoutUserName2.setError("Same profiles are not allowed");
            } else {
                username2Boolean = true;
                userName2 = initialSetupUserName2EditText.getText().toString().toLowerCase();
                initialSetupTextInputLayoutUserName2.setErrorEnabled(false);
            }
        } else {
            initialSetupTextInputLayoutUserName2.setError("This cannot be empty");
        }

        if (!TextUtils.isEmpty(initialSetupUserName3EditText.getText().toString().toLowerCase())) {
            if (TextUtils.equals(initialSetupUserName3EditText.getText().toString().toLowerCase(),
                    initialSetupUserName1EditText.getText().toString().toLowerCase())
                    || TextUtils.equals(initialSetupUserName3EditText.getText(), initialSetupUserName2EditText.getText())) {
                initialSetupTextInputLayoutUserName3.setError("Same profiles are not allowed");
            } else {
                username3Boolean = true;
                userName3 = initialSetupUserName3EditText.getText().toString().toLowerCase();
                initialSetupTextInputLayoutUserName3.setErrorEnabled(false);
            }
        } else {
            initialSetupTextInputLayoutUserName3.setError("This cannot be empty");
        }

        if (!TextUtils.isEmpty(initialSetupPassword1EditText.getText())) {
            password1Boolean = true;
            password1 = initialSetupPassword1EditText.getText().toString();
            initialSetupTextInputLayoutPassword1.setErrorEnabled(false);
        } else {
            initialSetupTextInputLayoutPassword1.setError("This cannot be empty");
        }

        if (!TextUtils.isEmpty(initialSetupPassword2EditText.getText())) {
            password2Boolean = true;
            password2 = initialSetupPassword2EditText.getText().toString();
            initialSetupTextInputLayoutPassword2.setErrorEnabled(false);
        } else {
            initialSetupTextInputLayoutPassword2.setError("This cannot be empty");
        }

        if (!TextUtils.isEmpty(initialSetupPassword3EditText.getText())) {
            password3Boolean = true;
            password3 = initialSetupPassword3EditText.getText().toString();
            initialSetupTextInputLayoutPassword3.setErrorEnabled(false);
        } else {
            initialSetupTextInputLayoutPassword3.setError("This cannot be empty");
        }


        return nameBoolean &&
                username1Boolean &&
                password1Boolean &&
                username2Boolean &&
                password2Boolean &&
                username3Boolean &&
                password3Boolean;

    }

    private void findIds() {

        initialSetupPageHeadingTextView = findViewById(R.id.initialSetupPageHeadingTextView);

        initialSetupNameEditText = findViewById(R.id.initialSetupNameEditText);

        initialSetupUserName1EditText = findViewById(R.id.initialSetupUserName1EditText);
        initialSetupUserName2EditText = findViewById(R.id.initialSetupUserName2EditText);
        initialSetupUserName3EditText = findViewById(R.id.initialSetupUserName3EditText);

        initialSetupPassword1EditText = findViewById(R.id.initialSetupPassword1EditText);
        initialSetupPassword2EditText = findViewById(R.id.initialSetupPassword2EditText);
        initialSetupPassword3EditText = findViewById(R.id.initialSetupPassword3EditText);

        initialSetupTextInputLayoutName = findViewById(R.id.initialSetupTextInputLayoutName);

        initialSetupTextInputLayoutUserName1 = findViewById(R.id.initialSetupTextInputLayoutUserName1);
        initialSetupTextInputLayoutUserName2 = findViewById(R.id.initialSetupTextInputLayoutUserName2);
        initialSetupTextInputLayoutUserName3 = findViewById(R.id.initialSetupTextInputLayoutUserName3);

        initialSetupTextInputLayoutPassword1 = findViewById(R.id.initialSetupTextInputLayoutPassword1);
        initialSetupTextInputLayoutPassword2 = findViewById(R.id.initialSetupTextInputLayoutPassword2);
        initialSetupTextInputLayoutPassword3 = findViewById(R.id.initialSetupTextInputLayoutPassword3);

        initialSetupDoneButton = findViewById(R.id.initialSetupDoneButton);
    }
}
