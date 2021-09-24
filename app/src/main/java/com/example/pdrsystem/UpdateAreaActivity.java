package com.example.pdrsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class UpdateAreaActivity extends AppCompatActivity  implements View.OnClickListener {


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ref;
    private FirebaseAuth auth;
    private FirebaseUser user;

    EditText editText,edit_text_2,textView1,Number1,Number2,Number3, water_needed, blanket_needed;

    private ProgressBar progressBar;
    private RadioGroup radioGroup, radioGroup2;
    private ValueEventListener a;

    private String state, status, area;
    long water_needed_long;
    long blanket_needed_long;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_area);


        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        progressBar = findViewById(R.id.login_progress);
        progressBar.bringToFront();
        Number1=findViewById(R.id.Number1);
        Number2=findViewById(R.id.Number2);
        Number3=findViewById(R.id.Number3);
//        edit_text_2=findViewById(R.id.edit_text_2);
        water_needed=findViewById(R.id.water);
        blanket_needed=findViewById(R.id.blanket);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);

        if (savedInstanceState == null) {
            Intent extras = this.getIntent();
            if (extras == null) {
                state = null;
                area = null;
//                status = null;
//                water = 0;
//                blanket = 0;
            } else {
                state = extras.getStringExtra("state_name");
                area = extras.getStringExtra("area_name");
//                status = extras.getStringExtra("status");
//                water = extras.getLongExtra("water", 0);
//                blanket = extras.getLongExtra("blanket", 0);
            }
        } else {
            state = (String) savedInstanceState.getSerializable("state_name");
            area = (String) savedInstanceState.getSerializable("state_address");
//            status = (String) savedInstanceState.getSerializable("state_name");
//            water = (String) savedInstanceState.getSerializable("state_name");
//            blanket = (String) savedInstanceState.getSerializable("audio_url");
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch(checkedId){
                    case R.id.radio0:
                        status="Good";
                        // do operations specific to this selection
                        break;
                    case R.id.radio1:
                        status="Average";
                        // do operations specific to this selection
                        break;
                    case R.id.radio2:
                        status="Bad";
                        // do operations specific to this selection
                        break;
                }
            }
        });

   findViewById(R.id.area_upload_button).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        String water_level = Number3.getText().toString().trim();
        String  resource= Number2.getText().toString().trim();
        String  severity= Number1.getText().toString().trim();
        String blanket_needed_string=blanket_needed.getText().toString().trim();
        String water_needed_string=water_needed.getText().toString().trim();


        if(TextUtils.isEmpty(status)){
            Toast.makeText(UpdateAreaActivity.this, "Please select an status type option.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(status.equals("Bad")){
            if (TextUtils.isEmpty(blanket_needed_string)) {
                Toast.makeText(UpdateAreaActivity.this, "Blanket required cannot be empty", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            if (TextUtils.isEmpty(water_needed_string)) {
                Toast.makeText(UpdateAreaActivity.this, "Drinking Water required cannot be empty", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            water_needed_long = Long.parseLong(water_needed_string);
            blanket_needed_long = Long.parseLong(blanket_needed_string);

        }else{
            water_needed_long=0;
            blanket_needed_long=0;
        }

        if (TextUtils.isEmpty(area)) {
            Toast.makeText(UpdateAreaActivity.this, "Placename cannot be empty", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (TextUtils.isEmpty(water_level)) {
            Toast.makeText(UpdateAreaActivity.this, "Water level cannot be empty", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (TextUtils.isEmpty(resource)) {
            Toast.makeText(UpdateAreaActivity.this, "Resource cannot be empty", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (TextUtils.isEmpty(severity)) {
            Toast.makeText(UpdateAreaActivity.this, "Severity cannot be empty", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        updateWaterAndBlanket(blanket_needed_long,water_needed_long,status,water_level,resource,severity);


    }


    private void updateWaterAndBlanket(final long local_blanket, final long water_local, final String status,final String water_level, final String resource ,final String severity) {

        a = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                        for (DataSnapshot areaSnapshot : snapshot.child("area").getChildren()) {
                            String area_name_checking = areaSnapshot.child("area_name").getValue(String.class);

                            if (area_name_checking != null) {
                                if (area_name_checking.equals(area)) {
                                    if(local_blanket==0&&water_local==0){
                                        areaSnapshot.getRef().child("status").setValue("good");
                                    }else{
                                        areaSnapshot.getRef().child("status").setValue(status);
                                    }
                                    areaSnapshot.getRef().child("water_level").setValue(water_level);
                                    areaSnapshot.getRef().child("resource").setValue(resource);
                                    areaSnapshot.getRef().child("severity").setValue(severity);
                                    areaSnapshot.getRef().child("item_bed_sheets").setValue(local_blanket);
                                    areaSnapshot.getRef().child("lastUpdate_date").setValue(java.text.DateFormat.getDateTimeInstance().format(new Date()));
                                    areaSnapshot.getRef().child("item_drinking_water").setValue(water_local, new DatabaseReference.CompletionListener() {
                                        public void onComplete(DatabaseError error, @NonNull DatabaseReference ref) {
                                            Toast.makeText(UpdateAreaActivity.this, "Successfully added", Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                    });
                                }
                            }


                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        ref.child("Malaysia_Area_Information").orderByChild("state").equalTo(state).addListenerForSingleValueEvent(a);


    }

}
