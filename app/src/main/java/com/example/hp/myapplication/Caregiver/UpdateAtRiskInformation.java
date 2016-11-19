package com.example.hp.myapplication.Caregiver;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.hp.myapplication.R;
import com.example.hp.myapplication.UtilHttp;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateAtRiskInformation extends AppCompatActivity {
    Button updateBtn;
    private String bID;
    private String err;
    private final String request = "updateProfile";
    private String caretaker;
    private String contact_number;
    private String details;
    private int missing;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_at_risk_information);

        updateBtn = (Button) findViewById(R.id.cfm_update);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UpdateAtRiskInformation.this)
                        .setTitle("Alert!")
                        .setMessage("Are you sure you want to update with the following information?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    new updateInformation().execute();
                                }
                            })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

    }


    private class updateInformation extends AsyncTask<Object, Object, Boolean> {
        ProgressDialog pdLoading = new ProgressDialog(UpdateAtRiskInformation.this);
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
                jsoin.put("request", request);
                jsoin.put("caretaker", caretaker);
                jsoin.put("contact_number", contact_number);
                jsoin.put("details", details);
                jsoin.put("missing", missing);
                jsoin.put("name", name);

            } catch (JSONException e){
                e.printStackTrace();
                err = e.getMessage();
            }


            bID = "797402778773489664";
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
                startActivity(i);
                finish();
            } else {
                Toast.makeText(getBaseContext(), err, Toast.LENGTH_LONG).show();
            }



        }

    }
}


