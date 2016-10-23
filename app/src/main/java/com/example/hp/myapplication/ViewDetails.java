package com.example.hp.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class ViewDetails extends AppCompatActivity {

    Button callBtn;
    Button emailBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);


        callBtn = (Button)findViewById(R.id.phone_call);
        emailBtn = (Button)findViewById(R.id.send_email);

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ViewDetails.this)
                        .setTitle("Alert!")
                        .setMessage("Call Tan Wei Liang?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
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
                    }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

    }

}
