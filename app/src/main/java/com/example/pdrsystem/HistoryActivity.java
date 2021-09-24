package com.example.pdrsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<History> data;
    private static ArrayList<History> data2;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;

    private ValueEventListener a;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ref;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private EditText filter_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_actvity);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        filter_key = findViewById(R.id.filter_key);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<History>();
        data2 = new ArrayList<History>();

        adapter = new HistoryAdapter(this, data);
        recyclerView.setAdapter(adapter);

        a = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                data2.clear();
//                Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        History key_Detail = snapshot.getValue(History.class);
                        data.add(key_Detail);
                        data2.add(key_Detail);
                    }

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        ref.child(user.getUid()).child("history").addListenerForSingleValueEvent(a);


        filter_key.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                String queary = s.toString();


                if (data2.size() > 0 && !queary.equals("")) {
                    data.clear();
                    for (int i = 0; i < data2.size(); i++) {
                        if (data2.get(i).getApprover().toLowerCase().contains(queary.toLowerCase())) {
                            data.add(data2.get(i));
                        }
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    data.clear();
                    data.addAll(data2);
                    adapter.notifyDataSetChanged();
                }
//                ref.child(user.getUid()).child("history")
//                        .orderByChild("approver")
//                        .startAt(queary)
//                        .endAt(queary + "\uf8ff")
//                        .addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                data.clear();
//                                if (dataSnapshot.exists()) {
//                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                        History key_Detail = snapshot.getValue(History.class);
//                                        data.add(key_Detail);
//                                    }
//
//                                }
//
//                                adapter.notifyDataSetChanged();
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//                            }
//                        });
//                // you can call or do what you want with your EditText here
//
//                // yourEditText...
            }


            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }
}
