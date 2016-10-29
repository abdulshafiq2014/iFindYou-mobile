package com.example.hp.myapplication;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
    import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.google.gson.Gson;
import com.twitter.sdk.android.core.TwitterAuthConfig;
    import com.twitter.sdk.android.core.TwitterCore;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Logger;

import io.fabric.sdk.android.Fabric;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String kay = "SRDXkLufvPkWOgnbrNqISmWwb";
    private static final String sct = "NBl7aVRjTYOT0Q9iu6b6QktAmoKlz3sN7f1mdUrBvTgPFuAiS4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // digits by twitter
        TwitterAuthConfig authConfig = new TwitterAuthConfig(kay, sct);
        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());
        // fast android networking
        AndroidNetworking.initialize(getApplicationContext());
        setContentView(R.layout.content_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        DigitsAuthButton digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);
        digitsButton.setCallback(new AuthCallback() {
            @Override
            public void success(final DigitsSession session, final String phoneNumber) {

                final JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("user_uuid", String.valueOf(session.getId()));
                    jsonObject.put("phone_number", phoneNumber);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                AndroidNetworking.post("https://tw9fnomwqe.execute-api.ap-southeast-1.amazonaws.com/dev")
                        .addJSONObjectBody(jsonObject)
                        .setTag("FAN")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsOkHttpResponse(new OkHttpResponseListener() {
                            @Override
                            public void onResponse(Response response) {
                                String TAG = "FAN";
                                // do anything with response
                                if (response.isSuccessful()) {
                                    Log.d(TAG, "Headers :" + response.headers());
                                    try {
                                        Log.d(TAG, "response : " + response.body().source().readUtf8());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(getApplicationContext(), "Authentication successful for " + session.getId() + " token: " +
                                            session.getAuthToken() + ", phone: " + phoneNumber, Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onError(ANError error) {
                                String TAG = "FAN";
                                // handle error
                                if (error.getErrorCode() != 0) {
                                    // received error from server
                                    // error.getErrorCode() - the error code from server
                                    // error.getErrorBody() - the error body from server
                                    // error.getErrorDetail() - just an error detail
                                    Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                                    Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                                    Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                    Log.d(TAG, "JSON : " + jsonObject.toString());
                                } else {
                                    // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                    Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                                }
                            }
                        });

            }

            @Override
            public void failure(DigitsException error) {

            }
        });
    }


}
