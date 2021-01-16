package com.example.androidnotifications;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;

import java.util.Timer;
import java.util.TimerTask;

public class SendingEmail extends AppCompatActivity {

    private TextView eTo;
    private TextView eSubject;
    private EditText eMsg;
    private Button btn, donorForm;

    Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending_email);

        final String TempHolder = getIntent().getStringExtra("EMAIL");
        String TempHolder2 = getIntent().getStringExtra("MOBILE");

        eTo = findViewById(R.id.txtTo);
        eSubject = findViewById(R.id.txtSub);
        eMsg = findViewById(R.id.txtMsg);
        btn = findViewById(R.id.btnSend);

        donorForm = findViewById(R.id.btnDonorForm);
        /*eTo.setEnabled(false);
        eSubject.setEnabled(false);*/

        eTo.setText(TempHolder);
        //eMsg.setText(TempHolder2);

        donorForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SendingEmail.this,FormActivity.class);
                intent.putExtra("EMAIL", TempHolder);
                startActivity(intent);
            }
        });


        /*mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SendingEmail.this,FormActivity.class);
                intent.putExtra("EMAIL", TempHolder);
                startActivity(intent);
            }
        },5000);*/


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Intent.ACTION_SEND);
                it.putExtra(Intent.EXTRA_EMAIL, new String[]{eTo.getText().toString()});
                it.putExtra(Intent.EXTRA_SUBJECT,eSubject.getText().toString());
                it.putExtra(Intent.EXTRA_TEXT,eMsg.getText());
                it.setType("message/rfc822");
                startActivity(Intent.createChooser(it,"Choose Mail App"));



                /*AlertDialog.Builder builder = new AlertDialog.Builder(SendingEmail.this);
                builder.setMessage("Mail sent to donor. If he/she doesn't responds try mailing/calling other donors");
                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.setCancelable(true);
                alertDialog.show();*/
               // Intent intent = new Intent(SendingEmail.this,Main2Activity.)

                //finish();


            }
        });


    }
}
