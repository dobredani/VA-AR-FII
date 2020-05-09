/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

import javafx.util.Pair;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Alex
 */
public class Floor {
    
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

        List<JSONObject> waypointsList = new ArrayList<>();
        for (Map.Entry<ExtendedShape, List<Pair<ExtendedShape, String>>> entry : graph.neighborGraph.entrySet()) {

            JSONObject waypointJSON = new JSONObject();

            waypointJSON.put("name", ((ExtendedRectangle) entry.getKey()).getName());
            waypointJSON.put("markerId", ((ExtendedRectangle) entry.getKey()).getId());
            waypointJSON.put("type", ((ExtendedRectangle) entry.getKey()).getType());
            waypointJSON.put("width", ((ExtendedRectangle) entry.getKey()).getWidth());
            waypointJSON.put("length", ((ExtendedRectangle) entry.getKey()).getLength());
            waypointJSON.put("x", ((ExtendedRectangle) entry.getKey()).getCenterPoint().getX());
            waypointJSON.put("y", ((ExtendedRectangle) entry.getKey()).getCenterPoint().getY());
            waypointJSON.put("color", ((ExtendedRectangle) entry.getKey()).getColor());
            waypointJSON.put("shapeType", ((ExtendedRectangle) entry.getKey()).getShapeType());

            List<JSONObject> neighboursList;
            neighboursList = new ArrayList<>();
            if(entry.getValue() != null)
            {
                for (Pair<ExtendedShape, String> neighbour : entry.getValue()) {
                    JSONObject neighbourJSON = new JSONObject();
                    neighbourJSON.put("name", ((ExtendedRectangle) neighbour.getKey()).getName());
                    neighbourJSON.put("direction", neighbour.getValue());
                    neighboursList.add(neighbourJSON);
                }
            }
            waypointJSON.put("neighbors", neighboursList);
            waypointsList.add(waypointJSON);
        }
        
        JSONObject waypointJSON = new JSONObject();

        waypointJSON.put("name", ((ExtendedRectangle) graph.hallway).getName());
        waypointJSON.put("markerId", ((ExtendedRectangle) graph.hallway).getId());
        waypointJSON.put("type", ((ExtendedRectangle) graph.hallway).getType());
        waypointJSON.put("width", ((ExtendedRectangle) graph.hallway).getWidth());
        waypointJSON.put("length", ((ExtendedRectangle) graph.hallway).getLength());
        waypointJSON.put("x", ((ExtendedRectangle) graph.hallway).getCenterPoint().getX());
        waypointJSON.put("y", ((ExtendedRectangle) graph.hallway).getCenterPoint().getY());
        waypointJSON.put("color", ((ExtendedRectangle) graph.hallway).getColor());
        waypointJSON.put("shapeType", ((ExtendedRectangle) graph.hallway).getShapeType());
        waypointsList.add(waypointJSON);
        
        floorJSON.put("waypoints", waypointsList);

        return floorJSON;
    }
}
