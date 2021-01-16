package com.example.androidnotifications;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PrivacySetting extends AppCompatActivity {


    private Button btnSave;
    private RadioGroup mRadioGroupPhone, mRadioGroupLocation;
    private RadioButton mRadioButtonPhone, mRadioButtonLocation;

    private RadioButton phoneMe, phoneAll;
    private RadioButton mapMe, mapAll;

    private FirebaseDatabase mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mReference;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_setting);


        btnSave=findViewById(R.id.btnSave);
        mTextView = findViewById(R.id.txtvw);


        mRadioGroupPhone = findViewById(R.id.radioGroupPhone);
        mRadioGroupLocation = findViewById(R.id.radioGroupMap);

        phoneAll = findViewById(R.id.phoneAll);
        phoneMe  = findViewById(R.id.phoneMe);
        mapMe = findViewById(R.id.mapMe);
        mapAll = findViewById(R.id.mapAll);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("donorList");

        final String email = mFirebaseAuth.getCurrentUser().getEmail();
        final String userId = mFirebaseAuth.getCurrentUser().getUid();


        mReference.orderByChild("email").equalTo(email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    Donor donor = dataSnapshot.getValue(Donor.class);
                    String privacyPhone = donor.phonePrivacy;
                    if (privacyPhone.equals("1")){
                        phoneMe.setChecked(true);
                    }
                    else {
                        phoneAll.setChecked(true);
                    }

                    String privacyLocation = donor.locationPrivacy;
                    if (privacyLocation.equals("1")){
                        mapMe.setChecked(true);
                    }
                    else {
                        mapAll.setChecked(true);
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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int selectedIdPhone = mRadioGroupPhone.getCheckedRadioButtonId();
                int selectedIdLocation = mRadioGroupLocation.getCheckedRadioButtonId();

                mRadioButtonPhone = findViewById(selectedIdPhone);
                mRadioButtonLocation = findViewById(selectedIdLocation);


                //String kire;

                mReference.orderByChild("email").equalTo(email).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if (dataSnapshot.exists()){

                            if(selectedIdPhone==-1){
                                Toast.makeText(PrivacySetting.this,"Nothing selected", Toast.LENGTH_SHORT).show();

                            }
                            else{
                                //Toast.makeText(PrivacySetting.this,mRadioButtonPhone.getText(), Toast.LENGTH_SHORT).show();
                                String privacyPhone = mRadioButtonPhone.getText().toString();
                                //mTextView.setText(kire);
                                if (privacyPhone.equals(" Everyone")){
                                    privacyPhone = "0";
                                }
                                else {
                                    privacyPhone = "1";
                                }

                                mReference.child(userId).child("phonePrivacy").setValue(privacyPhone);


                                String privacyMap = mRadioButtonLocation.getText().toString();
                                //mTextView.setText(kire);
                                if (privacyMap.equals(" Everyone")){
                                    privacyMap = "0";
                                }
                                else {
                                    privacyMap = "1";
                                }

                                mReference.child(userId).child("locationPrivacy").setValue(privacyMap);

                                AlertDialog.Builder builder = new AlertDialog.Builder(PrivacySetting.this);
                                builder.setMessage("Privacy updated successfully!");
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



               /* if(selectedIdLocation==-1){
                    Toast.makeText(PrivacySetting.this,"Nothing selected", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(PrivacySetting.this,mRadioButtonLocation.getText(), Toast.LENGTH_SHORT).show();
                }*/

            }
        });



       /*mFirebaseAuth = FirebaseAuth.getInstance();
       mDatabase = FirebaseDatabase.getInstance();
       mReference = mDatabase.getReference("donorList");

       final String email = mFirebaseAuth.getCurrentUser().getEmail();
       final String userId = mFirebaseAuth.getCurrentUser().getUid();*/

       /* btnSave = findViewById(R.id.btnSave);


        int selectedIdPhone = mRadioGroupPhone.getCheckedRadioButtonId();
        int selectedIdLocation = mRadioGroupLocation.getCheckedRadioButtonId();

        mRadioButtonPhone = findViewById(selectedIdPhone);
        mRadioButtonLocation = findViewById(selectedIdLocation);

        final RadioButton mapAll = findViewById(R.id.mapAll);
        final RadioButton mapMe = findViewById(R.id.mapMe);
        final RadioButton phoneAll = findViewById(R.id.phoneAll);
        final RadioButton phoneMe = findViewById(R.id.phoneMe);*/


    }
}
