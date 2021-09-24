package com.example.pdrsystem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private ArrayList<History> dataSet;
    private Activity activity;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView state_area_name;
        TextView username;
        TextView datetime;
        TextView status;
        TextView water_num;
        TextView bed_num;
        ImageView image_view;
        TextView mob_app;


//        ImageView imageViewIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.state_area_name = itemView.findViewById(R.id.state_area_name);
            this.datetime = itemView.findViewById(R.id.datetime_name);
            this.username = itemView.findViewById(R.id.user_name);
            this.status = itemView.findViewById(R.id.status);
            this.water_num = itemView.findViewById(R.id.text_water);
            this.bed_num = itemView.findViewById(R.id.text_bed);
            this.image_view = itemView.findViewById(R.id.image_view);
            this.mob_app = itemView.findViewById(R.id.mob_app);
        }
    }

    public HistoryAdapter(Activity activity, ArrayList<History> data) {
        this.dataSet = data;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_recycle_layout, parent, false);

//        view.setOnClickListener(ScheduleActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {


        TextView state_area_name = holder.state_area_name;
        TextView datetime = holder.datetime;
        TextView username = holder.username;
        TextView status = holder.status;
        TextView water_num = holder.water_num;
        TextView bed_num = holder.bed_num;
        ImageView image_view= holder.image_view;
        TextView mob_app = holder.mob_app;



//        ImageView profile=holder.profile_pic;
//        RatingBar rating=holder.rating;

//        if(dataSet.get(listPosition).getStatus().equals("danger")){
////            card_view.setCardBackgroundColor(Color.RED);
////        }else{
////            card_view.setCardBackgroundColor(Color.GREEN);
////        }//
//
//        //set all value into textview
//        final String full_name=dataSet.get(listPosition).getFirst_name()+" "+dataSet.get(listPosition).getLast_name();

        String state_area_name_string=dataSet.get(listPosition).getArea()+","+dataSet.get(listPosition).getState();
        state_area_name.setText(state_area_name_string);
        datetime.setText(dataSet.get(listPosition).getCreated_date());
        username.setText("Approved by: "+dataSet.get(listPosition).getApprover());
        mob_app.setText("Mobile number: "+dataSet.get(listPosition).getMobile());
//        status.setText(dataSet.get(listPosition).getStatus());

        water_num.setText(dataSet.get(listPosition).getWater());
        bed_num.setText(dataSet.get(listPosition).getBlanket());
//



    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

//
}
