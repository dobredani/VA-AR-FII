package com.example.myapplication.problem;

public class Waypoint {
    Location location;
    String indication;
    WaypointType locationType;

    public Waypoint(Location location, String indication, WaypointType locationType) {
        this.location = location;
        this.indication = indication;
        this.locationType = locationType;
    }

    public Waypoint(Location location, WaypointType locationType) {
        this.location = location;
        this.locationType = locationType;
        this.indication = "";
    }
}
