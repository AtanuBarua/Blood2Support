package com.example.androidnotifications;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfile extends AppCompatActivity {

    FusedLocationProviderClient mFusedLocationClient;

    private Button update;
    Spinner cityChoice;
    Spinner groupChoice;
    EditText Name;
    EditText Mobile;


    public static Double lat = 0.0;
    public static Double lng = 0.0;

    FirebaseDatabase mDatabase;

    FirebaseAuth mFirebaseAuth;
    int PERMISSION_ID = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mDatabase = FirebaseDatabase.getInstance();
        //mReference = mDatabase.getReference("donorList");

        update = findViewById(R.id.btnUpdate);


        cityChoice =  findViewById(R.id.dropdownCity);
        String[] cities = new String[]{"Chittagong", "Barisal", "Dhaka", "Mymensingh", "Khulna", "Rajshahi", "Rangpur", "Sylhet"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cities);
        cityChoice.setAdapter(adapter);


        groupChoice = findViewById(R.id.dropdownGroup);
        final String[] group = new String[]{"A+", "A-", "O+", "B+", "O-", "B-", "AB+", "AB-"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, group);
        groupChoice.setAdapter(adapter1);

        mFirebaseAuth = FirebaseAuth.getInstance();

        Name = findViewById(R.id.edt_name);
        Mobile = findViewById(R.id.edt_mobileNumber);




        final DatabaseReference myRef2 = mDatabase.getReference("donorList");
        final DatabaseReference myRef = mDatabase.getReference("nonDonor");

        final String userId = mFirebaseAuth.getCurrentUser().getUid();
        final String mail = mFirebaseAuth.getCurrentUser().getEmail();
        //String name;

        /*myRef2.orderByChild("email").equalTo(mail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Donor donor = dataSnapshot.getValue(Donor.class);
                    String name = donor.name;

                    Name.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


        myRef2.orderByChild("email").equalTo(mail).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    Donor donor = dataSnapshot.getValue(Donor.class);
                    String name = donor.name;
                    String mobile = donor.contactNumber;


                    Name.setText(name);
                    Mobile.setText(mobile);


                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        myRef.orderByChild("email").equalTo(mail).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    Users users = dataSnapshot.getValue(Users.class);
                    String name = users.name;
                    String mobile = users.contactNumber;


                    Name.setText(name);
                    Mobile.setText(mobile);


                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                //Donor donor = new Donor();




                final String name = Name.getText().toString();
                final String city = cityChoice.getSelectedItem().toString();
                final String group = groupChoice.getSelectedItem().toString();
                final String city_bloodGroup = city+"_"+group;
                final String mobile = Mobile.getText().toString();
                final String latt = lat.toString();
                final String lngg = lng.toString();
                //String availability = availableStatus.toString();




                //final Donor donor = new Donor(emaill, name, mobile, group, city, latt, lngg,phonePrivacy,availability);
               // final DatabaseReference myRef = mDatabase.getReference("donors");

                myRef2.orderByChild("email").equalTo(mail).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if (dataSnapshot.exists()){
                            myRef2.child(userId).child("name").setValue(name);
                            myRef2.child(userId).child("city").setValue(city);
                            myRef2.child(userId).child("bloodGroup").setValue(group);
                            myRef2.child(userId).child("contactNumber").setValue(mobile);
                            myRef2.child(userId).child("lat").setValue(latt);
                            myRef2.child(userId).child("lan").setValue(lngg);
                            myRef2.child(userId).child("city_bloodGroup").setValue(city_bloodGroup);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




                myRef.orderByChild("email").equalTo(mail).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if (dataSnapshot.exists()){
                            myRef.child(userId).child("name").setValue(name);
                            myRef.child(userId).child("city").setValue(city);
                            myRef.child(userId).child("bloodGroup").setValue(group);
                            myRef.child(userId).child("contactNumber").setValue(mobile);
                            myRef.child(userId).child("lat").setValue(latt);
                            myRef.child(userId).child("lan").setValue(lngg);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //myRef.child(city).child(group).child(userId).setValue(donor);
                //myRef2.child(userId).setValue(donor);
                //myRef2.child(city).child(group).child(userId).child("availability").setValue(availability);





                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfile.this);
                builder.setMessage("Profile updated successfully!");
                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.setCancelable(false);
                alertDialog.setCanceledOnTouchOutside(false);

            }
        });
    }


    @SuppressLint("MissingPermission")
    private void getLastLocation() {
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
                                 /*   latTextView.setText(location.getLatitude()+"");
                                    lonTextView.setText(location.getLongitude()+"");*/
                                    lat = location.getLatitude();
                                    lng = location.getLongitude();
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
    private void requestNewLocationData() {

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
            lat = mLastLocation.getLatitude();
            lng = mLastLocation.getLongitude();
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
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }
}
