package com.example.pdrsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.Date;

public class DonateActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ref;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private ValueEventListener a;

    private TextView text_state_area, text_status, text_water, text_blanket, water_added, blanket_added;
    private EditText apporver;
    private EditText mob_app;
    private ImageView image_status;
    private String state, status, area;
    private long water, blanket;
    private CardView card_water, card_blacket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        text_state_area = findViewById(R.id.state_area);
        text_status = findViewById(R.id.text_status);
        text_water = findViewById(R.id.text_water);
        text_blanket = findViewById(R.id.text_bed);
        apporver = findViewById(R.id.apporver);
        mob_app = findViewById(R.id.mob_app);
        image_status = findViewById(R.id.image_status);
        card_water = findViewById(R.id.card_water);
        card_blacket = findViewById(R.id.card_blanket);
        water_added = findViewById(R.id.water_added);
        blanket_added = findViewById(R.id.blanket_added);
        blanket_added.setSelected(true);


        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (savedInstanceState == null) {
            Intent extras = this.getIntent();
            if (extras == null) {
                state = null;
                area = null;
                status = null;
                water = 0;
                blanket = 0;
            } else {
                state = extras.getStringExtra("state_name");
                area = extras.getStringExtra("area_name");
                status = extras.getStringExtra("status");
                water = extras.getLongExtra("water", 0);
                blanket = extras.getLongExtra("blanket", 0);
            }
        } else {
            state = (String) savedInstanceState.getSerializable("state_name");
            area = (String) savedInstanceState.getSerializable("state_address");
            status = (String) savedInstanceState.getSerializable("state_name");
//            water = (String) savedInstanceState.getSerializable("state_name");
//            blanket = (String) savedInstanceState.getSerializable("audio_url");
        }

        String state_area = area + "," + state;
        text_state_area.setText(state_area);
        text_water.setText(String.valueOf(water));
        text_blanket.setText(String.valueOf(blanket));

        if (status.equals("Bad")) {
            image_status.setImageResource(R.drawable.need_help);
            text_status.setText(R.string.poor);
        } else {
            image_status.setImageResource(R.drawable.completed);
            text_status.setText(R.string.good);
        }

        card_blacket.setOnClickListener(this);
        card_water.setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.donate).setOnClickListener(this);
    }

    private void createDialog(String type, final TextView textview) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final EditText edittext = new EditText(this);


        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
        edittext.setRawInputType(Configuration.KEYBOARD_12KEY);


        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 0, 20, 0);


        layout.addView(edittext, params);
        alert.setTitle(type);
        alert.setView(layout);

        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
//                Editable YouEditTextValue = edittext.getText();
                //OR
                String YouEditTextValue = edittext.getText().toString();
                textview.setText(YouEditTextValue);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_blanket:
                createDialog("Bed Sheet", blanket_added);
                break;
            case R.id.card_water:
                createDialog("Drinking Water", water_added);
                break;
            case R.id.cancel:
                finish();
                break;
            case R.id.donate:


                if (state.equals("") || area.equals("")) {
                    return;
                }


                String water_added_string, blanket_added_string;

                try {
                    long i = Long.parseLong(water_added.getText().toString());
                    water_added_string = water_added.getText().toString();
                } catch (NumberFormatException e) {
                    water_added_string = "0";
                }

                try {
                    long i = Long.parseLong(blanket_added.getText().toString());
                    blanket_added_string = blanket_added.getText().toString();
                } catch (NumberFormatException e) {
                    blanket_added_string = "0";
                }


                if (water_added_string.equals("0") && blanket_added_string.equals("0")) {
                    Toast.makeText(getApplicationContext(), "Please enter the donation", Toast.LENGTH_SHORT).show();
                    return;
                }

                long total_water, total_blanket;
                try {
                    total_water = water - Long.parseLong(water_added_string);
                    total_blanket = blanket - Long.parseLong(blanket_added_string);

                    if(total_water<=0){
                        total_water=0;
                    }

                    if(total_blanket<=0){
                        total_blanket=0;
                    }


                } catch (NumberFormatException e) {
                    total_water = water;
                    total_blanket = blanket;
                }

                if(!status.equals("Bad")){
                    Toast.makeText(getApplicationContext(), "This Area does not need donation" , Toast.LENGTH_SHORT).show();
                    return;
                }


                String approver_string = apporver.getText().toString();
                if (TextUtils.isEmpty(approver_string)) {
                    Toast.makeText(getApplicationContext(), "Approved by cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                String approver_mob = mob_app.getText().toString();
                if (TextUtils.isEmpty(approver_mob)) {
                    Toast.makeText(getApplicationContext(), "Approved mobile by cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }


                History history = new History();
                history.setMobile(approver_mob);
                history.setApprover(approver_string);
                history.setArea(area);
                history.setState(state);
                history.setWater(water_added_string);
                history.setBlanket(blanket_added_string);
                history.setCreated_date(java.text.DateFormat.getDateTimeInstance().format(new Date()));

                final long Ftotal_blanket = total_blanket;
                final long Ftotal_water = total_water;
                if (user != null) {
                    ref.child(user.getUid()).child("history").push().setValue(history, new DatabaseReference.CompletionListener() {
                        public void onComplete(DatabaseError error, @NonNull DatabaseReference ref) {
                            updateWaterAndBlanket(Ftotal_blanket, Ftotal_water);

                        }
                    });

                }

                break;

        }
    }

    private void updateWaterAndBlanket(final long local_blanket, final long water_local) {

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
                                    }
                                    areaSnapshot.getRef().child("item_bed_sheets").setValue(local_blanket);
                                    areaSnapshot.getRef().child("item_drinking_water").setValue(water_local, new DatabaseReference.CompletionListener() {
                                        public void onComplete(DatabaseError error, @NonNull DatabaseReference ref) {
                                            Toast.makeText(DonateActivity.this, "Successfully added", Toast.LENGTH_LONG).show();
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




