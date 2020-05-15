package com.example.myapplication;

import com.example.myapplication.problem.Building;
import com.example.myapplication.problem.Waypoint;

import java.util.List;

public class ApplicationData {
    List<String> buildings;
    Building currentBuilding;
    String currentBuildingName;

    List<Waypoint> waypoints;

    public ApplicationData() {

    }

    public List<String> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<String> buildings) {
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

    public String getCurrentBuildingName() {
        return currentBuildingName;
    }

    public void setCurrentBuildingName(String currentBuildingName) {
        this.currentBuildingName = currentBuildingName;
    }

}
