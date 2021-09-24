package com.example.pdrsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminHistoryActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<AffectedArea> data, pending;
    private static ArrayList<AffectedArea> data2;
    private Intent intent;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ref;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ValueEventListener a;
    private EditText filter_key;
    String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_history);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        filter_key = findViewById(R.id.filter_key);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(AdminHistoryActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<AffectedArea>();
        data2 = new ArrayList<AffectedArea>();

        adapter = new AdminHistoryAdapter(this,data);

        recyclerView.setAdapter(adapter);


        a = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
                data.clear();
                data2.clear();
                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                        for (DataSnapshot areaSnapshot : snapshot.child("area").getChildren()) {
                            AffectedArea key_Detail = areaSnapshot.getValue(AffectedArea.class);
                            data.add(key_Detail);
                            data2.add(key_Detail);
                        }

                    }

                    adapter.notifyDataSetChanged();
                }
            }




            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        ref.child("Malaysia_Area_Information").addValueEventListener(a);


        filter_key.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                String queary = s.toString();


                if (data2.size() > 0 && !queary.equals("")) {
                    data.clear();
                    for (int i = 0; i < data2.size(); i++) {
                        if (data2.get(i).getArea_name().toLowerCase().contains(queary.toLowerCase())
                                ||data2.get(i).getState_name().toLowerCase().contains(queary.toLowerCase())) {
                            data.add(data2.get(i));
                        }
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    data.clear();
                    data.addAll(data2);
                    adapter.notifyDataSetChanged();
                }
            }


            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

    }

}
