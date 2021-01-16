package com.example.androidnotifications;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.androidnotifications.Main2Activity.database;

public class MainActivity extends AppCompatActivity {

    //1. Notification Channel
    //2. Notification Builder
    //3. Notification Manager
    public static final String CHANNEL_ID = "channel_id";
    private static final String CHANNEL_NAME = "channel name";
    private static final String CHANNEL_DESC = "channel desc";


    //Toolbar toolbar;
    //ProgressBar progressBar;
    ProgressDialog mProgressDialog;

    private EditText email;
    private EditText password;
    private Button signup;
    private Button login;

    Spinner cityChoice;
    Spinner groupChoice;

    CheckBox asDonor;

    EditText Name;
    EditText Mobile;

    public static Double lat = 0.0;
    public static Double lng = 0.0;

    public static FirebaseDatabase donorDatabase, donorDatabase2, userDatabase;

    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;

    FirebaseAuth firebaseAuth;

    private Switch sw1;


    //static String city;
    //static String group;
    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressDialog = new ProgressDialog(MainActivity.this);
        firebaseAuth = FirebaseAuth.getInstance();
        donorDatabase = FirebaseDatabase.getInstance();
        donorDatabase2 = FirebaseDatabase.getInstance();
        userDatabase = FirebaseDatabase.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


        if (firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified()) {
            startActivity(new Intent(MainActivity.this, UserManualActivity.class));
            finish();
        }


        //toolbar = findViewById(R.id.toolbar);
        // progressBar = findViewById(R.id.progressBar);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        signup = findViewById(R.id.btnSignup);
        login = findViewById(R.id.btnLogin);
        asDonor = findViewById(R.id.checkBox);
        //sw1 = findViewById(R.id.switch1);

        cityChoice = findViewById(R.id.dropdownCity);
        String[] cities = new String[]{"Chittagong", "Barisal", "Dhaka", "Mymensingh", "Khulna", "Rajshahi", "Rangpur", "Sylhet"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cities);
        cityChoice.setAdapter(adapter);


        groupChoice = findViewById(R.id.dropdownGroup);
        String[] group = new String[]{"A+", "A-", "O+", "B+", "O-", "B-", "AB+", "AB-"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, group);
        groupChoice.setAdapter(adapter1);

        Name = findViewById(R.id.edt_name);
        Mobile = findViewById(R.id.edt_mobileNumber);


        //toolbar.setTitle(R.string.app_name);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //     progressBar.setVisibility(View.VISIBLE);

                /*if(validateFields()){
                    // Then Submit
                    return;
                }*/

                mProgressDialog.show();
                mProgressDialog.setMessage("Creating account");

