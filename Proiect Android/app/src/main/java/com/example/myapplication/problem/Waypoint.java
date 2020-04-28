package com.example.myapplication.problem;

import android.util.Log;

public class Waypoint {
    Location location;
    String instruction;

    public Waypoint(Location location, String indication) {
        this.location = location;
        this.instruction = indication;
    }

    public Waypoint(Location location) {
        this.location = location;
        this.instruction = "";
    }

    public void addInstruction(String string){
        this.instruction = this.instruction.concat(string);
    }

    public String getInstruction() {return this.instruction; }

    public int getCode() {return this.location.id; }

    public String print(){
        return this.instruction;
    }
}
