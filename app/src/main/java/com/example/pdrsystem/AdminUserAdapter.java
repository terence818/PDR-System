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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.MyViewHolder> {

    private Activity activity;
    private ArrayList<User> dataSet;
    private Location Location_cur;
    private int show_button = 1;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference ref = firebaseDatabase.getReference();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();
    private String currentUserID;
    private ValueEventListener a;
    private ArrayList<String> key;


    public static class MyViewHolder extends RecyclerView.ViewHolder {


        TextView u_name;
        TextView e_mail;
        TextView mob_num;
        CardView card_view;


        public MyViewHolder(View itemView) {
            super(itemView);

            this.u_name = (TextView) itemView.findViewById(R.id.u_name);
            this.e_mail = (TextView) itemView.findViewById(R.id.e_mail);
            this.mob_num = (TextView) itemView.findViewById(R.id.mob_num);
            this.card_view = (CardView) itemView.findViewById(R.id.card_view);


        }
    }

    public AdminUserAdapter(Activity activity, ArrayList<User> data, ArrayList<String>keys) {
        this.activity = activity;
        this.dataSet = data;
        this.key = keys;

    }

    @Override
    public AdminUserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_user_recycle_layout, parent, false);

        AdminUserAdapter.MyViewHolder myViewHolder = new AdminUserAdapter.MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(AdminUserAdapter.MyViewHolder holder, final int listPosition) {


        TextView u_name = holder.u_name;
        TextView e_mail = holder.e_mail;
        TextView mob_num = holder.mob_num;
        CardView card_view = holder.card_view;

        //set value into textview
        String u_name_string = dataSet.get(listPosition).getUsername() + "," + dataSet.get(listPosition).getUser_type();
        u_name.setText(u_name_string);

        if (dataSet.get(listPosition).getEmail() != null) {
            e_mail.setText(String.valueOf(dataSet.get(listPosition).getEmail()));
        } else {
            e_mail.setText("Not found");
        }

        if (dataSet.get(listPosition).getPhone_number() != null) {
            mob_num.setText(String.valueOf(dataSet.get(listPosition).getPhone_number()));
        } else {
            mob_num.setText("Not found");
        }

        final Intent intent = new Intent(activity, UpdateUserInformationActivity.class);
        ;
        card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new Intent(activity, BookingActivity.class);
                intent.putExtra("username", dataSet.get(listPosition).getUsername());
                intent.putExtra("phone", dataSet.get(listPosition).getPhone_number());
                intent.putExtra("user_type", dataSet.get(listPosition).getUser_type());
                intent.putExtra("email", dataSet.get(listPosition).getEmail());
                intent.putExtra("key", key.get(listPosition));
                activity.startActivity(intent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}