package com.example.myapplication;

import com.example.myapplication.problem.Building;
import com.example.myapplication.problem.Waypoint;

import java.util.List;

public class ApplicationData {
    List<Building> buildings;
    Building currentBuilding;
    List<Waypoint> waypoints;

    public ApplicationData() {

    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }

    public Building getCurrentBuilding() {
        return currentBuilding;
    }

    public void setCurrentBuilding(Building currentBuilding) {
        this.currentBuilding = currentBuilding;
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }
}
