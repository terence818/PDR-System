package com.example.pdrsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateUserInformationActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText editTextpassword, editTextemail, editTextname, editTextphone;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private RadioGroup radioGroup;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ref;
    private FirebaseUser user;

    String email;
    String username;
    String phone_number;
    String user_type;

    String prev_username, prev_email, prev_userType, prev_key, prev_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_information);

        progressBar = findViewById(R.id.login_progress);
        progressBar.bringToFront();
        editTextname = findViewById(R.id.username);
        editTextphone = findViewById(R.id.mobile_number);
        editTextemail = findViewById(R.id.email);


        if (savedInstanceState == null) {
            Intent extras = this.getIntent();
            if (extras == null) {
                prev_username = null;
                prev_email = null;
                prev_userType = null;
                prev_phone = null;
            } else {
                prev_username = extras.getStringExtra("username");
                prev_email = extras.getStringExtra("email");
                prev_userType = extras.getStringExtra("user_type");
                prev_key = extras.getStringExtra("key");
                prev_phone = extras.getStringExtra("phone");
            }
        } else {
            prev_username = (String) savedInstanceState.getSerializable("username");
            prev_email = (String) savedInstanceState.getSerializable("email");
            prev_userType = (String) savedInstanceState.getSerializable("user_type");
            prev_key = (String) savedInstanceState.getSerializable("key");
            prev_phone = (String) savedInstanceState.getSerializable("phone");
        }


        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radio0) {
                    user_type = "NGO";
                    // do operations specific to this selection
                } else if (checkedId == R.id.radio1) {
                    user_type = "Volunteer";
                    // do operations specific to this selection
                }

            }
        });


        if (prev_email != null) {
            editTextemail.setText(prev_email);
        }

        if (prev_username != null) {
            editTextname.setText(prev_username);
        }

        if (prev_phone != null) {
            editTextphone.setText(prev_phone);
        }

        if (prev_userType.equals("NGO")) {
            radioGroup.check(R.id.radio0);
        } else {
            radioGroup.check(R.id.radio1);
        }

        //register new account
        findViewById(R.id.email_sign_up_button).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.email_sign_up_button) {

            email = editTextemail.getText().toString().trim();
            username = editTextname.getText().toString().trim();
            phone_number = editTextphone.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Email address cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(username)) {
                Toast.makeText(getApplicationContext(), "Username cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(phone_number)) {
                Toast.makeText(getApplicationContext(), "Phone Number cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }


            if (user_type != null) {

                User updated_user = new User("user", email, phone_number, user_type, username);

                progressBar.setVisibility(View.GONE);
                ref.child(prev_key).child("User_Information").setValue(updated_user, new DatabaseReference.CompletionListener() {
                    public void onComplete(DatabaseError error, @NonNull DatabaseReference ref) {
                        finish();
                    }
                });


            } else {
                Toast.makeText(UpdateUserInformationActivity.this, "Please select an user type option.", Toast.LENGTH_SHORT).show();
            }

//            case R.id.sign_in:
////                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
////                startActivity(intent);
//                finish();
//                break;
        }
    }
}

