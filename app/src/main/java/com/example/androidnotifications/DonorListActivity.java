package com.example.androidnotifications;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.androidnotifications.Main2Activity.database;

public class DonorListActivity extends AppCompatActivity{


    String city;
    String group;

    /*ArrayList<String> donorList;
    ArrayAdapter<String> arrayAdapter;
    public static ArrayList<Donor> donorInfo;*/

    Button buttonMap;


    //private static final String TAG = "Main2Activity";

    //private boolean isPermission;
    float distance, dist;


    //DatabaseReference databaseReference;

    ProgressDialog progressDialog;

    public static List<Donor> list = new ArrayList<>();


    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;




    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    double lat=0.0,lng=0.0;
    //Location mLastLocation;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_list2);

        Bundle extras = getIntent().getExtras();
        city = extras.getString("city");
        group = extras.getString("group");
        Log.i("NAME", city);
        Log.i("NAME", group);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(DonorListActivity.this));

        progressDialog = new ProgressDialog(DonorListActivity.this);

        progressDialog.setMessage("Loading data from database");

        progressDialog.show();

        buttonMap =  findViewById(R.id.Button_mapShow);
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DonorListActivity.this, MapsActivity.class));
            }
        });


        //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("donors");

       /* double lat = Main2Activity.lat;
        double lan = Main2Activity.lng;*/

       final String city_bloodGroup = city+"_"+group;


        //databaseReference.addValueEventListener(new ValueEventListener() {
        database = FirebaseDatabase.getInstance();

        DatabaseReference myRef =  database.getReference("donorList");
        myRef.orderByChild("city_bloodGroup").equalTo(city_bloodGroup).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                    progressDialog.dismiss();
                    if (dataSnapshot.exists()) {


                        Donor donorDetails = dataSnapshot.getValue(Donor.class);


                        double locationLat = 0.0;
                        double locationLng = 0.0;

                        locationLat = Double.parseDouble(donorDetails.lat);
                        locationLng = Double.parseDouble(donorDetails.lan);

                        LatLng donar = new LatLng(locationLat, locationLng);
                        //latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

                        Location loc1 = new Location("");
                        loc1.setLatitude(lat);
                        loc1.setLongitude(lng);

                        Location loc2 = new Location("");
                        loc2.setLatitude(donar.latitude);
                        loc2.setLongitude(donar.longitude);
                        distance = loc1.distanceTo(loc2);
                        dist = distance / 740;
                        String distString = String.format("%.2f", dist);
                        donorDetails.setDistance(distString);
                        //Donor dnr = new Donor(donorDetails.name, donorDetails.disString);
                        //dnr.setDistance(distString);
                        //String donorInfo = donorDetails.name + "   \n" +distString+" km";



                        list.add(donorDetails);

                        Collections.sort(list,Donor.myDistance);


                        adapter = new RecyclerViewAdapter(DonorListActivity.this, list, distString);
                        //arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, donorList);

                        recyclerView.setAdapter(adapter);



                    }


                 else {

                    progressDialog.dismiss();
                    Toast.makeText(DonorListActivity.this, "No users found", Toast.LENGTH_LONG).show();
                }

            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                progressDialog.dismiss();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                progressDialog.dismiss();
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }

        });



        list.clear();



    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    /*latTextView.setText(location.getLatitude()+"");
                                    lonTextView.setText(location.getLongitude()+"");*/
                                    lat=location.getLatitude();
                                    lng=location.getLongitude();
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            /*latTextView.setText(mLastLocation.getLatitude()+"");
            lonTextView.setText(mLastLocation.getLongitude()+"");*/
            lat=mLastLocation.getLatitude();
            lng=mLastLocation.getLongitude();
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }








}