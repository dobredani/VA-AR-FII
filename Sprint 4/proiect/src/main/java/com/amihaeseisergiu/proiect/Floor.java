/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

import javafx.util.Pair;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;

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
            if (entry.getValue() != null) {
                for (Pair<ExtendedShape, String> neighbour : entry.getValue()) {
                    JSONObject neighbourJSON = new JSONObject();
                    neighbourJSON.put("name", ((ExtendedRectangle) neighbour.getKey()).getName());
                    neighbourJSON.put("direction", neighbour.getValue());
                    neighboursList.add(neighbourJSON);
                }
            }
            waypointJSON.put("neighbors", neighboursList);
            if (entry.getKey() instanceof Office) {
                JSONArray profs = new JSONArray();
                for (String prof : ((Office) entry.getKey()).getProfessors()) {
                    profs.add(prof);
                }
                waypointJSON.put("professors", profs);
            }
            if (entry.getKey() instanceof Classroom) {
                JSONArray schedule = new JSONArray();
                Iterator<Map.Entry<Integer, List<InputSchedule>>> iter = ((Classroom) entry.getKey()).mapaInputuri.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<Integer, List<InputSchedule>> entry1 = iter.next();
                    List<InputSchedule> localLista = entry1.getValue();
                    JSONObject objectSchedule = new JSONObject();
                    for (InputSchedule input : localLista) {
                        objectSchedule.put("group", input.getGrupa());
                        objectSchedule.put("course", input.getMaterie());
                        objectSchedule.put("startTime", input.getOraStart());
                        objectSchedule.put("finishTime", input.getOraFinal());
                        switch (entry1.getKey()) {
                            case 0:
                                objectSchedule.put("dayOfWeek", "Luni");
                                break;
                            case 1:
                                objectSchedule.put("dayOfWeek", "Marti");
                                break;
                            case 2:
                                objectSchedule.put("dayOfWeek", "Miercuri");
                                break;
                            case 3:
                                objectSchedule.put("dayOfWeek", "Joi");
                                break;
                            case 4:
                                objectSchedule.put("dayOfWeek", "Vineri");
                                break;
                            case 5:
                                objectSchedule.put("dayOfWeek", "Sambata");
                                break;
                            case 6:
                                objectSchedule.put("dayOfWeek", "Duminica");
                                break;

                        }
                        schedule.add(objectSchedule);
                    }
                }
                waypointJSON.put("schedule", schedule);
            }

            waypointsList.add(waypointJSON);
        }

        floorJSON.put("waypoints", waypointsList);

        List<JSONObject> hallwaysList = new ArrayList<>();
        for (ExtendedShape s : shapes) {
            
            if(s instanceof Hallway)
            {
                JSONObject hallwayJSON = new JSONObject();
                
                hallwayJSON.put("name", ((ExtendedRectangle) s).getName());
                hallwayJSON.put("markerId", ((ExtendedRectangle) s).getId());
                hallwayJSON.put("shapeType", ((ExtendedRectangle) s).getShapeType());
                hallwayJSON.put("width", ((ExtendedRectangle) s).getWidth());
                hallwayJSON.put("length", ((ExtendedRectangle) s).getLength());
                hallwayJSON.put("x", ((ExtendedRectangle) s).getCenterPoint().getX());
                hallwayJSON.put("y", ((ExtendedRectangle) s).getCenterPoint().getY());
                hallwayJSON.put("color", ((ExtendedRectangle) s).getColor());
                
                hallwaysList.add(hallwayJSON);
            }
        }
        
        floorJSON.put("hallways", hallwaysList);

        return floorJSON;
    }
}
