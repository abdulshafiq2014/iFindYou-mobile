package com.example.hp.myapplication.Caregiver;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.estimote.sdk.internal.utils.L;
import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.Gson;
import com.example.hp.myapplication.R;
import com.example.hp.myapplication.UtilHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ViewCaregiverActivity extends AppCompatActivity {

    ToggleButton reportBtn;
    Button updateBtn;
    String err;
    private String bID;
    String uuid;

    int missing;
    String caretaker, name, contact_number,details, beacon_id;

    TextView is_missing,pid_name, last_seen_text, description_box, contact_no_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_view_caregiver);


        //initialize UI components
        reportBtn = (ToggleButton)findViewById(R.id.report_missing);
        updateBtn = (Button)findViewById(R.id.update);
        is_missing = (TextView)findViewById(R.id.is_missing);
        pid_name = (TextView) findViewById(R.id.pid_name);
        last_seen_text = (TextView) findViewById(R.id.last_seen_text);
        description_box = (TextView) findViewById(R.id.description_box);
        contact_no_details = (TextView) findViewById(R.id.contact_no_details);

        Intent intent = getIntent();
        uuid = intent.getStringExtra("uuid");
        String source = intent.getStringExtra("Source");
        if (source!=null){
            if (source.equals("UpdateAtRiskInformation")){
                new getInformation().execute();

            } else if (source.equals("RaiseAlert")){
                new getInformation().execute();
                //is_missing.setText("Missing : Yes");
            }
        } else {
            new getInformation().execute();
        }






//        reportBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new AlertDialog.Builder(ViewCaregiverActivity.this)
//                        .setTitle("Alert!")
//                        .setMessage("Are you sure you want to report Missing?")
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                new raiseAlert().execute();
//                                //
//                            }
//                        })
//                        .setNegativeButton(android.R.string.no, null).show();
//            }
//        });


        reportBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("check", "ifcheck is : " + isChecked);
                if (isChecked) {
                    // The toggle is enabled
                    //reportBtn.setTextOn("Report Found");
                    //reportBtn.setBackgroundResource(R.color.green);
                    new raiseAlert().execute();
                } else {
                    // The toggle is disabled
                    //reportBtn.setTextOff("Report Missing");
                    //reportBtn.setBackgroundResource(R.color.darkred);
                    new raiseAlert().execute();
                }
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewCaregiverActivity.this, UpdateAtRiskInformation.class);
                i.putExtra("uuid",uuid);
                i.putExtra("bID",bID);
                Toast.makeText(getApplicationContext(), "Going to edit information page...", Toast.LENGTH_SHORT).show();
                startActivity(i);
                finish();
            }
        });

    }

    private class raiseAlert extends AsyncTask<Object, Object, Boolean> {
        ProgressDialog pdLoading = new ProgressDialog(ViewCaregiverActivity.this);
        boolean success = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("Updating information...");
            pdLoading.show();
        }
        @Override
        protected Boolean doInBackground(Object... params) {

            //form JSON object to post
            JSONObject jsoin = null;

            try {

                jsoin = new JSONObject();
                jsoin.put("request", "toggleMissingStatus");

            } catch (JSONException e){
                e.printStackTrace();
                err = e.getMessage();
            }


//            bID = "797402778773489664";
            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            String url = "https://tw9fnomwqe.execute-api.ap-southeast-1.amazonaws.com/dev/beacons/" + bID;

            String rst = UtilHttp.doHttpPostJson(getApplication().getApplicationContext(),url,jsoin.toString());
            if (rst == null) {
                err = UtilHttp.err;
            } else {
                success = true;

            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            //this method will be running on UI thread
            if (result){
                pdLoading.dismiss();
                Toast.makeText(getApplicationContext(), "Information updated!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(),ViewCaregiverActivity.class);

                i.putExtra("Source", "RaiseAlert");
                i.putExtra("uuid",uuid);

                startActivity(i);
                finish();
            } else {
                Toast.makeText(getBaseContext(), err, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class getInformation extends AsyncTask<Object, Object, Boolean> {
        ProgressDialog pdLoading = new ProgressDialog(ViewCaregiverActivity.this);
        boolean success = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("Updating information...");
            pdLoading.show();
        }
        @Override
        protected Boolean doInBackground(Object... params) {
            //form JSON object to post
            JSONObject jsoin = null;

            try {

                jsoin = new JSONObject();
                jsoin.put("caretaker_uuid", uuid);


            } catch (JSONException e){
                e.printStackTrace();
                err = e.getMessage();
            }


            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            String url = "https://tw9fnomwqe.execute-api.ap-southeast-1.amazonaws.com/dev/beacons/";

            String rst = UtilHttp.doHttpPostJson(getApplication().getApplicationContext(),url,jsoin.toString());
            if (rst == null) {
                err = UtilHttp.err;
            } else {

                Log.d("check",rst.toString());

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(rst);

                    missing = jsonObject.getInt("missing");
                    caretaker = jsonObject.getString("caretaker");
                    name = jsonObject.getString("name");
                    contact_number = jsonObject.getString("contact_number");
                    details = jsonObject.getString("details");
                    beacon_id = jsonObject.getString("beacon_id");



                    success = true;

                } catch (JSONException e){
                    e.printStackTrace();
                    err = e.toString();
                    Log.d("check","here");
                    success = false;
                }

            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            //this method will be running on UI thread
            if (result){
                pdLoading.dismiss();

                if (missing == -1){
                    is_missing.setText("Missing: No");
                } else {
                    is_missing.setText("Missing: Yes");
                }

                Log.d("check","missing value is : " + missing);
                //need to rename toggle button
                if (missing == -1){
                    reportBtn.setBackgroundResource(R.color.darkred);
                    reportBtn.setText("Report Missing");

                } else {
                    Log.d("check","missing at this line");
                    reportBtn.setBackgroundResource(R.color.green);
                    reportBtn.setText("Report Found");
                }

                pid_name.setText(name);
                last_seen_text.setText("Random");
                description_box.setText(details);
                contact_no_details.setText(contact_number);
                bID = beacon_id;


                Toast.makeText(getApplicationContext(), "Loaded information!", Toast.LENGTH_SHORT).show();




            } else {
                Toast.makeText(getBaseContext(), err, Toast.LENGTH_LONG).show();
            }

            pdLoading.dismiss();

        }

    }


}
