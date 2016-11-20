package com.example.hp.myapplication.Volunteer;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.Utils;
import com.example.hp.myapplication.AlertDialogFragment;
import com.example.hp.myapplication.Caregiver.ViewCaregiverActivity;
import com.example.hp.myapplication.R;
import com.example.hp.myapplication.UtilHttp;
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
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ViewVolunteerActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        DetectedMissingBeaconDialogFragment.MissingBeaconDialogListener {

    Button getBtn;
    EditText enterPID;
    private BeaconManager beaconManager;
    private Region region = new Region( "allBeacons", null, null, null);
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    protected GoogleApiClient mGoogleApiClient;
    public LocationRequest mLocationRequest;
    private Context activity;
    private String caregiverNo;
    private TextView topBanner, client_name, description, distance;
    private String uuid;
    private String baseInfo = " is nearby and lost! Contact his caregiver immediately!";
    private String noAlert = "No missing people nearby";
    private Button contact, callBtn;
    private Context mContext;
    private Activity mActivity;
    private Circle circle;
    private String caretaker, name, contact_number,details, beacon_id;
    private String err;
    private int missing, unix_time;
    private boolean nearMissingPerson = false;
    private View view;
    private String detectedBID;
    private double detectedProximity;
    private static final Utils.Proximity FAR = Utils.Proximity.FAR;
    private static final Utils.Proximity IMMEDIATE = Utils.Proximity.IMMEDIATE;
    private static final Utils.Proximity NEAR = Utils.Proximity.NEAR;
    private LinearLayout dist_parent;
    private LatLng myLocation;
    private ArrayList<Circle> circleList;
    private ArrayList<Circle> trackedList;

    Geocoder geocoder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_view_volunteer);
        mContext = getApplication().getApplicationContext();

        circleList = new ArrayList<>();
        trackedList = new ArrayList<>();

        Intent intent = getIntent();
        final String userType = intent.getStringExtra("userType");
        uuid = intent.getStringExtra("uuid");

        topBanner = (TextView) findViewById(R.id.text_view);
        geocoder = new Geocoder(this);

        checkLocationPermission();
        new getInformation().execute();

        if(userType.equals("caretaker")){
            topBanner.setText("Click to go to caregiver page");
            topBanner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Intent i = new Intent(getApplicationContext(), ViewCaregiverActivity.class);
                        i.putExtra("uuid",uuid);
                        i.putExtra("userType",userType);
                        startActivity(i);
                        finish();
                }
            });
        } else {
            topBanner.setText("Welcome to the volunteer page!");
        }

        contact = (Button)findViewById(R.id.call_caregiver);
        client_name = (TextView) findViewById(R.id.client_name);
        description = (TextView)findViewById(R.id.description);



        view = (View) findViewById(R.id.row_layout);




        


        callBtn = (Button)findViewById(R.id.call_caregiver);
        dist_parent = (LinearLayout) findViewById(R.id.dist_parent);
        distance = (TextView)findViewById(R.id.distance);

        callBtn.setVisibility(view.GONE);
        description.setVisibility(view.GONE);
        dist_parent.setVisibility(view.GONE);



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
                        Log.d("testing","permissions is :  " + (ContextCompat.checkSelfPermission(getApplication().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED));

                        if (ContextCompat.checkSelfPermission(getApplication().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            Log.d("testing","location runs through here");
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
        beaconManager.setBackgroundScanPeriod(3000, 2000);
        beaconManager.setForegroundScanPeriod(3000, 2000);
        beaconManager.setRegionExitExpiration(5000);
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
                        DialogFragment missingAlert = new DetectedMissingBeaconDialogFragment();
                        missingAlert.show(getFragmentManager(), "missingAlert");
                        // TODO turn on ranging for this beacon
                    }
                }
            }
            @Override
            public void onExitedRegion(Region region) {
                // could add an "exit" notification too if you want (-:
                Log.i("volact", "No moar beacon: ");
                Toast.makeText(getApplicationContext(), "Beacon sign lost", Toast.LENGTH_SHORT).show();
                //reset UI
                callBtn.setVisibility(view.GONE);
                description.setVisibility(view.GONE);
                dist_parent.setVisibility(view.GONE);
            }
        });
        //connect to beacon if available
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(region);
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

        /*
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

        */

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
        if (circleList!=null && circleList.size()>0){

            for (Circle c:circleList){
                c.remove();
            }

            circleList.clear();
        }


