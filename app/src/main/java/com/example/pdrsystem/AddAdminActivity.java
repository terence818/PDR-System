package com.example.pdrsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddAdminActivity extends AppCompatActivity {

    String states;
    private EditText  editTextstate;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private String currentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);

        editTextstate = findViewById(R.id.state_edit_input);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (auth.getCurrentUser() != null) {
            currentUserID = auth.getCurrentUser().getUid();
        } else {
            currentUserID = null;
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();


        Button add = findViewById(R.id.state_upload_button);
       add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                states = editTextstate.getText().toString().trim();

                if (TextUtils.isEmpty(states)) {
                    Toast.makeText(getApplicationContext(), "State cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                State state = new State(states);
                databaseReference.child("Malaysia_Area_Information").push().setValue(state, new DatabaseReference.CompletionListener() {
                    public void onComplete(DatabaseError error, @NonNull DatabaseReference ref) {

                        Toast.makeText(getApplicationContext(), " State Uploads Successfully.", Toast.LENGTH_SHORT).show();


                    }
                });
//
            }
        });


    }
}
