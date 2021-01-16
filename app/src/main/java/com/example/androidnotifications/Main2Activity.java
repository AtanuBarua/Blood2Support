package com.example.androidnotifications;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.IOException;


public class Main2Activity extends AppCompatActivity {

    public static FirebaseDatabase database,database2;
    DatabaseReference mReference,mReference2;

    /*Button buttonDonor;
    Button buttonLogout;*/
    private Button needBlood;
    private Button userLogout;
    private Button reportDonor;

    private Button viewProfilebtn;
    private Button btnPrivacySetting;

    Button requests;

    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authListener;
    public static final String NODE_USERS = "users";


    public static String donorId = "no";
    //SharedPreferences sharedPreferences;




    public static Double lat = 0.0;
    public static Double lng = 0.0;



    //Switch mSwitch;
    SwitchCompat mSwitchCompat;


    //private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;

    String availability;


    @Override
    public void onBackPressed() {
        new android.app.AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Main2Activity.super.onBackPressed();
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        database2 = FirebaseDatabase.getInstance();
        mReference = database2.getReference("donorList");
        mReference2 = database2.getReference("nonDonor");

        //new
        firebaseAuth = FirebaseAuth.getInstance();


        //new
        String mail = firebaseAuth.getCurrentUser().getEmail();
        mReference.orderByChild("email").equalTo(mail).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()) {

                    Donor donor = dataSnapshot.getValue(Donor.class);
                    String status = donor.status;

                    if (status.equals("0")){
                        /*startActivity(new Intent(Main2Activity.this, LoginActivity.class));
                        finish();*/
                        firebaseAuth.getCurrentUser().delete();
                        firebaseAuth.signOut();

                    }
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


        /*mReference2.orderByChild("email").equalTo(mail).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()) {

                    Users donor = dataSnapshot.getValue(Users.class);
                    String status = donor.status;

                    if (status.equals("0")){
                        *//*startActivity(new Intent(Main2Activity.this, LoginActivity.class));
                        finish();*//*
                        firebaseAuth.getCurrentUser().delete();
                        firebaseAuth.signOut();

                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                firebaseAuth.getCurrentUser().delete();
                firebaseAuth.signOut();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                firebaseAuth.getCurrentUser().delete();
                firebaseAuth.signOut();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                firebaseAuth.getCurrentUser().delete();
                firebaseAuth.signOut();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                firebaseAuth.getCurrentUser().delete();
                firebaseAuth.signOut();
            }
        });*/


        //Donor dnr  = new Donor();
        //mSwitch = findViewById(R.id.switchAvailability);
        firebaseAuth = FirebaseAuth.getInstance();

        final String userId = firebaseAuth.getCurrentUser().getUid();

        mSwitchCompat = findViewById(R.id.switch1);

//        DatabaseReference b = database.getReference("donorList");

        btnPrivacySetting = findViewById(R.id.btnPrivacySetting);


        String email = firebaseAuth.getCurrentUser().getEmail();


        requests = findViewById(R.id.btnRequest);

        reportDonor = findViewById(R.id.btnReport);

        requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, DonorRequest.class);
                startActivity(intent);
            }
        });

        reportDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, ReportDonor.class);
                startActivity(intent);
            }
        });

        btnPrivacySetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this,PrivacySetting.class);
                startActivity(intent);
            }
        });



        SharedPreferences sharedPreferences = getSharedPreferences("save",MODE_PRIVATE);
        mSwitchCompat.setChecked(sharedPreferences.getBoolean("value",true));
        mSwitchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSwitchCompat.isChecked()){
                    SharedPreferences.Editor editor = getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",true);
                    editor.apply();

                    //String userId = firebaseAuth.getCurrentUser().getUid();




                    mSwitchCompat.setChecked(true);
                    availability = "1";
                    String phone = "0";
                    String location = "0";
                    mReference.child(userId).child("availability").setValue(availability);
                    mReference.child(userId).child("phonePrivacy").setValue(phone);
                    mReference.child(userId).child("locationPrivacy").setValue(location);

                }

                else {
                    SharedPreferences.Editor editor = getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",false);
                    editor.apply();

                    mSwitchCompat.setChecked(false);
                    availability = "0";
                    String phone = "1";
                    String location = "1";
                    mReference.child(userId).child("availability").setValue(availability);
                    mReference.child(userId).child("phonePrivacy").setValue(phone);
                    mReference.child(userId).child("locationPrivacy").setValue(location);

                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Main2Activity.this);
                    builder.setMessage("If you turn off your available status your phone number and location will be automatically hidden.");
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

            }
        });


        mReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    mSwitchCompat.setVisibility(View.VISIBLE);
                    btnPrivacySetting.setVisibility(View.VISIBLE);
                    requests.setVisibility(View.VISIBLE);


                    //mSwitchCompat.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


















        // str1 = sw1.getTextOff().toString();



        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();


        userLogout = findViewById(R.id.btnLogout);






        viewProfilebtn = findViewById(R.id.btnViewProfile);

        viewProfilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this,VIewProfile.class);
                startActivity(intent);
            }
        });











        //  mTextView.setText(userId);

  /*      mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mSwitch.isChecked()){
                    availability = "1";
                    mReference.child(userId).child("availability").setValue(availability);
                }
                else {
                    availability = "0";
                    mReference.child(userId).child("availability").setValue(availability);
                    mSwitch.setChecked(false);
                }
            }
        });
        if (mReference.child(userId).child("availability").equals("0")){
            mSwitch.setChecked(false);
        }
        else {
            mSwitch.setChecked(true);
        }*/


        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(Main2Activity.this, LoginActivity.class));
                    finish();
                }

            }
        };
        FirebaseInstanceId.getInstance().getInstanceId().
                addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {

                        if (task.isSuccessful()) {

                            String token = task.getResult().getToken();
                            // saveToken(token);

                        } else {


                        }

                    }
                });


        // Connecting to the database
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("donors");


        /**
         * Wiring up every thing
         */


        /*buttonInfo = (Button) findViewById(R.id.btn_info);
        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this, Main3Activity.class));
            }
        });*/
        userLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signOut();

            }
        });


        needBlood = findViewById(R.id.btn_need_blood);
        needBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this, NeedBlood.class));
            }
        });


       /* buttonDonor = findViewById(R.id.btn_donor_profile);
        if (donorId.toString().equals("no")) {
            buttonDonor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Main2Activity.this, DonorForm.class));
                }
            });
        } else {
        }*/


        /**
         * Initializing variable
         */
        /*try {
            donorId = sharedPreferences.getString("id", "no");
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }



    public void signOut() {



        firebaseAuth.signOut();
        /*SharedPreferences.Editor editor = getSharedPreferences("save",MODE_PRIVATE).edit();
        editor.clear().commit();*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FirebaseInstanceId.getInstance().deleteInstanceId();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Intent intent = new Intent(Main2Activity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }



    /*private void saveToken(String token) {
        String email = firebaseAuth.getCurrentUser().getEmail();
        User user = new User(email, token);
        DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference(NODE_USERS);
        dbUsers.child(firebaseAuth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //       Toast.makeText(Main2Activity.this, "Token saved", Toast.LENGTH_LONG).show();
                }
            }
        });
    }*/



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
                                 /*   latTextView.setText(location.getLatitude()+"");
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