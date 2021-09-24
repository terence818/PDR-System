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
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<AffectedArea> dataSet;
    private Activity activity;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView area_name;
        TextView state_name;
        TextView water;
        TextView blanket;
        TextView status;
        CardView card_view;
        ImageView image_view;
        TableLayout table;
        View view;


//        ImageView imageViewIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.area_name = itemView.findViewById(R.id.area_name);
            this.card_view = itemView.findViewById(R.id.card_view);
            this.state_name = itemView.findViewById(R.id.state_name);
            this.image_view = itemView.findViewById(R.id.image_view);
            this.status = itemView.findViewById(R.id.status);
            this.water = itemView.findViewById(R.id.water);
            this.table = itemView.findViewById(R.id.table);
            this.view = itemView.findViewById(R.id.view);
            this.blanket = itemView.findViewById(R.id.blanket);
        }
    }

    public CustomAdapter(Activity activity, ArrayList<AffectedArea> data) {
        this.dataSet = data;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.afected_area_layout, parent, false);

//        view.setOnClickListener(ScheduleActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {


        TextView area_name = holder.area_name;
        TextView state_name = holder.state_name;
        TextView water = holder.water;
        TextView blanket = holder.blanket;
        ImageView image_view = holder.image_view;
        CardView card_view = holder.card_view;
        TextView status = holder.status;
        TableLayout table = holder.table;
        View view = holder.view;


        if (dataSet.get(listPosition).getStatus().equals("Bad")) {
            image_view.setImageResource(R.drawable.need_help);
            status.setText(R.string.poor);
            table.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
            water.setText(String.valueOf(dataSet.get(listPosition).getItem_drinking_water()));
            blanket.setText(String.valueOf(dataSet.get(listPosition).getItem_bed_sheets()));

        } else {
            status.setText(R.string.good);
            image_view.setImageResource(R.drawable.completed);
            table.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }//
//
//        //set all value into textview
//        final String full_name=dataSet.get(listPosition).getFirst_name()+" "+dataSet.get(listPosition).getLast_name();
        area_name.setText(dataSet.get(listPosition).getArea_name());
        state_name.setText(dataSet.get(listPosition).getState_name());

        water.setText(String.valueOf(dataSet.get(listPosition).getItem_drinking_water()));
        blanket.setText(String.valueOf(dataSet.get(listPosition).getItem_bed_sheets()));

//
//        String user_type=Homepage.getMyString();
//        if(user_type.equals("client")){
//            detail.setEnabled(true);
//            detail.setVisibility(View.VISIBLE);
//        }else{
//            detail.setEnabled(false);
//            detail.setVisibility(View.INVISIBLE);
//        }
//        //send coach info into next activity and go into booking activity
        card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DonateActivity.class);
                intent.putExtra("state_name",dataSet.get(listPosition).getState_name());
                intent.putExtra("area_name", dataSet.get(listPosition).getArea_name());
                intent.putExtra("status",dataSet.get(listPosition).getStatus());
                intent.putExtra("water", dataSet.get(listPosition).getItem_drinking_water());
                intent.putExtra("blanket",dataSet.get(listPosition).getItem_bed_sheets());
                activity.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

//
}
