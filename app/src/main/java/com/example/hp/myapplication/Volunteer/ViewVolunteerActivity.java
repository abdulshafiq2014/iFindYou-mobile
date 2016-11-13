package com.example.hp.myapplication.Volunteer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.example.hp.myapplication.R;

import java.util.List;
import java.util.UUID;

public class ViewVolunteerActivity extends AppCompatActivity {

    Button getBtn;
    EditText enterPID;
    private BeaconManager beaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("volact","it's beginningggggggg");

        // create a new estimote beacon manager
        beaconManager = new BeaconManager(getApplicationContext());
        // display notification if entering region
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> beacons) {
                for(Beacon beacon : beacons){
                    Log.w("volact","Siao liao. Got beacon liao");
                    showNotification(
                            "Found a beacon!",
                            "BeaconID: " + beacon.getProximityUUID());

                }
            }
            @Override
            public void onExitedRegion(Region region) {
                // could add an "exit" notification too if you want (-:
            }
        });
        //connect to beacon if available
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new Region(
                        "allBeacons",
                        null, null, null));
            }
        });

        //check for permissions required by estimote
        SystemRequirementsChecker.checkWithDefaultDialogs(this);

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

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, ViewVolunteerActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }


}
