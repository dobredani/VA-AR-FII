package com.example.myapplication.Schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.Schedule.SchedAdapter;
import com.example.myapplication.Schedule.SchedData;
import com.example.myapplication.Schedule.SchedModel;
import com.example.myapplication.problem.Lecture;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SchedViewFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<SchedModel> lectures;

    public SchedViewFragment() { /* empty contructor */ }

    public SchedViewFragment(List<SchedModel> lectures) {
        this.lectures = (ArrayList) lectures;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calendar_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerCalendar);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

//        ArrayList<SchedModel> data = new ArrayList<>();
//        for (int xi = 0; xi < SchedData.nameArray.length; xi++) {
//            data.add(new SchedModel(SchedData.nameArray[xi], SchedData.versionArray[xi], SchedData.versionArray[xi], SchedData.versionArray[xi]));
//        }

        adapter = new SchedAdapter(lectures);
        recyclerView.setAdapter(adapter);

    }

//    public void addData(ArrayList<SchedModel> data) {
//        for (int xi = 0; xi < SchedData.nameArray.length; xi++) {
//            data.add(new SchedModel(SchedData.nameArray[xi], SchedData.versionArray[xi], SchedData.versionArray[xi], SchedData.versionArray[xi]));
//        }
//
//        adapter = new SchedAdapter(data);
//        recyclerView.setAdapter(adapter);
//
//    }
}
