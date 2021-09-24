package com.example.pdrsystem;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

public class AdapterViewActivity extends RecyclerView.Adapter<AdapterViewActivity.MyViewHolder> {

    private Activity activity;
    private ArrayList<State> dataSet;
    private FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
    private DatabaseReference ref= firebaseDatabase.getReference();
    private FirebaseAuth  auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();
    private String currentUserID;
    private ValueEventListener a;


    public static class MyViewHolder extends RecyclerView.ViewHolder {



        TextView state_name;
        RelativeLayout relative;
        Button delete2;






        public MyViewHolder(View itemView) {
            super(itemView);

            this.state_name = (TextView) itemView.findViewById(R.id.state_name);
            this.relative = (RelativeLayout) itemView.findViewById(R.id.relative);
            this.delete2 = (Button) itemView.findViewById(R.id.delete2);





        }
    }

    public AdapterViewActivity(Activity activity,ArrayList<State> data) {
        this.activity = activity;

        this.dataSet = data;

    }

    @Override
    public AdapterViewActivity.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_adapter_view, parent, false);

        AdapterViewActivity.MyViewHolder myViewHolder = new AdapterViewActivity.MyViewHolder(view);
        return myViewHolder;
    }



    @Override
    public void onBindViewHolder(AdapterViewActivity.MyViewHolder holder, final int listPosition) {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (auth.getCurrentUser() != null) {
            currentUserID = auth.getCurrentUser().getUid();
        } else {
            currentUserID = null;
        }
        ref = FirebaseDatabase.getInstance().getReference();


        TextView state_name = holder.state_name;
        RelativeLayout relative = holder.relative;
        Button delete2 = holder.delete2;



        //set value into textview
        state_name.setText(dataSet.get(listPosition).getState());

        final Intent intent=new Intent(activity, AreaActivity.class);;

        relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new Intent(activity, BookingActivity.class);
                intent.putExtra("state_name", dataSet.get(listPosition).getState());
                activity.startActivity(intent);


            }
        });

        delete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Are you sure you want to delete this state?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                a = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    if (snapshot.child("state").getValue(String.class).equals(dataSet.get(listPosition).getState())) {
                                                        snapshot.getRef().removeValue();
                                                    }


                                            }



                                        }
                                    }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                };
                                ref.child("Malaysia_Area_Information").orderByChild("state").equalTo(dataSet.get(listPosition).getState()).addListenerForSingleValueEvent(a);
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
