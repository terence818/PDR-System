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

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText editTextpassword, editTextemail, editTextname, editTextphone;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private RadioGroup radioGroup;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ref;
    private FirebaseUser user;


    String email;
    String password;
    String username;
    String phone_number;
    String user_type;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressBar = findViewById(R.id.login_progress);
        progressBar.bringToFront();
        editTextpassword = findViewById(R.id.password);
        editTextname = findViewById(R.id.username);
        editTextphone= findViewById(R.id.mobile_number);
        editTextemail = findViewById(R.id.email);


        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch(checkedId){
                    case R.id.radio0:
                       user_type="NGO";
                        // do operations specific to this selection
                        break;
                    case R.id.radio1:
                        user_type="Volunteer";
                        // do operations specific to this selection
                        break;

                }
            }
        });

        //register new account
        findViewById(R.id.email_sign_up_button).setOnClickListener(this);

        //intent to login activity
        findViewById(R.id.sign_in).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_sign_up_button:
                email = editTextemail.getText().toString().trim();
                password = editTextpassword.getText().toString().trim();
                username = editTextname.getText().toString().trim();
                phone_number = editTextphone.getText().toString().trim();



                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Email address cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
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

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!email.matches(emailPattern))
                {
                    Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(user_type!=null) {
                    progressBar.setVisibility(View.VISIBLE);
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.

                                        if (!task.isSuccessful()) {
                                            // there was an error
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(RegisterActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();

                                        }
                                    else {
                                        auth = FirebaseAuth.getInstance();
                                        user = auth.getCurrentUser();
                                        User updated_user = new User("user",email,phone_number,user_type,username);





                                        progressBar.setVisibility(View.GONE);
                                        ref.child(user.getUid()).child("User_Information").setValue(updated_user, new DatabaseReference.CompletionListener() {
                                            public void onComplete(DatabaseError error, @NonNull DatabaseReference ref) {

                                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });

                                    }
                                }
                            });
                } else {
                    Toast.makeText(RegisterActivity.this, "Please select an user type option.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.sign_in:
//                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                startActivity(intent);
                finish();
                break;
        }
    }
}

