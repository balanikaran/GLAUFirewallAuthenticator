package com.krnblni.evetech.glaufirewallauthenticator.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.krnblni.evetech.glaufirewallauthenticator.R;

public class LoginForegroundService extends Service {

    String TAG = "Logging - LoginForegroundService ";
    String loginUrl;
    int foregroundServiceID = 200;
    String notificationChannelIdForHelperService = "1000";

    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate: " + "called");
        Notification foregroundServiceNotification = new NotificationCompat.Builder(getApplicationContext(),
                notificationChannelIdForHelperService)
                .setSmallIcon(R.drawable.ic_stat_app_icon_notification)
                .setContentTitle("Login Service")
                .build();
        startForeground(foregroundServiceID, foregroundServiceNotification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: " + "service started " + startId);
        sharedPreferences = getSharedPreferences("initial_setup", MODE_PRIVATE);
        loginUrl = getString(R.string.login_url_glau);
        loginViaWebView();
        return START_STICKY;
    }

    @SuppressLint("SetJavaScriptEnabled")
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
                view.destroy();
                stopSelf();
                //updateNotification("Not inside GLAU Network");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                String username = sharedPreferences.getString("username1", "null");
                String password = sharedPreferences.getString("password1", "null");
//                view.loadUrl(
//                        getString(R.string.secret_1)
//                                + getString(R.string.secret_2)
//                                + getString(R.string.secret_3)
//                                + getString(R.string.secret_4)
//                                + getString(R.string.secret_5)
//                                + getString(R.string.secret_6)
//                                + username
//                                + getString(R.string.secret_7)
//                                + getString(R.string.secret_8)
//                                + password
//                                + getString(R.string.secret_9)
//                                + getString(R.string.secret_10)
//                                + getString(R.string.secret_11)
//                );

                view.loadUrl("javascript:( function() { var user = document.getElementById('ft_un'); user.value = '" +
                        username +
                        "'; var pwd = document.getElementById('ft_pd'); pwd.value = '" +
                        password +
                        "'; var inputs = document.getElementsByTagName('input'); for (var i = inputs.length - 1; i >= 0; i--) { if ( inputs[i].type == 'submit' ) { inputs[i].click(); } } } )()");

                view.setWebViewClient(new WebViewClient() {

                    @Override
                    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                        super.onReceivedError(view, request, error);
                        Log.e(TAG, "onReceivedError: " + "An error occurred while logging in");
                        //updateNotification("Login Error - (Concurrent/Unsuccessful)");
                        view.destroy();
                        stopSelf();
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        Log.e(TAG, "onPageFinished: " + "login done");
                        //updateNotification("Login in Successful");
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

//    public void updateNotification(String notificationMessage) {
//        Notification foregroundServiceNotification = new NotificationCompat.Builder(getApplicationContext(),
//                notificationChannelIdForHelperService)
//                .setSmallIcon(R.drawable.web_hi_res_512)
//                .setContentTitle(notificationMessage)
//                .build();
//        NotificationManagerCompat.from(getApplicationContext()).notify(foregroundServiceID, foregroundServiceNotification);
//    }
}
