package com.example.myapplication.problem;

import android.util.Log;

public class Waypoint {
    Location location;
    String indication;
//    WaypointType locationType;

    public Waypoint(Location location, String indication) {
        this.location = location;
        this.indication = indication;
//        this.locationType = locationType;
    }

    public Waypoint(Location location) {
        this.location = location;
//        this.locationType = locationType;
        this.indication = "";
    }

    public void addIndication(String string){
        this.indication = this.indication.concat(string);
    }

    public String print(){
        return this.indication;
    }
}
