package com.example.myapplication.Schedule;


import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;



public class SchedAdapter extends RecyclerView.Adapter<SchedAdapter.MyViewHolder> {

    private ArrayList<SchedModel> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewHours;
        TextView textViewGroup;
        TextView textViewCourse;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewHours = (TextView) itemView.findViewById(R.id.sched_hours);
            this.textViewGroup = (TextView) itemView.findViewById(R.id.sched_group);
            this.textViewCourse = (TextView) itemView.findViewById(R.id.sched_course);
        }
    }

    public SchedAdapter(ArrayList<SchedModel> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_card, parent, false);

//        view.setOnClickListener(MainActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewHours = holder.textViewHours;
        TextView textViewGroup = holder.textViewGroup;
        TextView textViewCourse = holder.textViewCourse;

        textViewHours.setText(dataSet.get(listPosition).getName());
        textViewGroup.setText(dataSet.get(listPosition).getVersion());
        textViewCourse.setText(dataSet.get(listPosition).getVersion());
//        imageView.setImageResource(dataSet.get(listPosition).getImage());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
