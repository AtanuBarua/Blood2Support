package com.example.androidnotifications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import static com.example.androidnotifications.Main2Activity.database;

public class FormActivity extends AppCompatActivity {


    EditText cityChoice;
    Spinner groupChoice;

    TextView DonorEmail;


    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;


    Button Save;


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authListener;

    ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        firebaseAuth = FirebaseAuth.getInstance();
        //final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //String userEmail=user.getEmail();

        cityChoice =  findViewById(R.id.dropdownCity);




        groupChoice = (Spinner) findViewById(R.id.dropdownGroup);
        String[] group = new String[]{"O+", "O-", "A+", "B+", "A-", "B-", "AB+", "AB-"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, group);
        groupChoice.setAdapter(adapter1);

        DonorEmail =  findViewById(R.id.edt_donor_email);
        //DonationDate =  findViewById(R.id.textView3);
        Save = (Button) findViewById(R.id.btn_saveForm);


        dateView = (TextView) findViewById(R.id.textView3);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        //   String email=firebaseAuth.getCurrentUser().getEmail();

        final String emailId = getIntent().getStringExtra("EMAIL");

        DonorEmail.setText(emailId);


        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String donorEmail = DonorEmail.getText().toString();
                String city = cityChoice.getText().toString();
                String group = groupChoice.getSelectedItem().toString();
                //String donationDate = DonationDate.getText().toString();
                String donationDate = dateView.getText().toString();

                String receiverEmail = firebaseAuth.getCurrentUser().getEmail();

                String donor_receiver = donorEmail+"_"+receiverEmail;

                DonorReceiver record = new DonorReceiver(donorEmail,receiverEmail,donationDate,group,city,donor_receiver);
                DatabaseReference myRef = database.getReference("donation_record");

                if (donorEmail.isEmpty() || donationDate.isEmpty() ) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(FormActivity.this);
                    builder.setMessage("Please fill up all fields");
                    builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();


                }

                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FormActivity.this);
                    builder.setMessage("Your form has been submitted successfully.");
                    builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            finish();
//                            Intent intent = new Intent(FormActivity.this,Main2Activity.class);
//                            startActivity(intent);

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    myRef.push().setValue(record);
                }



                //finish();
            }
        });





    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
/*        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();*/
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
}
