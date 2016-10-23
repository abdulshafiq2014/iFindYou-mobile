package com.example.hp.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button volBtn;
    Button caregiverBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        caregiverBtn = (Button)findViewById(R.id.caregiver_btn);
        volBtn = (Button)findViewById(R.id.volunteer_btn);

        caregiverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ViewCaregiverActivity.class);
                Toast.makeText(getApplicationContext(), "Setting up your Caregiver Page", Toast.LENGTH_SHORT).show();
                startActivity(i);

            }
        });

        volBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ViewVolunteerActivity.class);
                Toast.makeText(getApplicationContext(), "Setting up your Volunteer Page", Toast.LENGTH_SHORT).show();
                startActivity(i);

            }
        });

    }


}
