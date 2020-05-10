package com.example.myapplication.problem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Location {
    int id;
    String name;
    LocationType locationType;
    List<Lecture> schedule;


    public Location(int id, String name) {
        this.id = id;
        this.name = name;
        schedule = new ArrayList<>();
    }

    public Location(int id, String name, LocationType locationType) {
        this.id = id;
        this.name = name;
        this.locationType = locationType;
    }

    public Location(int id, String name, LocationType locationType, List<Lecture> schedule) {
        this.id = id;
        this.name = name;
        this.locationType = locationType;
        this.schedule = schedule;
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

    public LocationType getLocationType() {
        return locationType;
    }
}
