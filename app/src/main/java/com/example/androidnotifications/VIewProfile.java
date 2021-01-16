package com.example.androidnotifications;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VIewProfile extends AppCompatActivity {

    private TextView nameTxt;
    private TextView emailTxt;
    private TextView cityTxt;
    private TextView bloodGroupTxt;
    private TextView phoneNumberTxt;
    private TextView availableStatusTxt;
    private TextView hello;

    private Button updateProfile;


    FirebaseDatabase mDatabase;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference mReference, mReference2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        nameTxt = findViewById(R.id.txtName);
        emailTxt = findViewById(R.id.txtEmail);
        cityTxt = findViewById(R.id.txtCity);
        bloodGroupTxt = findViewById(R.id.txtBloodGroup);
        phoneNumberTxt = findViewById(R.id.txtPhoneNumber);
        availableStatusTxt = findViewById(R.id.txtAvailableStatus);
        hello = findViewById(R.id.hello);

        mDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mReference = mDatabase.getReference("donorList");
        mReference2 = mDatabase.getReference("nonDonor");

        updateProfile = findViewById(R.id.btnUpdateProfile);

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VIewProfile.this,UpdateProfile.class);
                startActivity(intent);
            }
        });

        String email = mFirebaseAuth.getCurrentUser().getEmail();

        mReference.orderByChild("email").equalTo(email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()){
                    Donor donor = dataSnapshot.getValue(Donor.class);
                    String name = donor.name;
                    String email = donor.email;
                    String city = donor.city;
                    String bloodGroup = donor.bloodGroup;
                    String phoneNumber = donor.contactNumber;
                    String availableStatus = donor.availability;

                    if (availableStatus.equals("1")){
                        availableStatus = "active";
                    }

                    else {
                        availableStatus = "inactive";
                    }

                    nameTxt.setText(name);
                    emailTxt.setText(email);
                    cityTxt.setText(city);
                    bloodGroupTxt.setText(bloodGroup);
                    phoneNumberTxt.setText(phoneNumber);
                    availableStatusTxt.setText(availableStatus);
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



        mReference2.orderByChild("email").equalTo(email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()){
                    Users users = dataSnapshot.getValue(Users.class);
                    String name = users.name;
                    String email = users.email;
                    String city = users.city;
                    String bloodGroup = users.bloodGroup;
                    String phoneNumber = users.contactNumber;




                    nameTxt.setText(name);
                    emailTxt.setText(email);
                    cityTxt.setText(city);
                    bloodGroupTxt.setText(bloodGroup);
                    phoneNumberTxt.setText(phoneNumber);

                    hello.setVisibility(View.GONE);

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
    }


}
