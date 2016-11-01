package com.example.hp.myapplication.Volunteer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hp.myapplication.R;

public class ViewVolunteerActivity extends AppCompatActivity {

    Button getBtn;
    EditText enterPID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_view_volunteer);

        enterPID = (EditText)findViewById(R.id.pid_text);
        getBtn = (Button)findViewById(R.id.getpid_button);

        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewVolunteerActivity.this, ViewDetails.class);
                Toast.makeText(getApplicationContext(), "Getting Missing Person Info...", Toast.LENGTH_SHORT).show();
                startActivity(i);

            }
        });

    }


}
