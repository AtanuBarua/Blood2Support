package com.example.androidnotifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class NeedBlood extends AppCompatActivity {

    Spinner groupChoice;
    Spinner cityChoice;
    Button need;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_blood);

        cityChoice = (Spinner) findViewById(R.id.needCity);
        groupChoice = (Spinner) findViewById(R.id.needBlood);
        need = (Button) findViewById(R.id.startSearch);

        String[] citis = new String[]{"Chittagong", "Barisal", "Dhaka", "Mymensingh", "Khulna", "Rajshahi", "Rangpur", "Sylhet"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, citis);
        cityChoice.setAdapter(adapter);


        String[] group = new String[]{"A+", "A-", "O+", "B+", "O-", "B-", "AB+", "AB-"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, group);
        groupChoice.setAdapter(adapter1);


        need.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String group = groupChoice.getSelectedItem().toString();
                String city = cityChoice.getSelectedItem().toString();

                Intent intent = new Intent(NeedBlood.this, DonorListActivity.class);
                intent.putExtra("group", group);
                intent.putExtra("city", city);
                startActivity(intent);
                //finish();
            }
        });

    }
}