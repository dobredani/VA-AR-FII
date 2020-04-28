package com.example.myapplication;

import android.util.Log;

import com.example.myapplication.problem.Building;
import com.example.myapplication.problem.Location;
import com.example.myapplication.problem.Waypoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    public JsonParser(){

    }

    public static List<String> parseBuildingList(JSONObject jsonObject){
        List<String> buildings = new ArrayList<>();
        try {
            JSONArray array = jsonObject.getJSONArray("buildings");
            for(int i=0; i < array.length(); i++){
                String building = new String(array.getJSONObject(i).getString("name"));
                buildings.add(building);
                Log.d("build","JSON Parsed Building: " + building);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return buildings;
    }

    public static Building parseBuilding(JSONObject jsonObject){
        List<Location> locations = new ArrayList<>();
        try {
            JSONArray floors = jsonObject.getJSONArray("floors");
            for(int i=0; i < floors.length(); i++){
                JSONObject floor = floors.getJSONObject(i);
                JSONArray floorLocations = floor.getJSONArray("waypoints");
                for(int j=0; j < floorLocations.length(); j++){
                    JSONObject object = floorLocations.getJSONObject(j);
                    Location location = new Location(object.getInt("markerId"),
                            object.getString("name"));
                    Log.e("location","Parsed Current Building Location " + location.getName());
                    locations.add(location);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Building building = null;
        try {
            building = new Building(jsonObject.getString("name"),locations);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return building;
    }



    public static List<Waypoint> parseRoute(JSONArray jsonArray){
        List<Waypoint> waypointList = new ArrayList<>();
//        Building currentBuilding = MainActivity.applicationData.getCurrentBuilding();
        try {

            for(int i=0; i < jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                if(object.has("markerId"))
                {
                    Waypoint waypoint = null;
                    Location location = new Location(object.getInt("markerId"),
                            object.getString("name"));
                    if(object.has("direction")){
                        String indication ="Go " + object.getString("direction") ;
                        waypoint = new Waypoint(location,indication);
                    }
                    else
                    {
                        waypoint = new Waypoint(location,"You have reached the destination!");
                    }

                    waypointList.add(waypoint);

                    if(i != 0){
//                        Current waypoint waypoinyList.size()-1;
//                        Add indication to the previous waypoint.
                        waypointList.get(waypointList.size()-2).addInstruction(" Scan " + object.getString("name"));
                    }
                }
                else {

//                    JSONObject adjacentObject = jsonArray.getJSONObject(i+1);
                    if(object.getString("name").equals("Elevator")){
                        // We ignore elevators for now

                        continue;

                    }

                    else if(object.getString("name").equals("Stairs")){
                        //Last added waypoint
                        Waypoint waypoint = waypointList.get(waypointList.size()-1);
                        int floor = object.getInt("floor");
                        waypoint.addInstruction(" Take the stairs to floor " + Integer.toString(floor) + " ");
                        waypoint.addInstruction("Go ");
                        waypoint.addInstruction(object.getString("direction"));

                    }
                }

            }
            for(Waypoint waypoint : waypointList){
                Log.e("Indication", waypoint.print());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return waypointList;
    }

}
