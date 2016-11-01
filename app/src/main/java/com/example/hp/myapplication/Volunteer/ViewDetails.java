package com.example.hp.myapplication.Volunteer;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.hp.myapplication.R;

public class ViewDetails extends AppCompatActivity {

    Button callBtn;
    Button emailBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);


        callBtn = (Button)findViewById(R.id.phone_call);
        emailBtn = (Button)findViewById(R.id.send_email);

        final int caregiverNo = 90252088;
        final String caregiverEmail = "littl3fiq@gmail.com";
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ViewDetails.this)
                        .setTitle("Alert!")
                        .setMessage("Call Tan Wei Liang?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent callIntent = new Intent(Intent.ACTION_DIAL );
                                //need to call from database to add in the caregiver number. because inner class can only have final variables
                                //call db here to get caregiverNo
                                callIntent.setData(Uri.parse("tel:" + caregiverNo));

                                if (ContextCompat.checkSelfPermission(getApplication().getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {


                                }

                                startActivity(callIntent);
                        }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ViewDetails.this)
                        .setTitle("Alert!")
                        .setMessage("Email Tan Wei Liang?")
                        .setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //call db here to get caregiverEmail
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto",caregiverEmail, null));//to change the email
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                        startActivity(Intent.createChooser(emailIntent, "Send email..."));
                    }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

    }

}
