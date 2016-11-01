package com.example.hp.myapplication.Caregiver;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.hp.myapplication.R;

public class UpdateAtRiskInformation extends AppCompatActivity {
    Button updateBtn;
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
                        .setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

    }

}