//
        // Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Log.d("testing","location changed current latlng is : " + latLng);

        myLocation = latLng;

        //move camera to marker
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        CameraPosition cmp = new CameraPosition(latLng, 25, 0, 0);
        drawCircle(latLng, 10);
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
        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Ask user if need to explain
            Log.d("testing","mContext is : " + mContext);
            Log.d("testing","Activity is : " + this);
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                //Prompt user once we show explanation
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);

            } else {
                //No need to explain
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
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
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                    }
                    mMap.setMyLocationEnabled(true);
                } else {
                    //Permission denied, Disable the functionality that depends on this permission
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

        nearMissingPerson = true;

        callBtn.setVisibility(view.VISIBLE);
        description.setVisibility(view.VISIBLE);
        dist_parent.setVisibility(view.VISIBLE);

        // User touched the dialog's positive button
        Toast.makeText(getApplicationContext(), "tracking", Toast.LENGTH_SHORT).show();
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    for (Beacon b: list){
                        Log.i("missingBeacon", "id: " + b.getProximityUUID() + " dist: " + b.getMeasuredPower());
                        detectedBID = b.getProximityUUID().toString();



                        switch (Utils.computeProximity(b)){
                            case FAR:
                                detectedProximity = 70;

                                double estimatedDist = Utils.computeAccuracy(b);

                                if (estimatedDist > 3){

                                    if (estimatedDist <=15){
                                        detectedProximity = 15;
                                    } else{
                                        if (estimatedDist > 15 && estimatedDist <= 30){
                                            detectedProximity = 30;
                                        } else {
                                            if (estimatedDist > 30){
                                                detectedProximity = estimatedDist;
                                            }
                                        }
                                    }

                                } else {
                                    detectedProximity = 3;
                                }

                                break;
                            case NEAR:
                                detectedProximity = 3;
                                break;
                            case IMMEDIATE:
                                detectedProximity = 1;
                                break;

                        }

                        break;
                    }

                    if (ContextCompat.checkSelfPermission(ViewVolunteerActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        // instantiate the location manager, note you will need to request permissions in your manifest
                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        // get the last know location from your location manager.
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                            return;
                        }
                        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); //doesnt seem to work
                        Log.d("Print", "location data1 is : " + location);
                        if (location == null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            Log.d("Print", "location data2 is : " + location);
                            drawCircle(myLocation,(int) detectedProximity);
                            distance.setText((int)detectedProximity+ "m away");
                        } else{
                            //List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            LatLng detectedLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            //Log.d("Print", "address list size is : " + address.size());

                            drawCircle(detectedLatLng,(int) detectedProximity);
                            distance.setText((int)detectedProximity+ "m away");
                        }





                    }else{

                        if (ActivityCompat.shouldShowRequestPermissionRationale(ViewVolunteerActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                            //Prompt user once we show explanation
                            ActivityCompat.requestPermissions(ViewVolunteerActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);

                        } else {
                            //No need to explain
                            ActivityCompat.requestPermissions(ViewVolunteerActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                        }
                    }



                }
            }
        });
        beaconManager.startRanging(region);

        client_name.setText(name);
        description.setText(name + baseInfo);
        caregiverNo = contact_number;


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewVolunteerActivity.this,ViewDetails.class);
                Log.d("testing", "here first");
                intent.putExtra("name",name);
                intent.putExtra("details",details);
                intent.putExtra("caregiver",caretaker);
                intent.putExtra("contact",contact_number);
                intent.putExtra("unix_time",missing);
                startActivity(intent);
            }
        });

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL );
                callIntent.setData(Uri.parse("tel:" + caregiverNo));

                if (ContextCompat.checkSelfPermission(getApplication().getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                }
                startActivity(callIntent);
            }
        });

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        Toast.makeText(getApplicationContext(), "ignore", Toast.LENGTH_SHORT).show();
        beaconManager.stopRanging(region);
    }


    private void drawCircle(LatLng point, int radius) {
        //reset all circles
        if (circleList!=null && circleList.size()>0){

            for (Circle c:circleList){
                c.remove();
            }

            circleList.clear();
        }

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(radius);

        // Border color of the circle
        circleOptions.strokeColor(Color.MAGENTA);

        // Fill color of the circle
        circleOptions.fillColor(0x30bca4c1);

        // Border width of the circle
        circleOptions.strokeWidth(3);

        // Adding the circle to the GoogleMap
        circle = mMap.addCircle(circleOptions);
        circleList.add(circle);

    }

    private void drawOtherCircles(LatLng point, int radius) {
        //reset all circles
        if (circleList!=null && circleList.size()>0){

            for (Circle c:circleList){
                c.remove();
            }

            circleList.clear();
        }

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(radius);

        // Border color of the circle
        circleOptions.strokeColor(Color.MAGENTA);

        // Fill color of the circle
        circleOptions.fillColor(0x30bca4c1);

        // Border width of the circle
        circleOptions.strokeWidth(3);

        // Adding the circle to the GoogleMap
        circle = mMap.addCircle(circleOptions);
        circleList.add(circle);

    }

    private class getInformation extends AsyncTask<Object, Object, Boolean> {
        ProgressDialog pdLoading = new ProgressDialog(ViewVolunteerActivity.this);
        boolean success = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("Updating information...");
            pdLoading.show();
        }
        @Override
        protected Boolean doInBackground(Object... params) {
            //form JSON object to post
            JSONObject jsoin = null;

            try {

                jsoin = new JSONObject();
                Log.d("testing","uuid is : " + uuid);
                jsoin.put("caretaker_uuid", uuid);


            } catch (JSONException e){
                e.printStackTrace();
                err = e.getMessage();
            }


            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            String url = "https://tw9fnomwqe.execute-api.ap-southeast-1.amazonaws.com/dev/beacons/";

            String rst = UtilHttp.doHttpPostJson(getApplication().getApplicationContext(),url,jsoin.toString());
            if (rst == null) {
                err = UtilHttp.err;
            } else {

                Log.d("check",rst.toString());

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(rst);

                    missing = jsonObject.getInt("missing");
                    caretaker = jsonObject.getString("caretaker");
                    name = jsonObject.getString("name");
                    contact_number = jsonObject.getString("contact_number");
                    details = jsonObject.getString("details");
                    beacon_id = jsonObject.getString("beacon_id");


                    Log.d("testing", "or here first ???");
                    Log.d("testing", "or here first ??? details is : " + details);
                    success = true;

                } catch (JSONException e){
                    e.printStackTrace();
                    err = e.toString();
                    Log.d("check","here");
                    success = false;
                }

            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            //this method will be running on UI thread
            if (result){
                pdLoading.dismiss();

//                client_name.setText(name);
//                description.setText(name + baseInfo);
//                caregiverNo = contact_number;
//
//                view.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(ViewVolunteerActivity.this,ViewDetails.class);
//                        Log.d("testing", "here first");
//                        intent.putExtra("name",name);
//                        intent.putExtra("details",details);
//                        intent.putExtra("caregiver",caretaker);
//                        intent.putExtra("contact",contact_number);
//                        intent.putExtra("unix_time",missing);
//                        startActivity(intent);
//                    }
//                });

                client_name.setText("No missing person detected");

                Toast.makeText(getApplicationContext(), "Loaded page!", Toast.LENGTH_SHORT).show();




            } else {
                Toast.makeText(getBaseContext(), err, Toast.LENGTH_LONG).show();
            }

            pdLoading.dismiss();

        }

    }



}
