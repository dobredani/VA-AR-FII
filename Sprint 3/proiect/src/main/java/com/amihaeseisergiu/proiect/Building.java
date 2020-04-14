/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Alex
 */
public class Building {
    Map<Integer, Floor> floors = new HashMap<>();

    public Map<Integer, Floor> getFloors() {
        return floors;
    }

    public void setFloors(Map<Integer, Floor> floors) {
        this.floors = floors;
    }
    
    public String toJson()
    {
        String json = "{\n";
        json += "\"name\": \"Building name\",\n"; //to be replaced when we add the option to add a name to a building
        json += "\"floors\":\n";
        json += "[\n";
        boolean first = true;
        for(var entry : floors.entrySet())
        {
            if(first)
            {
                first = false;
                json += "{\n";
            }
            else
            {
                json += ",\n{\n";
            }
            json += "\"level\": " + entry.getKey() + ",\n";
            json += "\"wayPoints\": \n";
            json += entry.getValue().toJson();
        }
        json += "]\n";
        json += "}";
        
        return json;
    }
}
