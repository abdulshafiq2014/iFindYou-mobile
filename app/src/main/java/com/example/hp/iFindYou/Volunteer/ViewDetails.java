package com.example.hp.iFindYou.Volunteer;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hp.iFindYou.R;

import java.util.Date;

public class ViewDetails extends AppCompatActivity {

    Button callBtn;

    private String name, details, caretaker, contact_number;
    TextView pid_name,description_box, missing_since;
    private int unix_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        details = intent.getStringExtra("details");
        caretaker = intent.getStringExtra("caretaker");
        contact_number = intent.getStringExtra("contact_number");
        unix_time = intent.getIntExtra("unix_time",0);


        Date date = new Date ();
        date.setTime((long)unix_time*1000);
        Log.d("testing","missing since : " + date);
        Log.d("testing","unix_time is : " + unix_time);
        Log.d("testing","details is : " + details);


        callBtn = (Button)findViewById(R.id.phone_call);
        pid_name = (TextView)findViewById(R.id.pid_name);
        description_box = (TextView)findViewById(R.id.description_box);
        missing_since = (TextView)findViewById(R.id.missing_since);

        pid_name.setText(name);
        description_box.setText(details);
        missing_since.setText(date.toString());

        final int caregiverNo = 90252088;
        final String caregiverEmail = "littl3fiq@gmail.com";
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ViewDetails.this)
                        .setTitle("Alert!")
                        .setMessage("Call " + name + "'s caregiver?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent callIntent = new Intent(Intent.ACTION_DIAL );
                                //need to call from database to add in the caregiver number. because inner class can only have final variables
                                //call db here to get caregiverNo
                                callIntent.setData(Uri.parse("tel:" + contact_number));

                                if (ContextCompat.checkSelfPermission(getApplication().getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {


                                }

                                startActivity(callIntent);
                        }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

    }

}
