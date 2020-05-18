package com.example.myapplication;

import android.os.Build;

import com.example.myapplication.problem.Building;
import com.example.myapplication.problem.Waypoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApplicationData {
    private static ApplicationData instance =  null;

    private List<String> buildings = new ArrayList<>();
    Building currentBuilding;
    private List<Waypoint> waypoints = new ArrayList<>();
    private List<Building> buildingsData = new ArrayList<>();

    private ApplicationData() {

    }

    public static ApplicationData getInstance() {
        if (instance == null)
            instance = new ApplicationData();
        return instance;
    }

    public Building getBuildingByName(String buildingName) {
        for (Building building:buildingsData)
            if (building.getName().equals(buildingName))
                return building;
        return null;
    }

    public List<Building> getBuildingsData() {
        return buildingsData;
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

    public void setWaypoints(List<Waypoint> waypoints) {
        this.waypoints = waypoints;
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
