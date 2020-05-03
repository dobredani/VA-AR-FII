package com.example.myapplication.problem;

import android.util.Log;

public class Waypoint {
    Location location;
    String instruction;

    public Waypoint(Location location, String indication) {
        this.location = location;
        this.instruction = indication;
    }

    public void addInstruction(String string){
        this.instruction = this.instruction.concat(string);
    }

    public String getInstruction() {return this.instruction; }

    public int getCode() {return this.location.id; }

    public String print(){
        return this.instruction;
    }

    @Override
    public String toString() {
        return "Waypoint{" +
                "location=" + location +
                ", instruction='" + instruction + '\'' +
                '}';
    }
}
