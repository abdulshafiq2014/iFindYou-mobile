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
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.myapplication.R;
import com.example.hp.myapplication.UtilHttp;
import com.example.hp.myapplication.Volunteer.ViewVolunteerActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class ViewCaregiverActivity extends AppCompatActivity {

    Button reportBtn;
    Button updateBtn;
    Button backBtn;
    String err;
    private String bID;

    TextView is_missing,pid_name, last_seen_text, description_box, contact_no_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_view_caregiver);

        //initialize UI components
        reportBtn = (Button)findViewById(R.id.report_missing);
        updateBtn = (Button)findViewById(R.id.update);
        backBtn = (Button) findViewById(R.id.back_button);
        is_missing = (TextView)findViewById(R.id.is_missing);
        pid_name = (TextView) findViewById(R.id.pid_name);
        last_seen_text = (TextView) findViewById(R.id.last_seen_text);
        description_box = (TextView) findViewById(R.id.description_box);
        contact_no_details = (TextView) findViewById(R.id.contact_no_details);

        Intent intent = getIntent();
        String source = intent.getStringExtra("Source");
        if (source!=null){
            if (source.equals("UpdateAtRiskInformation")){
                String name = intent.getStringExtra("name");
                String caretaker = intent.getStringExtra("caretaker");
                String details = intent.getStringExtra("details");
                int missing = intent.getIntExtra("missing",0);

                if(missing == -1){
                    is_missing.setText("Missing : No");
                } else {
                    is_missing.setText("Missing : Yes");
                }

                pid_name.setText(name);
                description_box.setText(details);

            } else if (source.equals("RaiseAlert")){
                is_missing.setText("Missing : Yes");
            }
        }






        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ViewCaregiverActivity.this)
                        .setTitle("Alert!")
                        .setMessage("Are you sure you want to report Missing?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                new raiseAlert().execute();
                                //
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewCaregiverActivity.this, UpdateAtRiskInformation.class);
                Toast.makeText(getApplicationContext(), "Going to edit information page...", Toast.LENGTH_SHORT).show();
                startActivity(i);

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewCaregiverActivity.this, ViewVolunteerActivity.class);
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
//                name
//                caretaker
//                contact_number
//                details
//                missing
                i.putExtra("Source", "RaiseAlert");
                i.putExtra("missing",1);

                startActivity(i);
                finish();
            } else {
                Toast.makeText(getBaseContext(), err, Toast.LENGTH_LONG).show();
            }



        }

    }


}
