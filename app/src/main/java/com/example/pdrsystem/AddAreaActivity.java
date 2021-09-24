package com.example.pdrsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AddAreaActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    EditText editText,edit_text_2,textView1,Number1,Number2,Number3, water_needed, blanket_needed;

    private GoogleApiClient mClient;
    private ProgressBar progressBar;
    private RadioGroup radioGroup, radioGroup2;
    private ValueEventListener a;

    private DatabaseReference databaseReference;
    private DatabaseReference logReference;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private String currentUserID;
    private int  PERMISSIONS_REQUEST_FINE_LOCATION=1;
    private Activity that=this;

    String mStringLatitude;
    String mStringLongitude;
    String Address;
    String Placename;
    String status;
    String water_level;
    String resource;
    String severity;
    String state;
    String shelter;
    long water_needed_long;
    long blanket_needed_long;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__area_);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                state= null;
            } else {
                state= extras.getString("state_name");
            }
        } else {
            state= (String) savedInstanceState.getSerializable("state_name");
        }



        editText=findViewById(R.id.edit_address);
        textView1=findViewById(R.id.edit_text_1);
        Number1=findViewById(R.id.Number1);
        Number2=findViewById(R.id.Number2);
        Number3=findViewById(R.id.Number3);
//        edit_text_2=findViewById(R.id.edit_text_2);
        water_needed=findViewById(R.id.water);
        blanket_needed=findViewById(R.id.blanket);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
        radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);


        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        progressBar = findViewById(R.id.login_progress);
        progressBar.bringToFront();

        if (auth.getCurrentUser() != null) {
            currentUserID = auth.getCurrentUser().getUid();
        } else {
            currentUserID = null;
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //Initialize places
        Places.initialize(getApplicationContext(),"AIzaSyCcgAmTUTcrnaQb5BCFGr8lW0YwIIOjlMM");

        ActivityCompat.requestPermissions(AddAreaActivity.this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_FINE_LOCATION);

//        CheckBox ringerPermissions = (CheckBox) findViewById(R.id.ringer_permissions_checkbox);
//        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        // Check if the API supports such permission change and check if permission is granted
//        if (Build.VERSION.SDK_INT >= 24 && !nm.isNotificationPolicyAccessGranted()) {
//            ringerPermissions.setChecked(false);
//        } else {
//            ringerPermissions.setChecked(true);
//            ringerPermissions.setEnabled(false);
//        }


        //Set EditText non focusable
        editText.setFocusable(false);
        editText.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //Initialize place field list
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,
                        Place.Field.NAME);
                //Create intent
                Intent intent =new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                        fieldList).build(AddAreaActivity.this);
                //Start activity result
                startActivityForResult(intent,100);

            }
        });

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


        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch(checkedId){
                    case R.id.yes:
                        shelter="yes";
                        // do operations specific to this selection
                        break;
                    case R.id.no:
                        shelter="no";
                        // do operations specific to this selection
                        break;
//                    case R.id.radio2:
//                        status="Bad";
//                        // do operations specific to this selection
//                        break;
                }
            }
        });







        Button upload = findViewById(R.id.area_upload_button);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setArea();

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onRingerPermissionsClicked(View view) {
        Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK) {
            //When success
            //Initialize place
            Place place = Autocomplete.getPlaceFromIntent(data);
            //Set address on EditText

            editText.setText(place.getAddress());
            //Set locality name
            textView1.setText(String.format(place.getName()));
            //Set latitude & longtitude
            LatLng latLng = place.getLatLng() ;
            mStringLatitude = String.valueOf(latLng.latitude);
            mStringLongitude = String.valueOf(latLng.longitude);


            Address = editText.getText().toString();
            Placename = textView1.getText().toString();







        } else if(resultCode == AutocompleteActivity.RESULT_ERROR) {
            //When error
            //Initialize status
            Status status = Autocomplete.getStatusFromIntent(data);
            //Display toast
            Toast.makeText(getApplicationContext(),"Please choose a location.", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onConnected(@Nullable Bundle connectionHint) {


    }

    /***
     * Called when the Google API Client is suspended
     *
     @param cause cause The reason for the disconnection. Defined by constants CAUSE_.
     */
    @Override
    public void onConnectionSuspended(int cause) {

    }

    /***
     * Called when the Google API Client failed to connect to Google Play Services
     *
     * @param result A ConnectionResult that can be used for resolving the error
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {


    }

    public void setArea() {

        water_level = Number3.getText().toString().trim();
        resource= Number2.getText().toString().trim();
        severity= Number1.getText().toString().trim();
        String blanket_needed_string=blanket_needed.getText().toString().trim();
        String water_needed_string=water_needed.getText().toString().trim();

        if (TextUtils.isEmpty(Address)) {
            Toast.makeText(AddAreaActivity.this, "Address cannot be empty", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (TextUtils.isEmpty(shelter)) {
            Toast.makeText(AddAreaActivity.this, "Shelter cannot be empty", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if(status.equals("Bad")){
            if (TextUtils.isEmpty(blanket_needed_string)) {
                Toast.makeText(AddAreaActivity.this, "Blanket required cannot be empty", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            if (TextUtils.isEmpty(water_needed_string)) {
                Toast.makeText(AddAreaActivity.this, "Drinking Water required cannot be empty", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            water_needed_long = Long.parseLong(water_needed_string);
            blanket_needed_long = Long.parseLong(blanket_needed_string);

        }else{
            water_needed_long=0;
            blanket_needed_long=0;
        }

        if (TextUtils.isEmpty(Placename)) {
            Toast.makeText(AddAreaActivity.this, "Placename cannot be empty", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (TextUtils.isEmpty(water_level)) {
            Toast.makeText(AddAreaActivity.this, "Water level cannot be empty", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (TextUtils.isEmpty(resource)) {
            Toast.makeText(AddAreaActivity.this, "Resource cannot be empty", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (TextUtils.isEmpty(severity)) {
            Toast.makeText(AddAreaActivity.this, "Severity cannot be empty", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if(status!=null) {
            progressBar.setVisibility(View.VISIBLE);

//             AffectedArea report = new AffectedArea();
//            report.setLatitude(mStringLatitude);
//            report.setLongitude(mStringLongitude);
//            report.setWater_level(water_level);
//            report.setResource(resource);
//            report.setSeverity(severity);
//            report.setStatus(status);
//            report.setShelter(shelter);
//            report.setArea_name(Placename);
//            report.setAddress(Address);

            a = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        progressBar.setVisibility(View.GONE);
                        AffectedArea report = new AffectedArea();
                        report.setLatitude(mStringLatitude);
                        report.setLongitude(mStringLongitude);
                        report.setWater_level(water_level);
                        report.setResource(resource);
                        report.setSeverity(severity);
                        report.setStatus(status);
                        report.setShelter(shelter);
                        report.setArea_name(Placename);
                        report.setAddress(Address);
                        report.setState_name(state);
                        report.setItem_drinking_water(water_needed_long);
                        report.setItem_bed_sheets(blanket_needed_long);
                        report.setCreated_date(java.text.DateFormat.getDateTimeInstance().format(new Date()));
                        report.setLastUpdate_date(java.text.DateFormat.getDateTimeInstance().format(new Date()));
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
                            snapshot.getRef().child("area").push().setValue(report, new DatabaseReference.CompletionListener() {
                                public void onComplete(DatabaseError error, @NonNull DatabaseReference ref) {
                                    Toast.makeText(AddAreaActivity.this, "Successfully added", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });


                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };

                databaseReference.child("Malaysia_Area_Information").orderByChild("state").equalTo(state).addListenerForSingleValueEvent(a);
        }

         else {

            Toast.makeText(AddAreaActivity.this, "Please select a status option.", Toast.LENGTH_SHORT).show();
        }


    }


}