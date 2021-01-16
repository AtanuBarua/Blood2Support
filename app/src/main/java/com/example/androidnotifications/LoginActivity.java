package com.example.androidnotifications;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    //Toolbar toolbar;
    //ProgressBar progressBar;
    private EditText userEmail;
    private EditText userPass;
    private Button userLogin;
    private Button forgotPass;
    ProgressDialog mProgressDialog;

    private FirebaseAuth firebaseAuth;





    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mProgressDialog = new ProgressDialog(LoginActivity.this);


        //toolbar = findViewById(R.id.toolbar2);
        //progressBar = findViewById(R.id.progressBar);
        userEmail = findViewById(R.id.etUserEmail);
        userPass = findViewById(R.id.etUserPass);
        userLogin = findViewById(R.id.btnUserLogin);
        forgotPass = findViewById(R.id.btnUserForgottPass);
        //toolbar.setTitle("Login");

        firebaseAuth = FirebaseAuth.getInstance();




        if (firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified()) {

            startActivity(new Intent(LoginActivity.this, PermissionsActivity.class));
            finish();
        }

        //firebaseAuth = FirebaseAuth.getInstance();



        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //     progressBar.setVisibility(View.VISIBLE);
                mProgressDialog.show();
                mProgressDialog.setMessage("Please wait");
                if (!userEmail.getText().toString().isEmpty() && !userPass.getText().toString().isEmpty()) {
                    firebaseAuth.signInWithEmailAndPassword(userEmail.getText().toString(),
                            userPass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {


                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //  progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        if (firebaseAuth.getCurrentUser().isEmailVerified()  ) {


                                            mProgressDialog.dismiss();
                                            startActivity(new Intent(LoginActivity.this, PermissionsActivity.class));
                                            finish();

                                        } else {
                                            mProgressDialog.dismiss();

                                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                            builder.setMessage("Please first verify your email address");
                                            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();
                                                }
                                            });
                                            AlertDialog alertDialog = builder.create();
                                            alertDialog.show();
                                        }
                                    } else {


                                        mProgressDialog.dismiss();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Please fill up Email & Password fields");
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

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
    }
}
