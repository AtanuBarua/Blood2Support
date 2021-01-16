package com.example.androidnotifications;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.androidnotifications.Main2Activity.database;

public class DonorRequest extends AppCompatActivity {


    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    FirebaseAuth mFirebaseAuth;



    TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_request);


        recyclerView = findViewById(R.id.list);



        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        fetch();


    }





    private void fetch() {

        mFirebaseAuth = FirebaseAuth.getInstance();

        final String email = mFirebaseAuth.getCurrentUser().getEmail();

        final String userId = mFirebaseAuth.getCurrentUser().getUid();


        //String donor = email+"_0";

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("donation_record").orderByChild("donorEmail").equalTo(email);

        message = findViewById(R.id.txtmessage);



        final FirebaseRecyclerOptions<Request> options =
                new FirebaseRecyclerOptions.Builder<Request>()
                        .setQuery(query, new SnapshotParser<Request>() {
                            @NonNull
                            @Override
                            public Request parseSnapshot(@NonNull DataSnapshot snapshot) {



                                return new Request(snapshot.child("bloodGroup").getValue().toString(),
                                        snapshot.child("city").getValue().toString(),
                                        snapshot.child("receiverEmail").getValue().toString(),
                                        snapshot.child("bloodGroup").getValue().toString(),
                                        snapshot.child("donationDate").getValue().toString());



                            }


                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<Request, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recylcerview_request, parent, false);

                return new ViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(final ViewHolder holder, final int position, final Request model) {



                holder.setTxtTitle(model.getmTitle());
                holder.setTxtDesc(model.getmDesc());
                holder.setBloodGroup(model.getBloodGroup());
                holder.setDonationDate(model.getDonationDate());


                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(DonorRequest.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    }
                });

                holder.acceptReq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        String receiverEmail = model.getmDesc();
                        String city = model.getmTitle();
                        String bloodGroup = model.getBloodGroup();
                        String donationDate = model.getDonationDate();


                        String donor_receiver = email+"_"+receiverEmail;

                        DonationRecord record = new DonationRecord(email,receiverEmail,city,bloodGroup,donationDate, donor_receiver);
                        DatabaseReference myRef = database.getReference("donation_record2");
                        DatabaseReference mReference = database.getReference("donorList");

                        myRef.push().setValue(record);


                        mReference.child(userId).child("availability").setValue("0");
                        mReference.child(userId).child("locationPrivacy").setValue("1");
                        mReference.child(userId).child("phonePrivacy").setValue("1");



                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        Query applesQuery = ref.child("donation_record").orderByChild("donor_receiver").equalTo(donor_receiver);

                        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                    appleSnapshot.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });


                        AlertDialog.Builder builder = new AlertDialog.Builder(DonorRequest.this);
                        builder.setMessage("Donation request accepted successfully.");
                        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setCancelable(false);
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();


            }
                });


                holder.cancelReq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        String receiverEmail = model.getmDesc();
                        //String city = model.getmTitle();
                        //String bloodGroup = model.getBloodGroup();
                        //String donationDate = model.getDonationDate();


                        String donor_receiver = email+"_"+receiverEmail;

                        //DonationRecord record = new DonationRecord(email,receiverEmail,city,bloodGroup,donationDate, donor_receiver);
                        //DatabaseReference myRef = database.getReference("donation_record2");


                       // myRef.push().setValue(record);


                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        Query applesQuery = ref.child("donation_record").orderByChild("donor_receiver").equalTo(donor_receiver);

                        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                    appleSnapshot.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });


                        AlertDialog.Builder builder = new AlertDialog.Builder(DonorRequest.this);
                        builder.setMessage("Donation request cancelled successfully.");
                        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setCancelable(false);
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();


                    }
                });

            }

        };
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;
        public TextView txtTitle;
        public TextView txtDesc;
        public TextView bloodGroup;
        public TextView donationDate;
        public TextView acceptReq;
        public TextView cancelReq;



        public ViewHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.list_root);
            txtTitle = itemView.findViewById(R.id.list_title);
            txtDesc = itemView.findViewById(R.id.list_desc);
            bloodGroup = itemView.findViewById(R.id.list_group);
            donationDate = itemView.findViewById(R.id.list_date);
            acceptReq = itemView.findViewById(R.id.acceptReq);
            cancelReq = itemView.findViewById(R.id.cancelReq);




        }

        public void setTxtTitle(String string) {
            txtTitle.setText(string);
        }


        public void setTxtDesc(String string) {
            txtDesc.setText(string);
        }


        public void setBloodGroup(String string) {
            bloodGroup.setText(string);
        }


        public void setDonationDate(String string) {
            donationDate.setText(string);
        }
    }



}
