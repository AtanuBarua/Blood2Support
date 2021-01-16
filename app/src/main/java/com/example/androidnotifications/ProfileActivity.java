package com.example.androidnotifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {


    public static final String NODE_USERS = "users";
    private List<User> userList;
    private RecyclerView recyclerView;


    // private TextView userEmail;
    private Button userLogout;

    private FirebaseAuth firebaseAuth;


    private FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        //  final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        //   userEmail = findViewById(R.id.tvUserEmail);
        userLogout = findViewById(R.id.btnLogout);

        //firebaseAuth = FirebaseAuth.getInstance();
        // firebaseUser = firebaseAuth.getCurrentUser();

        //final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        loadUsers();

        FirebaseInstanceId.getInstance().getInstanceId().
                addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {

                        if (task.isSuccessful()) {

                            String token = task.getResult().getToken();
                            saveToken(token);

                        } else {


                        }

                    }
                });

      /*  FirebaseUser mUser = firebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String token = task.getResult().getToken();
                            saveToken(token);
                            // Send token to your backend via HTTPS
                            // ...
                        } else {
                            // Handle error -> task.getException();
                        }
                    }
                });*/


        userLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signOut();

            }
        });

    }


    private void loadUsers() {

        //   progressBar.setVisibility(View.VISIBLE);
        userList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference("users");
        dbUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //progressBar.setVisibility(View.GONE);
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dsUser : dataSnapshot.getChildren()) {
                        User user = dsUser.getValue(User.class);
                        userList.add(user);
                    }

                    UserAdapter adapter = new UserAdapter(ProfileActivity.this, userList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(ProfileActivity.this, "No users found", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //userEmail.setText(user.getEmail());

    /**
     * userLogout.setOnClickListener(new View.OnClickListener() {
     *
     * @Override public void onClick(View view) {
     * FirebaseAuth.getInstance().signOut();
     * Intent intent = new Intent(ProfileActivity.this, Main3Activity.class);
     * intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
     * intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
     * startActivity(intent);
     * finish();
     * }
     * });
     */


    //sign out method
    public void signOut() {
        firebaseAuth.signOut();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FirebaseInstanceId.getInstance().deleteInstanceId();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

       // Intent intent = new Intent(ProfileActivity.this, SignUpActivity.class);
       // startActivity(intent);
        finish();

    }

    @Override
    protected void onStart() {
        super.onStart();

       /* if (firebaseAuth.getCurrentUser()== null) {

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }*/
    }

    @Override
    protected void onStop() {
        super.onStop();

        /*if (firebaseAuth.getCurrentUser()== null) {

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }*/
    }

    private void saveToken(String token) {
        String email = firebaseAuth.getCurrentUser().getEmail();
        User user = new User(email, token);

        DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference(NODE_USERS);

        dbUsers.child(firebaseAuth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Token saved", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
