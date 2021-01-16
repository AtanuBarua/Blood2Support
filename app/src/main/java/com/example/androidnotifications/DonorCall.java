package com.example.androidnotifications;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

public class DonorCall extends AppCompatActivity {

    EditText editText;
    Button mButton, mDonorForm;

    Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_call);

        editText = findViewById(R.id.editText1);
        mButton = findViewById(R.id.btnCall);

        mDonorForm = findViewById(R.id.btnDonorForm);
        // Receiving value into activity using intent.
        String TempHolder2 = getIntent().getStringExtra("ListViewClickedValue");

        // Setting up received value into EditText.
        editText.setText(TempHolder2);

        final String TempHolder = getIntent().getStringExtra("EMAIL");


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPhoneNumber();

            }
        });


        mDonorForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DonorCall.this,FormActivity.class);
                intent.putExtra("EMAIL", TempHolder);

                startActivity(intent);

            }
        });

        /*mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(DonorCall.this,FormActivity.class);
                intent.putExtra("EMAIL", TempHolder);
                startActivity(intent);
            }
        },5000);*/


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if(requestCode == 101)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                callPhoneNumber();

            }
        }
    }

    public void callPhoneNumber()
    {
        try
        {
            if(Build.VERSION.SDK_INT > 22)
            {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DonorCall.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+editText.getText().toString()));
                startActivity(callIntent);




            }
            else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+editText.getText().toString()));
                startActivity(callIntent);


            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


}