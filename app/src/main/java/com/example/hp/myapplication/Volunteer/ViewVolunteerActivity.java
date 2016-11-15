package com.example.hp.myapplication.Volunteer;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;

import java.util.List;
import java.util.UUID;

public class ViewVolunteerActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    Button getBtn;
    EditText enterPID;
    private BeaconManager beaconManager;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    protected GoogleApiClient mGoogleApiClient;
    public LocationRequest mLocationRequest;
    private Context activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_view_volunteer);

        //init map fragment
        //mapFragment = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map));
        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));

        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {

                    mMap = googleMap;
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(1.290270, 103.851959), 14));

                    // Initialize Google Play Svcs
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            buildGoogleApiClient();
                            mMap.setMyLocationEnabled(true);
                        }
                    } else {
                        buildGoogleApiClient();
                        mMap.setMyLocationEnabled(true);
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
        }

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



//        enterPID = (EditText)findViewById(R.id.pid_text);
//        getBtn = (Button)findViewById(R.id.getpid_button);
//
//        getBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(ViewVolunteerActivity.this, ViewDetails.class);
//                Toast.makeText(getApplicationContext(), "Getting Missing Person Info...", Toast.LENGTH_SHORT).show();
//                startActivity(i);
//
//            }
//        });

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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        // Initialize Google Play Svcs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        Log.d("circle", "checking for correct behaviour");
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mMap.clear();

//
        // Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());



        //move camera to marker
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        CameraPosition cmp = new CameraPosition(latLng, 16, 0, 0);

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cmp));


        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //Ask user if need to explain
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) activity, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                //Prompt user once we show explanation
                ActivityCompat.requestPermissions((Activity) activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);

            } else {
                //No need to explain
                ActivityCompat.requestPermissions((Activity) activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                //If request is cancelled, the resulting arrays are empty
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Permission was granted
                    if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                    }
                    mMap.setMyLocationEnabled(true);
                } else {
                    //Permission denied, Disable the functionality that depends on this permission
                    Toast.makeText(activity, "Permission Denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }


}
