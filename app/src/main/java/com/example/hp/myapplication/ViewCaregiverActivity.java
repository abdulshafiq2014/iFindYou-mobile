package com.example.hp.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ViewCaregiverActivity extends AppCompatActivity {

    Button reportBtn;
    Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_view_caregiver);

        reportBtn = (Button)findViewById(R.id.report_missing);
        updateBtn = (Button)findViewById(R.id.update);

        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ViewCaregiverActivity.this)
                        .setTitle("Alert!")
                        .setMessage("Are you sure you want to report Missing?")
                        .setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }})
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

    }


}
