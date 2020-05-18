/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

import java.io.Serializable;
import javafx.util.Pair;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Alex
 */
public class Floor implements Serializable {
    
    static final long serialVersionUID = 1L;
    
    private List<ExtendedShape> shapes = new ArrayList<>();
    private Graph graph = new Graph();
    private Hallway hallway;

    public Hallway getHallway() {
        return hallway;
    }

    public void setHallway(Hallway hallway) {
        this.hallway = hallway;
    }

    private int level;

    Floor(int level) {
        setLevel(level);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<ExtendedShape> getShapes() {
        return shapes;
    }

    public void setShapes(List<ExtendedShape> shapes) {
        this.shapes = shapes;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public JSONObject toJson() {
        JSONObject floorJSON = new JSONObject();

        floorJSON.put("level", level);

        List<JSONObject> waypointsList = new ArrayList<JSONObject>();
        for (Map.Entry<ExtendedShape, List<Pair<ExtendedShape, String>>> entry : graph.neighborGraph.entrySet()) {

            JSONObject waypointJSON = new JSONObject();

            waypointJSON.put("name", ((ExtendedRectangle) entry.getKey()).getName());
            waypointJSON.put("markerId", ((ExtendedRectangle) entry.getKey()).getId());
            waypointJSON.put("type", "connector");

            List<JSONObject> neighboursList = new ArrayList<JSONObject>();
            for (Pair<ExtendedShape, String> neighbour : entry.getValue()) {
                JSONObject neighbourJSON = new JSONObject();
                neighbourJSON.put("name", ((ExtendedRectangle) neighbour.getKey()).getName());
                neighbourJSON.put("direction", neighbour.getValue());
                neighboursList.add(neighbourJSON);
            }
            waypointJSON.put("neighbors", neighboursList);
            waypointsList.add(waypointJSON);
        }
        floorJSON.put("waypoints", waypointsList);

        return floorJSON;
    }
}
