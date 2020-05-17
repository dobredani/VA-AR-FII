package com.example.myapplication.problem;

import java.util.ArrayList;
import java.util.List;

public class Building {
    private String name;
    private List<Location> locations;

    public Building(String name, List<Location> locations) {
        this.name = name;
        this.locations = locations;
    }

    public String getName() {
        return name;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public Location getLocation(String name) {
        for (Location location : locations)
            if (location.getName().equals(name))
                return location;
        return null;
    }

    public Location getLocationById(int id) {
        for (Location location : locations)
            if (location.getId() == id)
                return location;
        return null;
    }

    public List<Location> getTopLocations(int howMany) {
        List<Location> topLocations = new ArrayList<>();
        for (Location location:locations) {
            if (location.getLocationType() != LocationType.CONNECTOR)
                topLocations.add(location);
            if (topLocations.size() == howMany)
                break;
        }
        return topLocations;
    }
}
