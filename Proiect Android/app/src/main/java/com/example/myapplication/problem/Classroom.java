package com.example.myapplication.problem;

import com.example.myapplication.Schedule.SchedModel;

import java.util.ArrayList;
import java.util.List;

public class Classroom extends Location {
    List<Lecture> schedules;
    List<List<SchedModel>> schedModels; // one list per weekday

    public List<SchedModel> getDaySchedule(int weekDay) {
        if (weekDay < 1 || weekDay > 5) return null;
        return schedModels.get(weekDay - 1);
    }


    public Classroom(int id, String name, List<Lecture> schedule) {
        super(id, name);
        this.schedules = schedule;

        for (int xi = 0; xi < 5; xi++) {
            List<SchedModel> lectures = new ArrayList<>();
            schedModels.add(lectures); // monday - friday
        }

        for (Lecture lecture : schedule) {
            schedules.add(lecture);
            schedModels.get(lecture.dayNumer - 1).add(new SchedModel(lecture.getCourse(),
                    lecture.getGroup(), lecture.getStartTime(), lecture.getFinishTime()));

        }

    }


    public List<Lecture> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Lecture> schedules) {
        this.schedules = schedules;
    }


}