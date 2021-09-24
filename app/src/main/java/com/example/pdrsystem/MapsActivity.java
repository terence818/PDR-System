package com.example.pdrsystem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ref;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ProgressBar water_level, resource, severity;
    private TextView water_percent, resource_percent, severity_percent;
    private static ArrayList<AffectedArea> data;
    float zoomLevel = 10.0f; //This goes up to 21
    float zoomLevel_city = 16.0f; //This goes up to 21
    private ValueEventListener a;
    private Spinner spinner_city;
    private ArrayAdapter spinnerArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        water_level = findViewById(R.id.water_level);
        resource = findViewById(R.id.resource_available);
        severity = findViewById(R.id.severity_level);

        water_percent = findViewById(R.id.water_percentage);
        resource_percent = findViewById(R.id.resource_percentage);
        severity_percent = findViewById(R.id.severity_percentage);

        data = new ArrayList<AffectedArea>();


        Spinner spinner = (Spinner) findViewById(R.id.state_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinner_adapter = ArrayAdapter.createFromResource(this,
                R.array.state, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(spinner_adapter);


        spinner_city = (Spinner)this.findViewById(R.id.city_spinner);

        // Step 2: Create and fill an ArrayAdapter with a bunch of "State" objects
         spinnerArrayAdapter = new ArrayAdapter<AffectedArea>(this, android.R.layout.simple_spinner_item,data
        );

        // Step 3: Tell the spinner about our adapter
        spinner_city.setAdapter(spinnerArrayAdapter);


        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AffectedArea st = (AffectedArea)spinner_city.getSelectedItem();
                updateCityInfo(st);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int i, long id) {
                String selectedItemText = (String) parentView.getItemAtPosition(i);
                data.clear();
                ref.child("Malaysia_Area_Information").orderByChild("state").equalTo(selectedItemText).addListenerForSingleValueEvent(a);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        a = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        if(mMap!=null){
                            mMap.clear();
                        }

                        if (snapshot.child("resource_available").getValue(Integer.class) != null) {
                            try {
                                int i = snapshot.child("resource_available").getValue(Integer.class);
                                water_level.setProgress(i);
                                water_percent.setText(String.valueOf(i) + "%");
                            } catch (NullPointerException e) {
                                water_level.setProgress(0);
                            }
                        } else {
                            water_percent.setText("0%");
                            water_level.setProgress(0);
                        }

                        if (snapshot.child("severity").getValue(Integer.class) != null) {
                            try {
                                int i = snapshot.child("severity").getValue(Integer.class);
                                resource.setProgress(i);
                                resource_percent.setText(String.valueOf(i) + "%");
                            } catch (NullPointerException e) {
                                resource.setProgress(0);
                            }
                        } else {
                            resource_percent.setText("0%");
                            resource.setProgress(0);
                        }


                        if (snapshot.child("water_level").getValue(Integer.class) != null) {
                            try {
                                int i = snapshot.child("water_level").getValue(Integer.class);
                                severity.setProgress(i);
                                severity_percent.setText(String.valueOf(i) + "%");
                            } catch (NullPointerException e) {
                                severity.setProgress(0);
                            }
                        } else {
                            severity_percent.setText("0%");
                            severity.setProgress(0);
                        }


                        for (DataSnapshot areaSnapshot : snapshot.child("area").getChildren()) {
                            AffectedArea key_Detail = areaSnapshot.getValue(AffectedArea.class);
                            data.add(key_Detail);
                        }

                        spinnerArrayAdapter.notifyDataSetChanged();
                        if(data.size()>0){
                            updateMapMarker();
                        }

                        if(data.size()>0){
                            updateCityInfo(data.get(0));
                        }

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        MarkerOptions marker1 = new MarkerOptions().position(sydney).title("Hello Maps");

//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));
//        marker1.icon(BitmapDescriptorFactory.fromResource(R.drawable.sunny));
//        mMap.addMarker(marker1);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));
//        marker.setIcon('newImage.png')
    }

    public void updateCityInfo(AffectedArea area) {
        Log.i("hedqwdqw",area.toString());
        LatLng location = new LatLng(Double.parseDouble(area.getLatitude()), Double.parseDouble(area.getLongitude()));

        if (area.getWater_level() != null) {
            try {
                int i =Integer.parseInt(area.getWater_level());
                water_level.setProgress(i);
                water_percent.setText(String.valueOf(i) + "%");
            } catch (NullPointerException e) {
                water_level.setProgress(0);
            }
        } else {
            water_percent.setText("0%");
            water_level.setProgress(0);
        }
        if (area.getResource() != null) {
            try {
                int i =Integer.parseInt(area.getResource());
                resource.setProgress(i);
                resource_percent.setText(String.valueOf(i) + "%");
            } catch (NullPointerException e) {
                resource.setProgress(0);
            }
        } else {
            resource_percent.setText("0%");
            resource.setProgress(0);
        }


        if (area.getSeverity() != null) {
            try {
                int i =Integer.parseInt(area.getSeverity());
                severity.setProgress(i);
                severity_percent.setText(String.valueOf(i) + "%");
            } catch (NullPointerException e) {
                severity.setProgress(0);
            }
        } else {
            severity_percent.setText("0%");
            severity.setProgress(0);
        }

        if( mMap!=null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel_city));
        }

    }

    public void updateMapMarker() {

//        boolean camera_position=false;
        LatLng shelter_location=null;
        if (mMap != null) {
            boolean camera_position=false;
            for (int counter = 0; counter < data.size(); counter++) {
                LatLng location = new LatLng(Double.parseDouble(data.get(counter).getLatitude()), Double.parseDouble(data.get(counter).getLongitude()));
                MarkerOptions marker_local = new MarkerOptions().position(location).title(data.get(counter).getArea_name());

//                Log.i("lllll",location.toString());
                if(data.get(counter).getShelter().equals("yes")){
                    marker_local.icon(BitmapDescriptorFactory.fromResource(R.drawable.charity));
                    shelter_location=location;
                    camera_position=true;
                }
                else{
                    if(!camera_position){
                        shelter_location=location;
                    }
                    if(data.get(counter).getStatus().equals("Bad")){
                        marker_local.icon(BitmapDescriptorFactory.fromResource(R.drawable.disaster));
                    }else {
                        marker_local.icon(BitmapDescriptorFactory.fromResource(R.drawable.sunny));
                    }
                }
//
                mMap.addMarker(marker_local);

                if(data.get(counter).getArea_name().equals("shelter")){
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));
                }
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shelter_location, zoomLevel));

        } else {
            Toast.makeText(getApplicationContext(), "Map not ready", Toast.LENGTH_LONG).show();
        }

    }
}
