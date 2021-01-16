package com.example.androidnotifications;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    List<Donor> MainImageUploadInfoList;
    String distString;

    FirebaseAuth mFirebaseAuth;



    public RecyclerViewAdapter(Context context, List<Donor> TempList, String distString) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
        this.distString = distString;

        mFirebaseAuth = FirebaseAuth.getInstance();


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Donor donorDetails = MainImageUploadInfoList.get(position);


        String x = donorDetails.getPhonePrivacy();
        String y = donorDetails.getAvailability();
        String z = donorDetails.getLocationPrivacy();
        //String x = null;

        /*if (x==null){
            x = studentDetails.getBloodGroup();
        }*/


        //not available
        if (y.equals("0") ){
            holder.DonorNameTextView.setText(donorDetails.getName());
            holder.DonorNumberTextView.setText("hidden");
            holder.distanceDonor.setText("hidden");
            holder.availability.setText("not available");
            holder.mButtonCall.setEnabled(false);
            holder.mButtonMail.setEnabled(false);
        }

        //phone privacy on AND available AND location privacy on
        else if (x.equals("1") && y.equals("1") && z.equals("1")){
            holder.DonorNameTextView.setText(donorDetails.getName());
            holder.DonorNumberTextView.setText("hidden");
            holder.distanceDonor.setText("hidden");
            holder.availability.setText("available");
            holder.mButtonCall.setEnabled(false);
        }

        //phone privacy on AND available AND location privacy off
        else if (x.equals("1") && y.equals("1") && z.equals("0")){
            holder.DonorNameTextView.setText(donorDetails.getName());
            holder.DonorNumberTextView.setText("hidden");
            holder.distanceDonor.setText(donorDetails.getDistance()+" km away");
            holder.availability.setText("available");
            holder.mButtonCall.setEnabled(false);
        }

        //phone privacy off AND available AND location privacy off
        else if (x.equals("0") && y.equals("1") && z.equals("0")){
            holder.DonorNameTextView.setText(donorDetails.getName());
            holder.DonorNumberTextView.setText(donorDetails.getContactNumber());
            holder.distanceDonor.setText(donorDetails.getDistance()+" km away");
            holder.availability.setText("available");
        }

        //phone privacy off AND available AND location privacy on
        else if (x.equals("0") && y.equals("1") && z.equals("1")){
            holder.DonorNameTextView.setText(donorDetails.getName());
            holder.DonorNumberTextView.setText(donorDetails.getContactNumber());
            holder.distanceDonor.setText("hidden");
            holder.availability.setText("available");
        }

        /*//phone privacy off AND available AND
        else if (x.equals("0") && y.equals("1")){
            holder.StudentNameTextView.setText(studentDetails.getName());
            holder.StudentNumberTextView.setText("hidden");
            holder.distanceDonor.setText("location not detected");
            holder.availabilty.setText("not available");
            holder.mButtonCall.setEnabled(false);
            holder.mButtonMail.setEnabled(false);
        }*/





        holder.mButtonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,DonorCall.class);
                intent.putExtra("EMAIL", donorDetails.getEmail());
                intent.putExtra("ListViewClickedValue", donorDetails.getContactNumber());
                context.startActivity(intent);
            }
        });

        holder.mButtonMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,SendingEmail.class);
                intent.putExtra("EMAIL", donorDetails.getEmail());
                intent.putExtra("MOBILE", donorDetails.getContactNumber());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView DonorNameTextView;
        public TextView DonorNumberTextView;
        public TextView distanceDonor;
        public TextView availability;
        public Button mButtonCall,mButtonMail;

        public ViewHolder(View itemView) {

            super(itemView);

            DonorNameTextView = (TextView) itemView.findViewById(R.id.ShowDonorNameTextView);

            DonorNumberTextView = (TextView) itemView.findViewById(R.id.ShowDonorNumberTextView);
            distanceDonor = itemView.findViewById(R.id.distance);
            //StudentNumberTextView = ((TextView) itemView.findViewById(R.id.ShowStudentNumberTextView)).setText();

            availability = itemView.findViewById(R.id.availability);

            mButtonCall = itemView.findViewById(R.id.btnCall);
            mButtonMail = itemView.findViewById(R.id.btnMail);

        }
    }
}