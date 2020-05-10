package com.example.myapplication.Schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.Schedule.SchedAdapter;
import com.example.myapplication.Schedule.SchedData;
import com.example.myapplication.Schedule.SchedModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SchedViewFragment extends Fragment {
    public SchedViewFragment() { /* empty contructor */ }

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

        RecyclerView recyclerView = view.findViewById(R.id.recyclerCalendar);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ArrayList<SchedModel> data = new ArrayList<>();
        for (int xi = 0; xi < SchedData.nameArray.length; xi++) {
            data.add(new SchedModel(SchedData.nameArray[xi], SchedData.versionArray[xi], SchedData.versionArray[xi], SchedData.versionArray[xi]));
        }

        RecyclerView.Adapter adapter = new SchedAdapter(data);
        recyclerView.setAdapter(adapter);

    }
}
