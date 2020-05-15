package com.example.myapplication.problem;

import com.example.myapplication.Schedule.SchedModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Location {
    int id;
    String name;
    LocationType locationType;

    List<Lecture> schedules;
    List<String> professors;
    List<List<SchedModel>> schedModels; // one list per weekday

    public List<SchedModel> getDaySchedule(int weekDay) {
        if (weekDay < 1 || weekDay > 5) return null;
        return schedModels.get(weekDay-1);
    }

    public Location() {
        schedules = new ArrayList<>();
        schedModels = new ArrayList<>();

        for (int xi = 0; xi < 5; xi++) {
            List<SchedModel> lectures = new ArrayList<>();
            schedModels.add(lectures); // monday - friday
        }
    }

    public Location(int id, String name) {
        this();
        this.id = id;
        this.name = name;
    }

    public Location(int id, String name, LocationType locationType) {
        this();
        this.id = id;
        this.name = name;
        this.locationType = locationType;
    }

    public Location(int id, String name, LocationType locationType, List<Lecture> schedule) {
        this();
        this.id = id;
        this.name = name;
        this.locationType = locationType;

        for (Lecture lecture: schedule) {
            schedules.add(lecture);
            schedModels.get(lecture.dayNumer-1).add(new SchedModel(lecture.getCourse(),
                    lecture.getGroup(),lecture.getStartTime(),lecture.getFinishTime()));
        }
    }

    public Location(int id, String name,List<String> professors,  LocationType locationType){
        this();
        this.id = id;
        this.name = name;
        this.locationType = locationType;
        this.professors = professors;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return id == location.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    //TODO return an array of lectures for the given week day

    public LocationType getLocationType() {
        return locationType;
    }
  
}
