package com.example.myapplication;

import com.example.myapplication.problem.Building;
import com.example.myapplication.problem.Waypoint;

import java.util.List;

public class ApplicationData {
    List<String> buildings;
    String currentBuilding;

    List<Waypoint> waypoints;

    public ApplicationData() {

    }

    public List<String> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<String> buildings) {
        this.buildings = buildings;
    }

    public String getCurrentBuilding() {
        return currentBuilding;
    }

    public void setCurrentBuilding(String currentBuilding) {
        this.currentBuilding = currentBuilding;
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }
}
