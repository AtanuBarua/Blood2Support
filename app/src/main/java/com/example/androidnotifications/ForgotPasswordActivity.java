package com.example.androidnotifications;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    //Toolbar toolbar;
    //ProgressBar progressBar;
    EditText userEmail;
    Button userPass;

    ProgressDialog mProgressDialog;
    FirebaseAuth firebaseAuth;

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mProgressDialog=new ProgressDialog(ForgotPasswordActivity.this);

        //  toolbar = findViewById(R.id.toolbar3);
        // progressBar = findViewById(R.id.progressBar);
        userEmail = findViewById(R.id.etUserEmail);
        userPass = findViewById(R.id.btnForgotPass);

        //toolbar.setTitle("Forgot password");

        firebaseAuth = FirebaseAuth.getInstance();

        userPass.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View view) {

                mProgressDialog.show();
                mProgressDialog.setMessage("Please wait");
                if (!userEmail.getText().toString().isEmpty() ) {

                    firebaseAuth.sendPasswordResetEmail(userEmail.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        mProgressDialog.dismiss();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
                                        builder.setMessage("Password sent to your email");
                                        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        });
                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();
                                    } else {

                                        mProgressDialog.dismiss();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
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

                     else {

                         mProgressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
                        builder.setMessage("Please provide your Email");
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