package com.example.androidnotifications;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.androidnotifications.Main2Activity.database;

public class DonorForm extends AppCompatActivity {
    Spinner cityChoice;
    Spinner groupChoice;

    EditText Name;
    EditText Mobile;

    Button Save;

    ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_form);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //String userEmail=user.getEmail();

        cityChoice = (Spinner) findViewById(R.id.dropdownCity);

        String[] citis = new String[]{"Barisal", "Chittagong", "Dhaka", "Mymensingh", "Khulna", "Rajshahi", "Rangpur", "Sylhet"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, citis);
        cityChoice.setAdapter(adapter);


        groupChoice = (Spinner) findViewById(R.id.dropdownGroup);
        String[] group = new String[]{"O+", "O-", "A+", "B+", "A-", "B-", "AB+", "AB-"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, group);
        groupChoice.setAdapter(adapter1);

        Name = (EditText) findViewById(R.id.edt_name);
        Mobile = (EditText) findViewById(R.id.edt_mobileNumber);
        Save = (Button) findViewById(R.id.btn_saveDonor);

        //   String email=firebaseAuth.getCurrentUser().getEmail();

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Name.getText().toString().isEmpty() || Mobile.getText().toString().isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DonorForm.this);
                    builder.setMessage("Please fill up all fields.");
                    builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else if (Main2Activity.lat == 0.0 && Main2Activity.lng == 0.0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DonorForm.this);
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


                    final String email = firebaseAuth.getCurrentUser().getEmail();
                    String name = Name.getText().toString();
                    final String city = cityChoice.getSelectedItem().toString();
                    final String group = groupChoice.getSelectedItem().toString();
                    String mobile = Mobile.getText().toString();
                    String lat = Main2Activity.lat.toString();
                    String lng = Main2Activity.lng.toString();
                    //String phonePrivacy =


                    //final Donor donor = new Donor(email, name, mobile, group, city, lat, lng);
                    final DatabaseReference myRef = database.getReference("donors");

              /*  myRef.child(city).child(group).orderByChild("email").equalTo(email)
                        .addListenerForSingleValueEvent(new ValueEventListener()*/
                    myRef.child(city).child(group).orderByChild("email").equalTo(email)

                            .addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {


                                    if (dataSnapshot.exists()) {
                                        //bus number exists in Database
                                        AlertDialog.Builder builder = new AlertDialog.Builder(DonorForm.this);
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
                                    //    myRef.child(city).child(group).push().setValue(donor);
                                        AlertDialog.Builder builder = new AlertDialog.Builder(DonorForm.this);
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
                            });


                }


            }
        });
    }
    //if (!Name.getText().toString().isEmpty() && !Mobile.getText().toString().isEmpty()){


    // DatabaseReference myRef = database.getReference("donors");

}
