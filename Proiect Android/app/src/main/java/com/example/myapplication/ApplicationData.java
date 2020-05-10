package com.example.myapplication;

import android.os.Build;

import com.example.myapplication.problem.Building;
import com.example.myapplication.problem.Waypoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApplicationData {

    static List<String> buildings = new ArrayList<>();
    static Building currentBuilding;
    static String currentBuildingName;
    static List<Waypoint> waypoints = new ArrayList<>();
    static List<Building> buildingsData = new ArrayList<>();

    public ApplicationData() {

    }

    public Building getBuildingByName(String buildingName) {
        for (Building building:buildingsData)
            if (building.getName().equals(buildingName))
                return building;
        return null;
    }

    public static List<Building> getBuildingsData() {
        return buildingsData;
    }

    public void setBuildingsData(List<Building> buildingsData) {
        ApplicationData.buildingsData = buildingsData;
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

    public void setCurrentBuildingName(String currentBuildingName) {
        this.currentBuildingName = currentBuildingName;
    }

    public ArrayList<String> getAllInstructions() {
        ArrayList<String> instructions = new ArrayList<>();
        for (Waypoint waypoint:waypoints)
            instructions.add(waypoint.getInstruction());
        return instructions;
    }

    public ArrayList<Integer> getAllCodesToScan() {
        ArrayList<Integer> codes = new ArrayList<>();
        for (Waypoint waypoint:waypoints)
            codes.add(waypoint.getCode());
        return codes;
    }

}
