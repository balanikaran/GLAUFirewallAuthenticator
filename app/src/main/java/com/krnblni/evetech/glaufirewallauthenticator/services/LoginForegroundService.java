package com.krnblni.evetech.glaufirewallauthenticator.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.krnblni.evetech.glaufirewallauthenticator.R;
import com.krnblni.evetech.glaufirewallauthenticator.activities.MainActivity;

public class LoginForegroundService extends Service {

    String TAG = "Logging - LoginForegroundService ";
    String loginUrl;
    int foregroundServiceID = 200, helperForegroundServiceID = 100;
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
                .setGroup("helperServiceGroup")
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
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                String username = sharedPreferences.getString("username1", "null");
                String password = sharedPreferences.getString("password1", "null");

                view.evaluateJavascript("(" +
                            "function(){ " +
                                "var user = document.getElementById('ft_un');" +
                                "user.value = '" + username + "';" +
                                "var pwd = document.getElementById('ft_pd');" +
                                "pwd.value = '" + password + "';" +
                                "var inputSubmit = document.querySelector(\"input[type='submit']\");" +
                                "if (inputSubmit) {" +
                                    "inputSubmit.click();" +
                                "}" +
                                "var inputButton = document.querySelector(\"button[type='submit']\");" +
                                "if (inputButton) {" +
                                    "inputButton.click();" +
                                "}" +
                                "return document.body.innerHTML;" +
                            "}" +
                        ")()", null);

                view.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        Log.e(TAG, "onPageFinished: " + "login done");
                        view.evaluateJavascript("(function(){ return document.body.innerHTML; })()", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String htmlDocumentString) {
                                if (htmlDocumentString != null) {
                                    if (htmlDocumentString.toLowerCase().contains("Keepalive".toLowerCase())) {
                                        Log.e(TAG, "onReceiveValue: Keep Alive");
                                        updateNotification("Login Successful! ✅");
                                    } else if (htmlDocumentString.toLowerCase().contains("over limit".toLowerCase())) {
                                        Log.e(TAG, "onReceiveValue: Over Limit");
                                        updateNotification("Over Limit! ⚠️");
                                    } else if (htmlDocumentString.toLowerCase().contains("Authentication Failed".toLowerCase())) {
                                        Log.e(TAG, "onReceiveValue: Authentication Failed");
                                        updateNotification("Login Failed! ❌");
                                    } else {
                                        Log.e(TAG, "onReceiveValue: Unknown");
                                    }
                                }
                            }
                        });
                        destroyWebViewAfterDelay(view);
                        stopSelf();
                    }
                });
            }

        });
    }

    private void destroyWebViewAfterDelay(final WebView view) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "run: " + "webView destroyed" );
                if (view != null){
                    view.destroy();
                }
            }
        }, 5000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: " + "called");
    }

    public void updateNotification(String notificationMessage) {
        Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent mainActivityPendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                300,
                mainActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        if (notificationMessage == null || notificationMessage.equals("")) {
            notificationMessage = "Unknown";
        }
        Notification foregroundServiceNotification = new NotificationCompat.Builder(getApplicationContext(),
                notificationChannelIdForHelperService)
                .setSmallIcon(R.drawable.ic_stat_app_icon_notification)
                .setContentTitle("Service is up and running 😉")
                .setContentText("Status: " + notificationMessage)
                .setContentIntent(mainActivityPendingIntent)
                .setGroup("helperServiceGroup")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .setBigContentTitle("Status: " + notificationMessage)
                        .bigText(getString(R.string.notification_info_text))
                ).build();
        NotificationManagerCompat.from(getApplicationContext()).notify(helperForegroundServiceID, foregroundServiceNotification);
    }
}
