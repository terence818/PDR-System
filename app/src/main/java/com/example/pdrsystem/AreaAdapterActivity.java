package com.example.pdrsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

public class AreaAdapterActivity extends RecyclerView.Adapter<AreaAdapterActivity.MyViewHolder> {

    private Activity activity;
    private ArrayList<AffectedArea> dataSet;
    private Location Location_cur;
    private int show_button = 1;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference ref = firebaseDatabase.getReference();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();
    private String currentUserID;
    private ValueEventListener a;




    public static class MyViewHolder extends RecyclerView.ViewHolder {


        TextView state_name;
        TextView state_address;

        RelativeLayout relative;

        Button delete;

        Button update;


        public MyViewHolder(View itemView) {
            super(itemView);

            this.state_name = (TextView) itemView.findViewById(R.id.state_name);
            this.state_address = (TextView) itemView.findViewById(R.id.state_address);

            this.relative = (RelativeLayout) itemView.findViewById(R.id.relative);
            this.delete = (Button) itemView.findViewById(R.id.delete);
            this.update = (Button) itemView.findViewById(R.id.update);


        }
    }

    public AreaAdapterActivity(Activity activity, ArrayList<AffectedArea> data) {
        this.activity = activity;
        this.dataSet = data;

    }

    @Override
    public AreaAdapterActivity.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_area_adapter, parent, false);

        AreaAdapterActivity.MyViewHolder myViewHolder = new AreaAdapterActivity.MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(AreaAdapterActivity.MyViewHolder holder, final int listPosition) {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (auth.getCurrentUser() != null) {
            currentUserID = auth.getCurrentUser().getUid();
        } else {
            currentUserID = null;
        }
        ref = FirebaseDatabase.getInstance().getReference();


        TextView state_name = holder.state_name;
        TextView state_address = holder.state_address;

        RelativeLayout relative = holder.relative;
        Button delete = holder.delete;
        Button update = holder.update;


        //set value into textview
        state_name.setText(dataSet.get(listPosition).getArea_name());
        state_address.setText(String.valueOf(dataSet.get(listPosition).getAddress()));


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, UpdateAreaActivity.class);
                intent.putExtra("state_name",dataSet.get(listPosition).getState_name());
                intent.putExtra("area_name", dataSet.get(listPosition).getArea_name());
//                intent.putExtra("status",dataSet.get(listPosition).getStatus());
//                intent.putExtra("water", dataSet.get(listPosition).getItem_drinking_water());
//                intent.putExtra("blanket",dataSet.get(listPosition).getItem_bed_sheets());
                activity.startActivity(intent);
            }

        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Are you sure you want to cancel this request?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                a = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                for (DataSnapshot areaSnapshot : snapshot.child("area").getChildren()) {
                                                    if(areaSnapshot.child("address").getValue(String.class).equals(dataSet.get(listPosition).getAddress())){
                                                        areaSnapshot.getRef().removeValue();
                                                    }

                                                }


                                            }

                                        }
                                    }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                };
                                ref.child("Malaysia_Area_Information").orderByChild("state").equalTo(dataSet.get(listPosition).getState_name()).addListenerForSingleValueEvent(a);
                            }

                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }

            ;
        });

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}


