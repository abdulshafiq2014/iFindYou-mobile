package com.example.hp.myapplication.Volunteer;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.hp.myapplication.AlertDialogFragment;
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

        // create a new estimote beacon manager
        beaconManager = new BeaconManager(getApplicationContext());
        // display notification if entering region
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> beacons) {
                for(Beacon beacon : beacons){
                    Log.i("volact","Siao liao. Got beacon liao");
                    String beaconUuid = "" + beacon.getProximityUUID(); // TODO get missing person name from DB?
                    // assume that all beacons now are on the alert list
                    if(!true && false){ // TODO check if beacon UUID is NOT on the alert list && user allows interaction data to be recorded
                        Log.i("volact", "Beginning normal beacon interaction test:" + beaconUuid);
                        // TODO upload interaction data to server
                    } else if (true) { //TODO check if beacon UUID is on the alert list
                        Log.i("volact", "Beginning missing beacon interaction test: " + beaconUuid);
                        //showNotification( "Found a beacon!", "BeaconID: " + beaconUuid);
                        // TODO alert user to this situation
                        alertUser(beaconUuid);
                        // TODO turn on ranging for this beacon
                    }


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

    private void alertUser(String identifier) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                ViewVolunteerActivity.this);

        // Setting Dialog Title
                alertDialog.setTitle("Found a beacon");

        // Setting Dialog Message
                alertDialog.setMessage("Beacon identifier: " + identifier);

        // Setting Icon to Dialog
                //alertDialog.setIcon(R.drawable.);

        // Setting Positive "Yes" Btn
                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                Toast.makeText(getApplicationContext(),
                                        "You clicked on YES", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });

        // Setting Negative "NO" Btn
                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                Toast.makeText(getApplicationContext(),
                                        "You clicked on NO", Toast.LENGTH_SHORT)
                                        .show();
                                dialog.cancel();
                            }
                        });

        // Showing Alert Dialog
                alertDialog.show();

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
