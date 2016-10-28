package com.example.hp.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "0qvRvPrDzPb0fsmAfZvA2Y8Bt";
    private static final String TWITTER_SECRET = "8mjdsCuTzkgRPm2a9eTK71rkCOwzSD3SliubSOrOA9rieCd6bD";

    //Button volBtn;
    //Button caregiverBtn;
    private AuthCallback authCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());
        setContentView(R.layout.content_main);

        //to make toasts for debugging
        final Context context = getApplicationContext();
        final int duration = Toast.LENGTH_LONG;

        authCallback = new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                // Do something with the session
                CharSequence text = "Success! + phoneNumber";
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

            @Override
            public void failure(DigitsException exception) {
                // Do something on failure
                CharSequence text = "Fail!";
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        };

        DigitsAuthButton digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);
        digitsButton.setCallback(authCallback);
    }

    public AuthCallback getAuthCallback(){
        return authCallback;
    }

}
