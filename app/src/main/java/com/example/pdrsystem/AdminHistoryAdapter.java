package com.example.pdrsystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminHistoryAdapter extends RecyclerView.Adapter<AdminHistoryAdapter.MyViewHolder> {

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
        TextView created_date;
        TextView latest_date;



        public MyViewHolder(View itemView) {
            super(itemView);

            this.state_name = (TextView) itemView.findViewById(R.id.state_area_name);
            this.created_date = (TextView) itemView.findViewById(R.id.created_date);
            this.latest_date = (TextView) itemView.findViewById(R.id.latest_date);



        }
    }

    public AdminHistoryAdapter(Activity activity, ArrayList<AffectedArea> data) {
        this.activity = activity;
        this.dataSet = data;

    }

    @Override
    public AdminHistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_history_recycle_layout, parent, false);

        AdminHistoryAdapter.MyViewHolder myViewHolder = new AdminHistoryAdapter.MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(AdminHistoryAdapter.MyViewHolder holder, final int listPosition) {


        TextView state_name = holder.state_name;
        TextView created_date = holder.created_date;
        TextView latest_date = holder.latest_date;


        //set value into textview
        String state_area_name_string=dataSet.get(listPosition).getArea_name()+","+dataSet.get(listPosition).getState_name();
        state_name.setText(state_area_name_string);

        if(dataSet.get(listPosition).getCreated_date()!=null){
            created_date.setText(String.valueOf(dataSet.get(listPosition).getCreated_date()));
        }else{
            created_date.setText("Not found");
        }

        if(dataSet.get(listPosition).getLastUpdate_date()!=null){
            latest_date.setText(String.valueOf(dataSet.get(listPosition).getLastUpdate_date()));
        }else{
            latest_date.setText("Not found");
        }





    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}