                if (asDonor.isChecked()) {


                    if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || Name.getText().toString().isEmpty() || Mobile.getText().toString().isEmpty()) {

                        mProgressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Please fill up all fields");
                        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();


                    } else if (lat == 0.0 || lng == 0.0) {
                        mProgressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Please turn on your location and try again");
                        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                finish();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setCancelable(false);
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();


                    } else {


                        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),
                                password.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        //       progressBar.setVisibility(View.GONE);
                                        if (task.isSuccessful()) {
                                            firebaseAuth.getCurrentUser().sendEmailVerification()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {



                                                                final String emaill = email.getText().toString();
                                                                String name = Name.getText().toString();
                                                                final String city = cityChoice.getSelectedItem().toString();
                                                                //city = cityChoice.getSelectedItem().toString();
                                                                final String group = groupChoice.getSelectedItem().toString();
                                                                //group = groupChoice.getSelectedItem().toString();
                                                                String mobile = Mobile.getText().toString();
                                                                String latt = lat.toString();
                                                                String lngg = lng.toString();
                                                                String availability = "1";
                                                                String city_bloodGroup = city + "_" + group;
                                                                //String city_bloodGroup_availability = city + "_" + group + "_" + availability;
                                                                String locationPrivacy = "0";
                                                                String phonePrivacy = "0";
                                                                String status = "1";


                                                                final Donor donor = new Donor(emaill, name, mobile, group, city, latt, lngg, phonePrivacy, availability, city_bloodGroup, locationPrivacy, status);
                                                               // final DatabaseReference myRef = donorDatabase.getReference("donors");
                                                                final DatabaseReference myRef = donorDatabase2.getReference("donorList");

                                                                String userId = firebaseAuth.getUid();


                                                                //myRef.child(city).child(group).child(userId).setValue(donor);
                                                                myRef.child(userId).setValue(donor);

                                                                /*myRef.child(city).child(group).push().setValue(donor);
                                                                myRef2.push().child(userId).setValue(donor);*/

                                                                mProgressDialog.dismiss();

                                                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                                builder.setMessage("Registration successful. Please check your email to verify your account");
                                                                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                                        dialogInterface.cancel();
                                                                    }
                                                                });
                                                                AlertDialog alertDialog = builder.create();
                                                                alertDialog.show();


                                                                /*email.setText("");
                                                                password.setText("");*/
                                                            } else {

                                                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                                builder.setMessage(task.getException().getMessage());
                                                                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                                        dialogInterface.cancel();
                                                                    }
                                                                });
                                                                AlertDialog alertDialog = builder.create();
                                                                alertDialog.show();
                                                            }

                                                        }
                                                    });
                                        } else {
                                   /* Toast.makeText(Main3Activity.this, task.getException().getMessage(),
                                            Toast.LENGTH_LONG).show();*/

                                            mProgressDialog.dismiss();
                                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                            builder.setMessage(task.getException().getMessage());
                                            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();
                                                }
                                            });
                                            AlertDialog alertDialog = builder.create();
                                            alertDialog.show();

                                        }
                                    }
                                });

                    }


                } else {


                    if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || Name.getText().toString().isEmpty() || Mobile.getText().toString().isEmpty()) {

                        mProgressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Please fill up all fields.");
                        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    } else if (lat == 0.0 || lng == 0.0) {

                        mProgressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Please turn on your location and try again.");
                        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                finish();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setCancelable(false);
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();


                    } else {

                        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),
                                password.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()) {
                                            firebaseAuth.getCurrentUser().sendEmailVerification()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {


                                                                final String emaill = email.getText().toString();
                                                                String name = Name.getText().toString();
                                                                final String city = cityChoice.getSelectedItem().toString();
                                                                final String group = groupChoice.getSelectedItem().toString();
                                                                String mobile = Mobile.getText().toString();
                                                                String latt = lat.toString();
                                                                String lngg = lng.toString();
                                                                String status = "1";


                                                                final Users users = new Users(emaill, name, mobile, group, city, latt, lngg, status);
                                                                final DatabaseReference myRef = userDatabase.getReference("nonDonor");
                                                                //final DatabaseReference myRef2 = database.getReference("donor");
                                                                //myRef.child(city).child(group).push().setValue(users);
                                                                String userId = firebaseAuth.getUid();
                                                                //myRef.push().setValue(users);
                                                                myRef.child(userId).setValue(users);


                                                                mProgressDialog.dismiss();
                                                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                                builder.setMessage("Registration successful. Please check your email to verifiy your account");
                                                                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                                        dialogInterface.cancel();
                                                                    }
                                                                });
                                                                AlertDialog alertDialog = builder.create();
                                                                alertDialog.show();


                                                                /*email.setText("");
                                                                password.setText("");*/
                                                            } else {

                                                                mProgressDialog.dismiss();
                                                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                                builder.setMessage(task.getException().getMessage());
                                                                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                                        dialogInterface.cancel();
                                                                    }
                                                                });
                                                                AlertDialog alertDialog = builder.create();
                                                                alertDialog.show();
                                                            }

                                                        }
                                                    });
                                        } else {

                                            mProgressDialog.dismiss();
                                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                            builder.setMessage(task.getException().getMessage());
                                            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();
                                                }
                                            });
                                            AlertDialog alertDialog = builder.create();
                                            alertDialog.show();

                                        }
                                    }
                                });
                    }


                }


            }
        });

              /*  myRef.child(city).child(group).orderByChild("email").equalTo(email)
                        .addListenerForSingleValueEvent(new ValueEventListener()*/
                      /*  myRef.child(city).child(group).orderByChild("email").equalTo(emaill)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            //bus number exists in Database
                                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                            builder.setMessage("You are already a donor");
                                            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();
                                                }
                                            });
                                            AlertDialog alertDialog = builder.create();
                                            alertDialog.show();
                                        } else {
                                            //bus number doesn't exists.
                                            myRef.child(city).child(group).push().setValue(donor);
                                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                            builder.setMessage("Congratulations! Now you are a donor. You will be notified when someone needs blood");
                                            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();
                                                    finish();
                                                }
                                            });
                                            AlertDialog alertDialog = builder.create();
                                            alertDialog.setCancelable(false);
                                            alertDialog.setCanceledOnTouchOutside(false);
                                            alertDialog.show();
                                            //finish();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });*/


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });


    }

   /* private boolean validateFields() {
        int yourDesiredLength = 5;
        if (email.getText().length() < yourDesiredLength) {
            email.setError("Your Input is Invalid");
            return false;
        } else if (password.getText().length() < yourDesiredLength) {
            password.setError("Your Input is Invalid");
            return false;
        } else {
            return true;
        }
    }*/

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

                                    lat = location.getLatitude();
                                    lng = location.getLongitude();
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on your location.", Toast.LENGTH_LONG).show();
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