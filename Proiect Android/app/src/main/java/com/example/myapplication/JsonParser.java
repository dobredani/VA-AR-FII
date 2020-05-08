package com.example.myapplication;

import android.util.Log;

import com.example.myapplication.problem.Building;
import com.example.myapplication.problem.Lecture;
import com.example.myapplication.problem.Location;
import com.example.myapplication.problem.LocationType;
import com.example.myapplication.problem.Waypoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    public JsonParser() {

    }

    public static List<String> parseBuildingList(JSONObject jsonObject) {
        List<String> buildings = new ArrayList<>();
        try {
            JSONArray array = jsonObject.getJSONArray("buildings");
            for (int i = 0; i < array.length(); i++) {
                String building = new String(array.getJSONObject(i).getString("name"));
                buildings.add(building);
                Log.d("build", "JSON Parsed Building: " + building);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return buildings;
    }

    public static Building parseBuilding(JSONObject jsonObject) {
        List<Location> locations = new ArrayList<>();


        try {
            JSONArray floors = jsonObject.getJSONArray("floors");
            for (int i = 0; i < floors.length(); i++) {
                JSONObject floor = floors.getJSONObject(i);
                JSONArray floorLocations = floor.getJSONArray("waypoints");
                for (int j = 0; j < floorLocations.length(); j++) {
                    JSONObject object = floorLocations.getJSONObject(j);
                    if (object.has("type")) {
                        if (object.getString("type").equals("classRoom")) {
                            List<Lecture> schedule = new ArrayList<>();

                            JSONArray lectureList = object.getJSONArray("schedule");
                            for (int k = 0; k < lectureList.length(); k++) {
                                JSONObject lectureItem = lectureList.getJSONObject(k);

                                String group = lectureItem.getString("group");
                                String course = lectureItem.getString("course");
                                String dayOfWeek = lectureItem.getString("dayOfWeek");
                                String startTime = lectureItem.getString("startTime");
                                String finishTime = lectureItem.getString("finishTime");
                                Lecture lecture = new Lecture(group, course, dayOfWeek, startTime, finishTime);
                                schedule.add(lecture);

                            }
                            for (Lecture lecture : schedule)
                                Log.e("lecture", "This clasroom lecture " + lecture.toString());

                            Location location = new Location(object.getInt("markerId"), object.getString("name"),
                                    LocationType.CLASSROOM, schedule);

                            locations.add(location);

                            Log.e("location", "Parsed Current Building Location " + location.getName());

                        } else {
                            Location location = new Location(object.getInt("markerId"), object.getString("name"));

                            locations.add(location);

                            Log.e("location", "Parsed Current Building Location " + location.getName());

                        }
                    } else {

                        Location location = new Location(object.getInt("markerId"),
                                object.getString("name"));
                        locations.add(location);

                        Log.e("location", "Parsed Current Building Location " + location.getName());
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Building building = null;
        try {
            building = new Building(jsonObject.getString("name"), locations);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return building;
    }


    public static List<Waypoint> parseRoute(JSONArray jsonArray) {
        List<Waypoint> waypointList = new ArrayList<>();
//        Building currentBuilding = MainActivity.applicationData.getCurrentBuilding();
        try {

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                Waypoint waypoint = null;

                Location location = new Location(object.getInt("markerId"),
                        object.getString("name"));

                if (object.has("direction")) {
                    String indication = "Go " + object.getString("direction").toLowerCase();
                    waypoint = new Waypoint(location, indication);
                } else {
                    waypoint = new Waypoint(location, "You have reached the destination!");
                }

                waypointList.add(waypoint);

                if (i != 0) {
//                        Current waypoint waypoinyList.size()-1;
//                        Add indication to the previous waypoint.
                    Waypoint prevWaypoint = waypointList.get(waypointList.size() - 2);

                    if (object.getString("name").equals("Stairs") && prevWaypoint.getLocationName().equals("Stairs")) {
                        int floor = object.getInt("floor");

//                          Previous Waypoint indication = Go {direction}
                        prevWaypoint.addInstruction(" to floor " + Integer.toString(floor));
                    }

                    prevWaypoint.addInstruction(" then " + "scan " + object.getString("name"));


                }

            }

            for (Waypoint waypoint : waypointList) {
                Log.e("Indication", waypoint.print());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return waypointList;
    }

}
