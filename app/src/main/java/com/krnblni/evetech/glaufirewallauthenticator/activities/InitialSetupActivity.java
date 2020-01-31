package com.krnblni.evetech.glaufirewallauthenticator.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.krnblni.evetech.glaufirewallauthenticator.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InitialSetupActivity extends AppCompatActivity {

    TextView initialSetupPageHeadingTextView;
    EditText initialSetupNameEditText, initialSetupUserName1EditText, initialSetupPassword1EditText;
    TextInputLayout initialSetupTextInputLayoutName, initialSetupTextInputLayoutUserName1, initialSetupTextInputLayoutPassword1;
    Button initialSetupDoneButton;
    String userName1, password1, name;

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

        Pattern namePattern = Pattern.compile("^[a-zA-Z ]+$");
        Matcher nameMatcher = namePattern.matcher(initialSetupNameEditText.getText().toString());
        if (nameMatcher.matches()) {
            nameBoolean = true;
            name = initialSetupNameEditText.getText().toString();
            initialSetupTextInputLayoutName.setErrorEnabled(false);
        } else {
            initialSetupTextInputLayoutName.setError(getString(R.string.invalid_name));
        }

        if (!TextUtils.isEmpty(initialSetupUserName1EditText.getText().toString().toLowerCase())) {
            username1Boolean = true;
            userName1 = initialSetupUserName1EditText.getText().toString().toLowerCase();
            initialSetupTextInputLayoutUserName1.setErrorEnabled(false);
        } else {
            initialSetupTextInputLayoutUserName1.setError(getString(R.string.this_cannot_be_empty));
        }

        if (!TextUtils.isEmpty(initialSetupPassword1EditText.getText())) {
            password1Boolean = true;
            password1 = initialSetupPassword1EditText.getText().toString();
            initialSetupTextInputLayoutPassword1.setErrorEnabled(false);
        } else {
            initialSetupTextInputLayoutPassword1.setError(getString(R.string.this_cannot_be_empty));
        }

        return nameBoolean &&
                username1Boolean &&
                password1Boolean;
    }

    private void findIds() {
        initialSetupPageHeadingTextView = findViewById(R.id.initialSetupPageHeadingTextView);
        initialSetupNameEditText = findViewById(R.id.initialSetupNameEditText);
        initialSetupUserName1EditText = findViewById(R.id.initialSetupUserName1EditText);
        initialSetupPassword1EditText = findViewById(R.id.initialSetupPassword1EditText);
        initialSetupTextInputLayoutName = findViewById(R.id.initialSetupTextInputLayoutName);
        initialSetupTextInputLayoutUserName1 = findViewById(R.id.initialSetupTextInputLayoutUserName1);
        initialSetupTextInputLayoutPassword1 = findViewById(R.id.initialSetupTextInputLayoutPassword1);
        initialSetupDoneButton = findViewById(R.id.initialSetupDoneButton);
    }
}
