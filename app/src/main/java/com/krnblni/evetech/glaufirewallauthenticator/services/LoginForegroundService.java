package com.krnblni.evetech.glaufirewallauthenticator.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class LoginForegroundService extends Service {

    String TAG = "Logging - LoginForegroundService ";
    String loginUrl = "http://172.16.10.20:1000/login?011abaee7cf9a5fc";

    SharedPreferences sharedPreferences = getSharedPreferences("initial_setup", MODE_PRIVATE);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate: " + "called");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e(TAG, "onStartCommand: " + "service started " + startId);

        loginViaWebView();

        return START_STICKY;
    }

    private void loginViaWebView() {

        WebView webView = new WebView(getApplicationContext());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(loginUrl);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.e(TAG, "onReceivedError: " + "error occurred");
                Log.e(TAG, "onReceivedError: " + "not inside GLAU network");
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                String username = sharedPreferences.getString("username1", "null");
                String password = sharedPreferences.getString("password1", "null");
                view.loadUrl(
                        "javascript:(function() { " +
                                "var form = document.forms[0];"
                                + "var usr = form.elements[3];"
                                + "var pass = form.elements[4];"
                                + "var con = form.elements[5];"
                                + "usr.value = " + username + " ;"
                                + "pass.value = " + password + " ;"
                                + "con.click();" +
                                "})()"
                );

                view.setWebViewClient(new WebViewClient() {

                    @Override
                    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                        super.onReceivedError(view, request, error);
                        Log.e(TAG, "onReceivedError: " + "An error occurred while logging in");
                        view.destroy();
                        stopSelf();
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        Log.e(TAG, "onPageFinished: " + "login done");
                        view.destroy();
                        stopSelf();
                    }
                });
            }

        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: " + "called");
    }
}
