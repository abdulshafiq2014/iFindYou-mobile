package com.example.hp.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.example.hp.myapplication.Caregiver.ViewCaregiverActivity;
import com.example.hp.myapplication.Volunteer.ViewDetails;
import com.example.hp.myapplication.Volunteer.ViewVolunteerActivity;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.fabric.sdk.android.Fabric;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String kay = "SRDXkLufvPkWOgnbrNqISmWwb";
    private static final String sct = "NBl7aVRjTYOT0Q9iu6b6QktAmoKlz3sN7f1mdUrBvTgPFuAiS4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        // digits by twitter
        TwitterAuthConfig authConfig = new TwitterAuthConfig(kay, sct);
        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());
        // fast android networking
        AndroidNetworking.initialize(getApplicationContext());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        DigitsAuthButton digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);

        //buttons if login has isseus -> development purposes only
        /*Button volBtn = (Button)findViewById(R.id.volunteer);
        volBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ViewVolunteerActivity.class);
                startActivity(i);
                finish();
            }
        });

        Button caretkrBtn = (Button)findViewById(R.id.caretaker);
        caretkrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ViewCaregiverActivity.class);
                startActivity(i);
                finish();
            }
        }); */

        //continue for digit log in/authentication
        digitsButton.setCallback(new AuthCallback() {
            @Override
            public void success(final DigitsSession session, final String phoneNumber) {

                final JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("user_uuid", String.valueOf(session.getId()));
                    jsonObject.put("phone_number", phoneNumber);
                    Log.d("check","phonenumber is : " + phoneNumber);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), "Logging in!", Toast.LENGTH_LONG).show();

                AndroidNetworking.post("https://tw9fnomwqe.execute-api.ap-southeast-1.amazonaws.com/dev/users")
                        .addJSONObjectBody(jsonObject)
                        .setTag("FAN")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsOkHttpResponse(new OkHttpResponseListener() {
                            @Override
                            public void onResponse(Response response) {
                                try{
                                    String TAG = "FAN";
                                    String jsonData = response.body().source().readUtf8();
                                    JSONObject jsonObject = new JSONObject(jsonData);
                                    Log.d(TAG, "jsonObj: " + jsonObject);
                                    String userName = jsonObject.getString("userName");
                                    String userType = jsonObject.getString("userType");
                                    // do anything with response
                                    if (response.isSuccessful()) {
                                        try {
                                            Log.d(TAG, "response : " + response.body().source().readUtf8());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        //Toast.makeText(getApplicationContext(), "Authentication successful for " + session.getId() + " token: " +
                                        //        session.getAuthToken() + ", phone: " + phoneNumber, Toast.LENGTH_LONG).show();
                                        Toast.makeText(getApplicationContext(), "(" + userType + ") Logging in as " + userName, Toast.LENGTH_SHORT).show();
                                        /*if(userType.equals("caretaker")){
                                            Intent i = new Intent(MainActivity.this, ViewCaregiverActivity.class);
                                            startActivity(i);
                                            finish();
                                        } else {*/
                                            Intent i = new Intent(MainActivity.this, ViewVolunteerActivity.class);
                                            startActivity(i);
                                            finish();
                                        //}

                                    }
                                } catch (Exception e){
                                    e.printStackTrace();
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
                Log.d("check","Failure");
                Toast.makeText(getApplicationContext(), "Unfortunately we couldn't log you in!", Toast.LENGTH_LONG).show();
            }
        });
    }


}
