/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.util.Pair;

/**
 * @author Alex
 */
public class Graph {

    Map<ExtendedShape, List<Pair<ExtendedShape, String>>> graph;
    Map<ExtendedShape, List<Pair<ExtendedShape, String>>> neighborGraph;

    public Graph() {
        graph = new HashMap<>();
        neighborGraph = new HashMap<>();
    }

    public Map<ExtendedShape, List<Pair<ExtendedShape, String>>> getGraph() {
        return graph;
    }

    public void setGraph(Map<ExtendedShape, List<Pair<ExtendedShape, String>>> graph) {
        this.graph = graph;
    }

    public void addInitialShape(ExtendedShape initialShape) {
        List<Pair<ExtendedShape, String>> l = new ArrayList<>();
        graph.put(initialShape, l);
    }

    public void addShape(ExtendedShape shape, Pair<ExtendedShape, String> p) {
        if (graph.containsKey(shape)) {
            graph.get(shape).add(p);
        } else {
            List<Pair<ExtendedShape, String>> l = new ArrayList<>();
            graph.put(shape, l);
            graph.get(shape).add(p);
        }
    }

    public void deleteShapeFromGraph(ExtendedShape shape) {
        ExtendedShape s = null;
        for (Map.Entry<ExtendedShape, List<Pair<ExtendedShape, String>>> entry : graph.entrySet()) {
            if (entry.getKey() == shape) {
                s = entry.getKey();
                break;
            }
        }
        graph.remove(s);

        for (Map.Entry<ExtendedShape, List<Pair<ExtendedShape, String>>> entry : graph.entrySet()) {
            int[] indexes = new int[entry.getValue().size()];
            int contor = 0;
            for (Pair<ExtendedShape, String> p : entry.getValue()) {
                if (p.getKey() == s) {
                    indexes[contor++] = entry.getValue().indexOf(p);
                }
            }
            for (int i = 0; i < contor; i++) {
                entry.getValue().remove(indexes[i]);
            }
        }

        s = null;
        for (Map.Entry<ExtendedShape, List<Pair<ExtendedShape, String>>> entry : neighborGraph.entrySet()) {
            if (entry.getKey() == shape) {
                s = entry.getKey();
                break;
            }
        }
        neighborGraph.remove(s);

        for (Map.Entry<ExtendedShape, List<Pair<ExtendedShape, String>>> entry : neighborGraph.entrySet()) {
            if (entry.getValue() != null) {
                int[] indexes = new int[entry.getValue().size()];
                int contor = 0;
                for (Pair<ExtendedShape, String> p : entry.getValue()) {
                    if (p.getKey() == s) {
                        indexes[contor++] = entry.getValue().indexOf(p);
                    }
                }
                for (int i = 0; i < contor; i++) {
                    entry.getValue().remove(indexes[i]);
                    for (int j = i + 1; j < contor; j++) {
                        indexes[j]--;
                    }
                }
            }
        }
        setOrder();
    }

    public void setOrder() {
        for (Map.Entry<ExtendedShape, List<Pair<ExtendedShape, String>>> r : graph.entrySet()) {
            List<ExtendedShape> downs = new ArrayList<>();
            List<ExtendedShape> ups = new ArrayList<>();
            List<ExtendedShape> rights = new ArrayList<>();
            List<ExtendedShape> lefts = new ArrayList<>();
            if (r.getValue() != null) {
                for (Pair p : r.getValue()) {
                    if (p.getValue().toString().contains("Down")) {
                        downs.add((ExtendedShape) p.getKey());
                    } else if (p.getValue().toString().contains("Up")) {
                        ups.add((ExtendedShape) p.getKey());
                    } else if (p.getValue().toString().contains("Right")) {
                        rights.add((ExtendedShape) p.getKey());
                    } else if (p.getValue().toString().contains("Left")) {
                        lefts.add((ExtendedShape) p.getKey());
                    }
                }
                Collections.sort(downs, new SortByX());
                Collections.sort(ups, new SortByX());
                Collections.sort(lefts, new SortByY());
                Collections.sort(rights, new SortByY());

                int downIndex = 0;
                int upIndex = 0;
                int rightIndex = 0;
                int leftIndex = 0;
                List<Pair<ExtendedShape, String>> newPairs = new ArrayList<>();
                for (Pair p : r.getValue()) {
                    if (p.getValue().toString().contains("Down")) {
                        newPairs.add(new Pair(downs.get(downIndex), "Down " + (downIndex + 1)));
                        downIndex++;
                    } else if (p.getValue().toString().contains("Up")) {
                        newPairs.add(new Pair(ups.get(upIndex), "Up " + (upIndex + 1)));
                        upIndex++;
                    } else if (p.getValue().toString().contains("Right")) {
                        newPairs.add(new Pair(rights.get(rightIndex), "Right " + (rightIndex + 1)));
                        rightIndex++;
                    } else if (p.getValue().toString().contains("Left")) {
                        newPairs.add(new Pair(lefts.get(leftIndex), "Left " + (leftIndex + 1)));
                        leftIndex++;
                    }
                }
                graph.replace(r.getKey(), newPairs);
            }
        }
        setNeighbors();
    }

    public void setNeighbors() {
        for (Map.Entry<ExtendedShape, List<Pair<ExtendedShape, String>>> entry : graph.entrySet()) {
            if (!(entry.getKey() instanceof Hallway)) {
                Hallway hallway = findShapeHallway(entry.getKey());
                String relationToHallway = "";
                String directionToHallway = "";
                int orderToHallway = 0;
                List<Pair<ExtendedShape, String>> list = graph.get(hallway);
                if (list != null) {
                    for (Pair<ExtendedShape, String> pair : list) {
                        if (pair.getKey() == entry.getKey()) {
                            relationToHallway = pair.getValue();
                            directionToHallway = relationToHallway.split(" ")[0];
                            orderToHallway = Integer.valueOf(relationToHallway.split(" ")[1]);
                        }
                    }
                    ExtendedShape leftNeighbor = findLeftNeighbor(entry.getKey(), directionToHallway, orderToHallway, hallway);
                    ExtendedShape rightNeighbor = findRightNeighbor(entry.getKey(), directionToHallway, orderToHallway, hallway);
                    ExtendedShape straightNeighbor = findStraightNeighbor(entry.getKey(), directionToHallway, hallway);
                    /* if (neighborGraph.get(straightNeighbor) != null) {
                        for (Pair<ExtendedShape, String> pair : neighborGraph.get(straightNeighbor)) {
                            if (pair.getValue().equals("Straight") && pair.getKey() != entry.getKey()) {
                                straightNeighbor = null;
                            }
                        }
                  /*      for (Map.Entry<ExtendedShape, List<Pair<ExtendedShape, String>>> entry2 : neighborGraph.entrySet()) {
                            for (Pair<ExtendedShape, String> pair : entry2.getValue()) {
                                if(pair.getValue().equals("Straight") && pair.getKey() == straightNeighbor) {
                                    straightNeighbor = null;
                                }
                        }
                    }
                }*/
                    if (leftNeighbor != null && rightNeighbor != null && straightNeighbor != null) {
                        Pair<ExtendedShape, String> p1 = new Pair<>(leftNeighbor, "Left");
                        Pair<ExtendedShape, String> p2 = new Pair<>(rightNeighbor, "Right");
                        Pair<ExtendedShape, String> p3 = new Pair<>(straightNeighbor, "Straight");
                        List<Pair<ExtendedShape, String>> neighbors = new ArrayList<>();
                        neighbors.add(p1);
                        neighbors.add(p2);
                        neighbors.add(p3);
                        if (neighborGraph.get(entry.getKey()) != null) {
                            neighborGraph.get(entry.getKey()).removeAll(neighborGraph.get(entry.getKey()));
                            neighborGraph.replace(entry.getKey(), neighbors);
                        } else {
                            neighborGraph.put(entry.getKey(), neighbors);
                        }
                    } else {
                        List<Pair<ExtendedShape, String>> neighbors = new ArrayList<>();
                        if (leftNeighbor != null) {
                            Pair<ExtendedShape, String> p1 = new Pair<>(leftNeighbor, "Left");
                            neighbors.add(p1);
                        }
                        if (rightNeighbor != null) {
                            Pair<ExtendedShape, String> p1 = new Pair<>(rightNeighbor, "Right");
                            neighbors.add(p1);
                        }

                        if (straightNeighbor != null) {
                            Pair<ExtendedShape, String> p1 = new Pair<>(straightNeighbor, "Straight");
                            neighbors.add(p1);
                        }
                        if (leftNeighbor != null || rightNeighbor != null || straightNeighbor != null) {
                            if (neighborGraph.get(entry.getKey()) != null) {
                                neighborGraph.get(entry.getKey()).removeAll(neighborGraph.get(entry.getKey()));
                                neighborGraph.replace(entry.getKey(), neighbors);
                            } else {
                                neighborGraph.put(entry.getKey(), neighbors);
                            }
                        }
                        if (rightNeighbor == null && leftNeighbor == null && straightNeighbor == null) {
                            if (neighborGraph.get(entry.getKey()) != null) {
                                neighborGraph.replace(entry.getKey(), null);
                            } else {
                                neighborGraph.put(entry.getKey(), null);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        String output = "Graph: \n";
        for (Map.Entry<ExtendedShape, List<Pair<ExtendedShape, String>>> entry : graph.entrySet()) {
            output += ((ExtendedRectangle) entry.getKey()).getId() + "\n";
            if (entry.getValue() != null) {
                for (Pair<ExtendedShape, String> p : entry.getValue()) {
                    output += ((ExtendedRectangle) p.getKey()).getId() + ":" + p.getValue() + "\n";
                }
            }
        }
        output += "\nNeighbor Graph: \n";
        for (Map.Entry<ExtendedShape, List<Pair<ExtendedShape, String>>> entry : neighborGraph.entrySet()) {
            output += ((ExtendedRectangle) entry.getKey()).getId() + "\n";
            if (entry.getValue() != null) {
                for (Pair<ExtendedShape, String> p : entry.getValue()) {
                    output += ((ExtendedRectangle) p.getKey()).getId() + ":" + p.getValue() + "\n";
                }
            }
        }
        return output;
    }

    private ExtendedShape findLeftNeighbor(ExtendedShape sh, String dth, int otw, Hallway hallway) {
        List<ExtendedShape> hallwaysToAvoid = new ArrayList<>();
        List<ExtendedShape> traversedHallways = new ArrayList<>();
        traversedHallways.add(hallway);
        String searchDirection = "";
        if (dth.equals("Left")) {
            searchDirection = "Down";
        } else if (dth.equals("Right")) {
            searchDirection = "Up";
        } else if (dth.equals("Up")) {
            searchDirection = "Up";
        } else if (dth.equals("Down")) {
            searchDirection = "Down";
        }
        int minOrderToSearch = 1;
        boolean ok = true;
        while (ok == true) {
            ok = false;
            for (Map.Entry<ExtendedShape, List<Pair<ExtendedShape, String>>> entry : graph.entrySet()) {
                if (entry.getKey() != hallway && entry.getKey() != sh) {
                    String relationToHallway = "";
                    String directionToHallway = "";
                    int orderToHallway = 0;
                    List<Pair<ExtendedShape, String>> list = graph.get(hallway);
                    for (Pair<ExtendedShape, String> pair : list) {
                        if (pair.getKey() == entry.getKey()) {
                            relationToHallway = pair.getValue();
                            directionToHallway = relationToHallway.split(" ")[0];
                            orderToHallway = Integer.valueOf(relationToHallway.split(" ")[1]);
                            if (searchDirection.equals("Up")) {
                                if (dth.equals(directionToHallway) && orderToHallway == otw + minOrderToSearch) {
                                    if (entry.getKey() instanceof Hallway) {
                                        //   hallwaysToAvoid.add(hallway);
                                        traversedHallways.add(entry.getKey());
                                        ExtendedShape neighbor = findFirstShapeInHallwayFromLeft(entry.getKey(), traversedHallways, dth, sh, hallway, hallwaysToAvoid);
                                        if (neighbor != null) {
                                            return neighbor;
                                        } else {
                                            minOrderToSearch++;
                                            ok = true;
                                        }
                                    } else {
                                        return entry.getKey();
                                    }
                                }
                            } else {
                                if (dth.equals(directionToHallway) && orderToHallway == otw - minOrderToSearch) {
                                    if (entry.getKey() instanceof Hallway) {
                                        //hallwaysToAvoid.add(hallway);
                                        traversedHallways.add(entry.getKey());
                                        ExtendedShape neighbor = findFirstShapeInHallwayFromLeft(entry.getKey(), traversedHallways, dth, sh, hallway, hallwaysToAvoid);
                                        if (neighbor != null) {
                                            return neighbor;
                                        } else {
                                            minOrderToSearch++;
                                            ok = true;
                                        }
                                    } else {
                                        return entry.getKey();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        int max = 0;
        ExtendedShape currentShape = null;
        if (dth.equals("Left")) {
            searchDirection = "Up"; // min
        } else if (dth.equals("Down")) {
            searchDirection = "Left"; // max
        } else if (dth.equals("Up")) {
            searchDirection = "Right"; // min
        } else if (dth.equals("Right")) {
            searchDirection = "Down"; //max
        }

        minOrderToSearch = 1;
        ok = true;
        while (ok == true) {
            ok = false;
            for (Map.Entry<ExtendedShape, List<Pair<ExtendedShape, String>>> entry : graph.entrySet()) {
                if (entry.getKey() != hallway && entry.getKey() != sh) {
                    String relationToHallway = "";
                    String directionToHallway = "";
                    int orderToHallway = 0;
                    List<Pair<ExtendedShape, String>> list = graph.get(hallway);
                    for (Pair<ExtendedShape, String> pair : list) {
                        if (pair.getKey() == entry.getKey()) {
                            relationToHallway = pair.getValue();
                            directionToHallway = relationToHallway.split(" ")[0];
                            orderToHallway = Integer.valueOf(relationToHallway.split(" ")[1]);
                            if (searchDirection.equals("Up") || searchDirection.equals("Right")) {
                                if (searchDirection.equals(directionToHallway) && orderToHallway == minOrderToSearch) {
                                    if (entry.getKey() instanceof Hallway) {
//                                        hallwaysToAvoid.add(hallway);
                                        traversedHallways.add(entry.getKey());
                                        ExtendedShape neighbor = findFirstShapeInHallwayFromLeft(entry.getKey(), traversedHallways, searchDirection, sh, hallway, hallwaysToAvoid);
                                        if (neighbor != null) {
                                            return neighbor;
                                        } else {
                                            ok = true;
                                            minOrderToSearch++;
                                        }
                                    } else {
                                        return entry.getKey();
                                    }
                                }
                            } else {
                                if (searchDirection.equals(directionToHallway) && orderToHallway > max) {
                                    max = orderToHallway;
                                    currentShape = entry.getKey();
                                }
                            }
                        }
                    }
                }
            }
        }
        if (searchDirection.equals("Down") || searchDirection.equals("Left")) {
            if (max != 0) {
                if (currentShape instanceof Hallway) {
//                    hallwaysToAvoid.add(hallway);
                    traversedHallways.add(currentShape);
                    ExtendedShape neighbor = findFirstShapeInHallwayFromLeft(currentShape, traversedHallways, searchDirection, sh, hallway, hallwaysToAvoid);
                    if (neighbor != null) {
                        return neighbor;
                    }
                } else {
                    if (currentShape != null) {
                        return currentShape;
                    }
                }
            }
        }

        max = 0;
        currentShape = null;
        if (searchDirection.equals("Left")) {
            searchDirection = "Up"; // min
        } else if (searchDirection.equals("Down")) {
            searchDirection = "Left"; // max
        } else if (searchDirection.equals("Up")) {
            searchDirection = "Right"; // min
        } else if (searchDirection.equals("Right")) {
            searchDirection = "Down"; //max
        }

        minOrderToSearch = 1;
        ok = true;
        while (ok == true) {
            ok = false;
            for (Map.Entry<ExtendedShape, List<Pair<ExtendedShape, String>>> entry : graph.entrySet()) {
                if (entry.getKey() != hallway && entry.getKey() != sh) {
                    String relationToHallway = "";
                    String directionToHallway = "";
                    int orderToHallway = 0;
                    List<Pair<ExtendedShape, String>> list = graph.get(hallway);
                    for (Pair<ExtendedShape, String> pair : list) {
                        if (pair.getKey() == entry.getKey()) {
                            relationToHallway = pair.getValue();
                            directionToHallway = relationToHallway.split(" ")[0];
                            orderToHallway = Integer.valueOf(relationToHallway.split(" ")[1]);
                            if (searchDirection.equals("Up") || searchDirection.equals("Right")) {
                                if (searchDirection.equals(directionToHallway) && orderToHallway == minOrderToSearch) {
                                    if (entry.getKey() instanceof Hallway) {
                                        //hallwaysToAvoid.add(hallway);
                                        traversedHallways.add(entry.getKey());
                                        ExtendedShape neighbor = findFirstShapeInHallwayFromLeft(entry.getKey(), traversedHallways, searchDirection, sh, hallway, hallwaysToAvoid);
                                        if (neighbor != null) {
                                            return neighbor;
                                        } else {
                                            ok = true;
                                            minOrderToSearch++;
                                        }
                                    } else {
                                        return entry.getKey();
                                    }
                                }
                            } else {
                                if (searchDirection.equals(directionToHallway) && orderToHallway > max) {
                                    max = orderToHallway;
                                    currentShape = entry.getKey();
                                }
                            }
                        }
                    }
                }
            }
        }
        if (searchDirection.equals("Down") || searchDirection.equals("Left")) {
            if (max != 0) {
                if (currentShape instanceof Hallway) {
                    //hallwaysToAvoid.add(hallway);
                    traversedHallways.add(currentShape);
                    ExtendedShape neighbor = findFirstShapeInHallwayFromLeft(currentShape, traversedHallways, searchDirection, sh, hallway, hallwaysToAvoid);
                    if (neighbor != null) {
                        return neighbor;
                    }
                } else {
                    if (currentShape != null) {
                        return currentShape;
                    }
                }
            }
        }

        max = 0;
        currentShape = null;
        if (searchDirection.equals("Left")) {
            searchDirection = "Up"; // min
        } else if (searchDirection.equals("Down")) {
            searchDirection = "Left"; // max
        } else if (searchDirection.equals("Up")) {
            searchDirection = "Right"; // min
        } else if (searchDirection.equals("Right")) {
            searchDirection = "Down"; //max
        }

        minOrderToSearch = 1;
        ok = true;
        while (ok == true) {
            ok = false;
            for (Map.Entry<ExtendedShape, List<Pair<ExtendedShape, String>>> entry : graph.entrySet()) {
                if (entry.getKey() != hallway && entry.getKey() != sh) {
                    String relationToHallway = "";
                    String directionToHallway = "";
                    int orderToHallway = 0;
                    List<Pair<ExtendedShape, String>> list = graph.get(hallway);
                    for (Pair<ExtendedShape, String> pair : list) {
                        if (pair.getKey() == entry.getKey()) {
                            relationToHallway = pair.getValue();
                            directionToHallway = relationToHallway.split(" ")[0];
                            orderToHallway = Integer.valueOf(relationToHallway.split(" ")[1]);
                            if (searchDirection.equals("Up") || searchDirection.equals("Right")) {
                                if (searchDirection.equals(directionToHallway) && orderToHallway == minOrderToSearch) {
                                    if (entry.getKey() instanceof Hallway) {
                                        //hallwaysToAvoid.add(hallway);
                                        traversedHallways.add(entry.getKey());
                                        ExtendedShape neighbor = findFirstShapeInHallwayFromLeft(entry.getKey(), traversedHallways, searchDirection, sh, hallway, hallwaysToAvoid);
                                        if (neighbor != null) {
                                            return neighbor;
                                        } else {
                                            ok = true;
                                            minOrderToSearch++;
                                        }
                                    } else {
                                        return entry.getKey();
                                    }
                                }
                            } else {
                                if (searchDirection.equals(directionToHallway) && orderToHallway > max) {
                                    max = orderToHallway;
                                    currentShape = entry.getKey();
                                }
                            }
                        }
                    }
                }
            }
        }
        if (searchDirection.equals("Down") || searchDirection.equals("Left")) {
            if (max != 0) {
                if (currentShape instanceof Hallway) {
                    //hallwaysToAvoid.add(hallway);
                    traversedHallways.add(currentShape);
                    ExtendedShape neighbor = findFirstShapeInHallwayFromLeft(currentShape, traversedHallways, searchDirection, sh, hallway, hallwaysToAvoid);
                    if (neighbor != null) {
                        return neighbor;
                    }
                } else {
                    return currentShape;
                }
            }
        }

        return null;
    }

    private ExtendedShape findRightNeighbor(ExtendedShape sh, String dth, int otw, Hallway hallway) {
        List<ExtendedShape> hallwaysToAvoid = new ArrayList<>();
        List<ExtendedShape> traversedHallways = new ArrayList<>();
        traversedHallways.add(hallway);
        String searchDirection = "";
        if (dth.equals("Left")) {
            searchDirection = "Up";
        } else if (dth.equals("Right")) {
            searchDirection = "Down";
        } else if (dth.equals("Down")) {
            searchDirection = "Up";
        } else if (dth.equals("Up")) {
            searchDirection = "Down";
        }
        int minOrderToSearch = 1;
        boolean ok = true;
        while (ok == true) {
            ok = false;
            for (Map.Entry<ExtendedShape, List<Pair<ExtendedShape, String>>> entry : graph.entrySet()) {
                if (entry.getKey() != hallway && entry.getKey() != sh) {
                    String relationToHallway = "";
                    String directionToHallway = "";
                    int orderToHallway = 0;
                    List<Pair<ExtendedShape, String>> list = graph.get(hallway);
                    for (Pair<ExtendedShape, String> pair : list) {
                        if (pair.getKey() == entry.getKey()) {
                            relationToHallway = pair.getValue();
                            directionToHallway = relationToHallway.split(" ")[0];
                            orderToHallway = Integer.valueOf(relationToHallway.split(" ")[1]);
                            if (searchDirection.equals("Up")) {
                                if (dth.equals(directionToHallway) && orderToHallway == otw + minOrderToSearch) {
                                    if (entry.getKey() instanceof Hallway) {
                                        //hallwaysToAvoid.add(hallway);
                                        traversedHallways.add(entry.getKey());
                                        ExtendedShape neighbor = findFirstShapeInHallwayFromRight(entry.getKey(), traversedHallways, dth, sh, hallway, hallwaysToAvoid);
                                        if (neighbor != null) {
                                            return neighbor;
                                        } else {
                                            minOrderToSearch++;
                                            ok = true;
                                        }
                                    } else {
                                        return entry.getKey();
                                    }

                                }
                            } else {
                                if (dth.equals(directionToHallway) && orderToHallway == otw - minOrderToSearch) {
                                    if (entry.getKey() instanceof Hallway) {
                                        // hallwaysToAvoid.add(hallway);
                                        traversedHallways.add(entry.getKey());
                                        ExtendedShape neighbor = findFirstShapeInHallwayFromRight(entry.getKey(), traversedHallways, dth, sh, hallway, hallwaysToAvoid);
                                        if (neighbor != null) {
                                            return neighbor;
                                        } else {
                                            minOrderToSearch++;
                                            ok = true;
                                        }
                                    } else {
                                        return entry.getKey();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (dth.equals("Left")) {
            searchDirection = "Down";
        } else if (dth.equals("Down")) {
            searchDirection = "Right";
        } else if (dth.equals("Up")) {
            searchDirection = "Left";
        } else if (dth.equals("Right")) {
            searchDirection = "Up";
        }

        int max = 0;
        ExtendedShape currentShape = null;
        minOrderToSearch = 1;
        ok = true;
        while (ok == true) {
            ok = false;
            for (Map.Entry<ExtendedShape, List<Pair<ExtendedShape, String>>> entry : graph.entrySet()) {
                if (entry.getKey() != hallway && entry.getKey() != sh) {
                    String relationToHallway = "";
                    String directionToHallway = "";
                    int orderToHallway = 0;
                    List<Pair<ExtendedShape, String>> list = graph.get(hallway);
                    for (Pair<ExtendedShape, String> pair : list) {
                        if (pair.getKey() == entry.getKey()) {
                            relationToHallway = pair.getValue();
                            directionToHallway = relationToHallway.split(" ")[0];
                            orderToHallway = Integer.valueOf(relationToHallway.split(" ")[1]);
                            if (searchDirection.equals("Down") || searchDirection.equals("Left")) {
                                if (searchDirection.equals(directionToHallway) && orderToHallway == minOrderToSearch) {
                                    if (entry.getKey() instanceof Hallway) {
                                        // hallwaysToAvoid.add(hallway);
                                        traversedHallways.add(entry.getKey());
                                        ExtendedShape neighbor = findFirstShapeInHallwayFromRight(entry.getKey(), traversedHallways, searchDirection, sh, hallway, hallwaysToAvoid);
                                        if (neighbor != null) {
                                            return neighbor;
                                        } else {
                                            minOrderToSearch++;
                                            ok = true;
                                        }
                                    } else {
                                        return entry.getKey();
                                    }
                                }
                            } else {
                                if (searchDirection.equals(directionToHallway) && orderToHallway > max) {
                                    max = orderToHallway;
                                    currentShape = entry.getKey();
                                }
                            }
                        }
                    }
                }
            }
        }
        if (searchDirection.equals("Up") || searchDirection.equals("Right")) {
            if (max != 0) {
                if (currentShape instanceof Hallway) {
                    // hallwaysToAvoid.add(hallway);
                    traversedHallways.add(currentShape);
                    ExtendedShape neighbor = findFirstShapeInHallwayFromRight(currentShape, traversedHallways, searchDirection, sh, hallway, hallwaysToAvoid);
                    if (neighbor != null) {
                        return neighbor;
                    }
                } else if (currentShape != null) {
                    return currentShape;
                }
            }
        }
        if (searchDirection.equals("Left")) {
            searchDirection = "Down";
        } else if (searchDirection.equals("Down")) {
            searchDirection = "Right";
        } else if (searchDirection.equals("Up")) {
            searchDirection = "Left";
        } else if (searchDirection.equals("Right")) {
            searchDirection = "Up";
        }

        max = 0;
        currentShape = null;
        minOrderToSearch = 1;
        ok = true;
        while (ok == true) {
            ok = false;
            for (Map.Entry<ExtendedShape, List<Pair<ExtendedShape, String>>> entry : graph.entrySet()) {
                if (entry.getKey() != hallway && entry.getKey() != sh) {
                    String relationToHallway = "";
                    String directionToHallway = "";
                    int orderToHallway = 0;
                    List<Pair<ExtendedShape, String>> list = graph.get(hallway);
                    for (Pair<ExtendedShape, String> pair : list) {
                        if (pair.getKey() == entry.getKey()) {
                            relationToHallway = pair.getValue();
                            directionToHallway = relationToHallway.split(" ")[0];
                            orderToHallway = Integer.valueOf(relationToHallway.split(" ")[1]);
                            if (searchDirection.equals("Down") || searchDirection.equals("Left")) {
                                if (searchDirection.equals(directionToHallway) && orderToHallway == minOrderToSearch) {
                                    if (entry.getKey() instanceof Hallway) {
                                        //  hallwaysToAvoid.add(hallway);
                                        traversedHallways.add(entry.getKey());
                                        ExtendedShape neighbor = findFirstShapeInHallwayFromRight(entry.getKey(), traversedHallways, searchDirection, sh, hallway, hallwaysToAvoid);
                                        if (neighbor != null) {
                                            return neighbor;
                                        } else {
                                            minOrderToSearch++;
                                            ok = true;
                                        }
                                    } else {
                                        return entry.getKey();
                                    }
                                }
                            } else {
                                if (searchDirection.equals(directionToHallway) && orderToHallway > max) {
                                    max = orderToHallway;
                                    currentShape = entry.getKey();
                                }
                            }
                        }
                    }
                }
            }
        }
        if (searchDirection.equals("Up") || searchDirection.equals("Right")) {
            if (max != 0) {
                if (currentShape instanceof Hallway) {
                    //hallwaysToAvoid.add(hallway);
                    traversedHallways.add(currentShape);
                    ExtendedShape neighbor = findFirstShapeInHallwayFromRight(currentShape, traversedHallways, searchDirection, sh, hallway, hallwaysToAvoid);
                    if (neighbor != null) {
                        return neighbor;
                    }
                } else if (currentShape != null) {
                    return currentShape;
                }
            }
        }
        if (searchDirection.equals("Left")) {
            searchDirection = "Down";
        } else if (searchDirection.equals("Down")) {
            searchDirection = "Right";
        } else if (searchDirection.equals("Up")) {
            searchDirection = "Left";
        } else if (searchDirection.equals("Right")) {
            searchDirection = "Up";
        }
        max = 0;
        currentShape = null;
        minOrderToSearch = 1;
        ok = true;
        while (ok == true) {
            ok = false;
            for (Map.Entry<ExtendedShape, List<Pair<ExtendedShape, String>>> entry : graph.entrySet()) {
                if (entry.getKey() != hallway && entry.getKey() != sh) {
                    String relationToHallway = "";
                    String directionToHallway = "";
                    int orderToHallway = 0;
                    List<Pair<ExtendedShape, String>> list = graph.get(hallway);
                    for (Pair<ExtendedShape, String> pair : list) {
                        if (pair.getKey() == entry.getKey()) {
                            relationToHallway = pair.getValue();
                            directionToHallway = relationToHallway.split(" ")[0];
                            orderToHallway = Integer.valueOf(relationToHallway.split(" ")[1]);
                            if (searchDirection.equals("Down") || searchDirection.equals("Left")) {
                                if (searchDirection.equals(directionToHallway) && orderToHallway == minOrderToSearch) {
                                    if (entry.getKey() instanceof Hallway) {
                                        // hallwaysToAvoid.add(hallway);
                                        traversedHallways.add(entry.getKey());
                                        ExtendedShape neighbor = findFirstShapeInHallwayFromRight(entry.getKey(), traversedHallways, searchDirection, sh, hallway, hallwaysToAvoid);
                                        if (neighbor != null) {
                                            return neighbor;
                                        } else {
                                            minOrderToSearch++;
                                            ok = true;
                                        }
                                    } else {
                                        return entry.getKey();
                                    }
                                }
                            } else {
                                if (searchDirection.equals(directionToHallway) && orderToHallway > max) {
                                    max = orderToHallway;
                                    currentShape = entry.getKey();
                                }
                            }
                        }
                    }
                }
            }
        }
        if (searchDirection.equals("Up") || searchDirection.equals("Right")) {
            if (max != 0) {
                if (currentShape instanceof Hallway) {
                    hallwaysToAvoid.add(hallway);
                    traversedHallways.add(currentShape);
                    ExtendedShape neighbor = findFirstShapeInHallwayFromRight(currentShape, traversedHallways, searchDirection, sh, hallway, hallwaysToAvoid);
                    if (neighbor != null) {
                        return neighbor;
                    }
                } else {
                    return currentShape;
                }
            }
        }

        return null;
    }

    private ExtendedShape findStraightNeighbor(ExtendedShape sh, String dth, Hallway hallway) {
        String searchDirection = "";
        if (dth.equals("Left")) {
            searchDirection = "Right";
        } else if (dth.equals("Right")) {
            searchDirection = "Left";
        } else if (dth.equals("Down")) {
            searchDirection = "Up";
        } else if (dth.equals("Up")) {
            searchDirection = "Down";
        }
        ExtendedRectangle castedShape = (ExtendedRectangle) sh;
        //  double minDistance = Double.MAX_VALUE;
        //  ExtendedShape currentShape = null;
        for (Map.Entry<ExtendedShape, List<Pair<ExtendedShape, String>>> entry : graph.entrySet()) {
            if (entry.getKey() != hallway && entry.getKey() != sh) {
                String relationToHallway = "";
                String directionToHallway = "";
                // int orderToHallway = 0;
                List<Pair<ExtendedShape, String>> list = graph.get(hallway);
                for (Pair<ExtendedShape, String> pair : list) {
                    if (pair.getKey() == entry.getKey()) {
                        relationToHallway = pair.getValue();
                        directionToHallway = relationToHallway.split(" ")[0];
                        //  orderToHallway = Integer.valueOf(relationToHallway.split(" ")[1]);
                        if (searchDirection.equals(directionToHallway)) {
                            if (entry.getKey() instanceof Hallway) {
                                Hallway hall = (Hallway) entry.getKey();
                                boolean ok = false;
                                if (searchDirection.equals("Left") || searchDirection.equals("Right")) {
                                    if (castedShape.getCenterPoint().getY() + castedShape.getLength() / 2 > hall.getCenterPoint().getY() && castedShape.getCenterPoint().getY() + castedShape.getLength() / 2 < hall.getCenterPoint().getY() + hall.getLength()) {
                                        ok = true;
                                    }
                                } else {
                                    if (castedShape.getCenterPoint().getX() + castedShape.getWidth() / 2 > hall.getCenterPoint().getX() && castedShape.getCenterPoint().getX() + castedShape.getWidth() / 2 < hall.getCenterPoint().getX() + hall.getWidth()) {
                                        ok = true;
                                    }
                                }
                                if (ok == true) {
                                    ExtendedShape neighbor = findFirstShapeInHallwayFromStraight(entry.getKey(), searchDirection, sh);
                                    if (neighbor != null) {
                                        return neighbor;
                                    }
                                }
                            } else {
                                double distanceBetweenCenters;
                                if (searchDirection.equals("Left") || searchDirection.equals("Right")) {
                                    distanceBetweenCenters = Math.abs((((ExtendedRectangle) pair.getKey()).centerPoint.getY() + ((ExtendedRectangle) pair.getKey()).getLength() / 2) - ((((ExtendedRectangle) sh).getCenterPoint().getY()) + ((ExtendedRectangle) sh).getLength() / 2));
                                } else {
                                    distanceBetweenCenters = Math.abs((((ExtendedRectangle) pair.getKey()).centerPoint.getX() + ((ExtendedRectangle) pair.getKey()).getWidth() / 2) - ((((ExtendedRectangle) sh).getCenterPoint().getX()) + ((ExtendedRectangle) sh).getWidth() / 2));
                                }
                                if (distanceBetweenCenters < 25) {
                                    return entry.getKey();
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private Hallway findShapeHallway(ExtendedShape shape) {
        if (((ExtendedRectangle) shape).getHallway() != null) {
            return ((ExtendedRectangle) shape).getHallway();
        }
        for (Pair<ExtendedShape, String> list : graph.get(shape)) {
            if (list.getKey() instanceof Hallway) {
                return (Hallway) list.getKey();
            }
        }
        return null;
    }

    // hallway - holul in care tre sa caut vecinul
    // dth - directia holului in care tre sa caut vecinul fata de holul initial
    // shape - forma la care tre sa gasesc vecinul
    // hallwayToAvoid - holul initial
    // otw - al catalea e holul in care caut fata de holul initial
    private ExtendedShape findFirstShapeInHallwayFromLeft(ExtendedShape hallway, List<ExtendedShape> traversedHallways, String dth, ExtendedShape shape, ExtendedShape hallwayToAvoid, List<ExtendedShape> hallwaysToAvoid) {
        int otw = 0;
        int otwMin = 0;
        int otwMax = 0;
        String dth2 = "";
        if(dth.equals("Down")) {
            dth2 = "Up";
        } else if(dth.equals("Up")) {
            dth2 = "Down";
        } else if(dth.equals("Left")) {
            dth2 = "Right";
        } else if(dth.equals("Right")) {
            dth2 = "Left";
        }
        for (Pair<ExtendedShape, String> pair : graph.get(hallway)) {
            if (pair.getKey() == hallwayToAvoid && pair.getValue().split(" ")[0].equals(dth2)) {
                //relationToHallway2 = pair.getValue();
                //directionToHallway2 = relationToHallway2.split(" ")[0];
                otwMin = Integer.valueOf(pair.getValue().split(" ")[1]);
            } else if (pair.getKey() == shape && pair.getValue().split(" ")[0].equals(dth2)) {
                otwMax = Integer.valueOf(pair.getValue().split(" ")[1]);
            }
        }
        if (otwMin > otwMax) {
            otw = otwMin;
            otwMin = otwMax;
            otwMax = otw;
        }
        List<String> searchDirections = new ArrayList<>();

        if (dth.equals("Left")) {
            searchDirections.add("Right");
            searchDirections.add("Down");
            searchDirections.add("Left");
            searchDirections.add("Up");
        } else if (dth.equals("Up")) {
            searchDirections.add("Down");
            searchDirections.add("Left");
            searchDirections.add("Up");
            searchDirections.add("Right");
        } else if (dth.equals("Right")) {
            searchDirections.add("Left");
            searchDirections.add("Up");
            searchDirections.add("Right");
            searchDirections.add("Down");
        } else if (dth.equals("Down")) {
            searchDirections.add("Up");
            searchDirections.add("Right");
            searchDirections.add("Down");
            searchDirections.add("Left");
        }

        List<String> downDirections = new ArrayList<>();

        if (dth.equals("Right")) {
            //downDirections.add("Left");
            downDirections.add("Up");
            downDirections.add("Right");
        } else if (dth.equals("Left")) {
            downDirections.add("Up");
            downDirections.add("Right");
        } else if (dth.equals("Up")) {
            //downDirections.add("Down");
            //downDirections.add("Left");
            downDirections.add("Up");
            downDirections.add("Right");
        } else if (dth.equals("Down")) {
            downDirections.add("Up");
            downDirections.add("Right");
        }
        for (String s : searchDirections) {
            if (downDirections.contains(s)) {
                if (otwMax != 0) {
                    otw = otwMax;
                } else {
                    otw = otwMin;
                }
            } else {
                if (otwMin != 0) {
                    otw = otwMin;
                } else {
                    otw = otwMax;
                }
            }
            int max = 0;
            int minOrderToSearch = 1;
            ExtendedShape currentShape = null;
            String currentShapeDirectionToHallway = "";
            for (Map.Entry<ExtendedShape, List<Pair<ExtendedShape, String>>> entry : graph.entrySet()) {
                boolean ok = false;
                for (Pair<ExtendedShape, String> pair : graph.get(hallway)) {
                    if (pair.getKey() == entry.getKey()) {
                        ok = true;
                    }
                }
                String relationToHallway2 = "";
                String directionToHallway2 = "";
                int orderToHallway2 = 0;
                List<Pair<ExtendedShape, String>> list2 = graph.get(hallway);
                for (Pair<ExtendedShape, String> pair : list2) {
                    if (pair.getKey() == hallwayToAvoid || pair.getKey() == shape) {
                        relationToHallway2 = pair.getValue();
                        directionToHallway2 = relationToHallway2.split(" ")[0];
                        orderToHallway2 = Integer.valueOf(relationToHallway2.split(" ")[1]);
                        if (orderToHallway2 == 1 && directionToHallway2.equals(s)) {
                            minOrderToSearch = 2;
                        }
                    }
                }

                if (entry.getKey() != hallway && entry.getKey() != shape && ok == true && !(hallwaysToAvoid.contains(entry.getKey())) && !(traversedHallways.contains(entry.getKey()))) {
                    String relationToHallway = "";
                    String directionToHallway = "";
                    int orderToHallway = 0;
                    List<Pair<ExtendedShape, String>> list = graph.get(hallway);
                    for (Pair<ExtendedShape, String> pair : list) {
                        if (pair.getKey() == entry.getKey()) {
                            relationToHallway = pair.getValue();
                            directionToHallway = relationToHallway.split(" ")[0];
                            orderToHallway = Integer.valueOf(relationToHallway.split(" ")[1]);
                            if (downDirections.contains(s)) {
                                if (s.equals(searchDirections.get(0))) {
                                    if (s.equals(directionToHallway) && orderToHallway == otw + 1) {
                                        if (entry.getKey() instanceof Hallway) {
                                            //hallwaysToAvoid.add(hallway);
                                            traversedHallways.add(entry.getKey());
                                            ExtendedShape neighbor = findFirstShapeInHallwayFromLeft(entry.getKey(), traversedHallways, directionToHallway, shape, traversedHallways.get(traversedHallways.size() - 1), hallwaysToAvoid);
                                            if (neighbor != null) {
                                                return neighbor;
                                            }
                                        } else {
                                            return entry.getKey();
                                        }
                                    }
                                } else {
                                    if (s.equals(directionToHallway) && orderToHallway == minOrderToSearch) {
                                        if (entry.getKey() instanceof Hallway) {
                                            //  hallwaysToAvoid.add(hallway);
                                            traversedHallways.add(entry.getKey());
                                            ExtendedShape neighbor = findFirstShapeInHallwayFromLeft(entry.getKey(), traversedHallways, directionToHallway, shape, traversedHallways.get(traversedHallways.size() - 1), hallwaysToAvoid);
                                            if (neighbor != null) {
                                                return neighbor;
                                            }
                                        } else {
                                            return entry.getKey();
                                        }
                                    }
                                }
                            } else {
                                if (s.equals(searchDirections.get(0))) {
                                    if (s.equals(directionToHallway) && orderToHallway == otw - 1) {
                                        if (entry.getKey() instanceof Hallway) {
                                            //hallwaysToAvoid.add(hallway);
                                            traversedHallways.add(entry.getKey());
                                            ExtendedShape neighbor = findFirstShapeInHallwayFromLeft(entry.getKey(), traversedHallways, directionToHallway, shape, traversedHallways.get(traversedHallways.size() - 1), hallwaysToAvoid);
                                            if (neighbor != null) {
                                                return neighbor;
                                            }
                                        } else {
                                            return entry.getKey();
                                        }
                                    }
                                } else {
                                    if (s.equals(directionToHallway) && orderToHallway > max && entry.getKey() != hallwayToAvoid && entry.getKey() != shape) {
                                        max = orderToHallway;
                                        currentShape = entry.getKey();
                                        currentShapeDirectionToHallway = directionToHallway;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!(downDirections.contains(s))) {
                if (max != 0) {
                    if (currentShape instanceof Hallway) {
                        //hallwaysToAvoid.add(hallway);
                        traversedHallways.add(currentShape);
                        ExtendedShape neighbor = findFirstShapeInHallwayFromLeft(currentShape, traversedHallways, currentShapeDirectionToHallway, shape, traversedHallways.get(traversedHallways.size() - 1), hallwaysToAvoid);
                        if (neighbor != null) {
                            return neighbor;
                        }
                    } else {
                        return currentShape;
                    }
                }
            }
        }
        hallwaysToAvoid.add(hallway);
        traversedHallways.remove(hallway);
        if (!(traversedHallways.isEmpty())) {
            if (dth.equals("Left")) {
                dth = "Right";
            } else if (dth.equals("Right")) {
                dth = "Left";
            } else if (dth.equals("Down")) {
                dth = "Up";
            } else if (dth.equals("Up")) {
                dth = "Down";
            }
            return findFirstShapeInHallwayFromLeft(traversedHallways.get(traversedHallways.size() - 1), traversedHallways, dth, shape, hallwaysToAvoid.get(hallwaysToAvoid.size() - 1), hallwaysToAvoid);
        }
        return null;
    }

    // hallway - holul in care tre sa caut vecinul
    // dth - directia holului in care tre sa caut vecinul fata de holul initial
    // shape - forma la care tre sa gasesc vecinul
    // hallwayToAvoid - holul initial
    // otw - al catalea e holul in care caut fata de holul initial
    private ExtendedShape findFirstShapeInHallwayFromRight(ExtendedShape hallway, List<ExtendedShape> traversedHallways, String dth, ExtendedShape shape, ExtendedShape hallwayToAvoid, List<ExtendedShape> hallwaysToAvoid) {
        int otw = 0;
        int otwMin = 0;
        int otwMax = 0;
        String dth2 = "";
        if(dth.equals("Down")) {
            dth2 = "Up";
        } else if(dth.equals("Up")) {
            dth2 = "Down";
        } else if(dth.equals("Left")) {
            dth2 = "Right";
        } else if(dth.equals("Right")) {
            dth2 = "Left";
        }
        for (Pair<ExtendedShape, String> pair : graph.get(hallway)) {
            if (pair.getKey() == hallwayToAvoid && pair.getValue().split(" ")[0].equals(dth2)) {
                //relationToHallway2 = pair.getValue();
                //directionToHallway2 = relationToHallway2.split(" ")[0];
                otwMin = Integer.valueOf(pair.getValue().split(" ")[1]);
            } else if (pair.getKey() == shape && pair.getValue().split(" ")[0].equals(dth2)) {
                otwMax = Integer.valueOf(pair.getValue().split(" ")[1]);
            }
        }
        if (otwMin > otwMax) {
            otw = otwMin;
            otwMin = otwMax;
            otwMax = otw;
        }

        List<String> searchDirections = new ArrayList<>();
        if (dth.equals("Left")) { //ASTA
            searchDirections.add("Right"); //ASTA
            searchDirections.add("Up");
            searchDirections.add("Left");
            searchDirections.add("Down");
        } else if (dth.equals("Up")) {
            searchDirections.add("Down");
            searchDirections.add("Right");
            searchDirections.add("Up");
            searchDirections.add("Left");
        } else if (dth.equals("Right")) {
            searchDirections.add("Left");
            searchDirections.add("Down");
            searchDirections.add("Right");
            searchDirections.add("Up");
        } else if (dth.equals("Down")) {
            searchDirections.add("Up");
            searchDirections.add("Left");
            searchDirections.add("Down");
            searchDirections.add("Right");
        }
        List<String> downDirections = new ArrayList<>();
        if (dth.equals("Right")) {
            //downDirections.add("Left");
            downDirections.add("Left");
            downDirections.add("Down");
        } else if (dth.equals("Left")) {
            downDirections.add("Left");
            downDirections.add("Down");
        } else if (dth.equals("Up")) {
            downDirections.add("Down");
            downDirections.add("Left");
        } else if (dth.equals("Down")) {
            //downDirections.add("Up");
            downDirections.add("Left");
            downDirections.add("Down");
        }

        for (String s : searchDirections) {
            if (downDirections.contains(s)) {
                otw = otwMax;
            } else {
                if (otwMin != 0) {
                    otw = otwMin;
                } else {
                    otw = otwMax;
                }
            }
            int max = 0;
            int minOrderToSearch = 1;
            ExtendedShape currentShape = null;
            String currentShapeDirectionToHallway = "";
            for (Map.Entry<ExtendedShape, List<Pair<ExtendedShape, String>>> entry : graph.entrySet()) {
                boolean ok = false;
                for (Pair<ExtendedShape, String> pair : graph.get(hallway)) {
                    if (pair.getKey() == entry.getKey()) {
                        ok = true;
                    }
                }
                String relationToHallway2 = "";
                String directionToHallway2 = "";
                int orderToHallway2 = 0;
                List<Pair<ExtendedShape, String>> list2 = graph.get(hallway);
                for (Pair<ExtendedShape, String> pair : list2) {
                    if (pair.getKey() == hallwayToAvoid || pair.getKey() == shape) {
                        relationToHallway2 = pair.getValue();
                        directionToHallway2 = relationToHallway2.split(" ")[0];
                        orderToHallway2 = Integer.valueOf(relationToHallway2.split(" ")[1]);
                        if (orderToHallway2 == 1 && directionToHallway2.equals(s)) {
                            minOrderToSearch = 2;
                        }
                    }
                }

                if (entry.getKey() != hallway && entry.getKey() != shape && ok == true && !(hallwaysToAvoid.contains(entry.getKey())) && !(traversedHallways.contains(entry.getKey()))) {
                    String relationToHallway = "";
                    String directionToHallway = "";
                    int orderToHallway = 0;
                    List<Pair<ExtendedShape, String>> list = graph.get(hallway);
                    for (Pair<ExtendedShape, String> pair : list) {
                        if (pair.getKey() == entry.getKey()) {
                            relationToHallway = pair.getValue();
                            directionToHallway = relationToHallway.split(" ")[0];
                            orderToHallway = Integer.valueOf(relationToHallway.split(" ")[1]);
                            if (downDirections.contains(s)) {
                                if (s.equals(searchDirections.get(0))) {
                                    if (s.equals(directionToHallway) && orderToHallway == otw + 1) {
                                        if (entry.getKey() instanceof Hallway) {
                                            //   hallwaysToAvoid.add(hallway);
                                            traversedHallways.add(entry.getKey());
                                            ExtendedShape neighbor = findFirstShapeInHallwayFromRight(entry.getKey(), traversedHallways, directionToHallway, shape, hallway, hallwaysToAvoid);
                                            if (neighbor != null) {
                                                return neighbor;
                                            }
                                        } else {
                                            return entry.getKey();
                                        }
                                    }
                                } else {
                                    if (s.equals(directionToHallway) && orderToHallway == minOrderToSearch) {
                                        if (entry.getKey() instanceof Hallway) {
                                            // hallwaysToAvoid.add(hallway);
                                            traversedHallways.add(entry.getKey());
                                            ExtendedShape neighbor = findFirstShapeInHallwayFromRight(entry.getKey(), traversedHallways, directionToHallway, shape, hallway, hallwaysToAvoid);
                                            if (neighbor != null) {
                                                return neighbor;
                                            }
                                        } else {
                                            return entry.getKey();
                                        }
                                    }
                                }
                            } else {
                                if (s.equals(searchDirections.get(0))) {
                                    if (s.equals(directionToHallway) && orderToHallway == otw - 1) {
                                        if (entry.getKey() instanceof Hallway) {
                                            //  hallwaysToAvoid.add(hallway);
                                            traversedHallways.add(entry.getKey());
                                            ExtendedShape neighbor = findFirstShapeInHallwayFromRight(entry.getKey(), traversedHallways, directionToHallway, shape, hallway, hallwaysToAvoid);
                                            if (neighbor != null) {
                                                return neighbor;
                                            }
                                        } else {
                                            return entry.getKey();
                                        }
                                    }
                                } else {
                                    if (s.equals(directionToHallway) && orderToHallway > max && entry.getKey() != hallwayToAvoid && entry.getKey() != shape) {
                                        max = orderToHallway;
                                        currentShape = entry.getKey();
                                        currentShapeDirectionToHallway = directionToHallway;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!(downDirections.contains(s))) {
                if (max != 0) {
                    if (currentShape instanceof Hallway) {
                        // hallwaysToAvoid.add(hallway);
                        traversedHallways.add(currentShape);
                        ExtendedShape neighbor = findFirstShapeInHallwayFromRight(currentShape, traversedHallways, currentShapeDirectionToHallway, shape, hallway, hallwaysToAvoid);
                        if (neighbor != null) {
                            return neighbor;
                        }
                    } else {
                        return currentShape;
                    }
                }
            }
        }
        hallwaysToAvoid.add(hallway);
        traversedHallways.remove(hallway);
        if (!(traversedHallways.isEmpty())) {
            if (dth.equals("Left")) {
                dth = "Right";
            } else if (dth.equals("Right")) {
                dth = "Left";
            } else if (dth.equals("Down")) {
                dth = "Up";
            } else if (dth.equals("Up")) {
                dth = "Down";
            }
            return findFirstShapeInHallwayFromRight(traversedHallways.get(traversedHallways.size() - 1), traversedHallways, dth, shape, hallwaysToAvoid.get(hallwaysToAvoid.size() - 1), hallwaysToAvoid);
        }
        return null;
    }

    private ExtendedShape findFirstShapeInHallwayFromStraight(ExtendedShape hallway, String searchDirection, ExtendedShape sh) {
        ExtendedRectangle castedShape = (ExtendedRectangle) sh;
        List<Pair<ExtendedShape, String>> list = graph.get(hallway);
        if (list != null) {
            for (Pair<ExtendedShape, String> pair : list) {
                String relationToHallway = pair.getValue();
                String directionToHallway = relationToHallway.split(" ")[0];
                //int orderToHallway = Integer.valueOf(relationToHallway.split(" ")[1]);
                double distanceBetweenCenters;
                if (searchDirection.equals("Left") || searchDirection.equals("Right")) {
                    distanceBetweenCenters = Math.abs((((ExtendedRectangle) pair.getKey()).centerPoint.getY() + ((ExtendedRectangle) pair.getKey()).getLength() / 2) - ((((ExtendedRectangle) sh).getCenterPoint().getY()) + ((ExtendedRectangle) sh).getLength() / 2));
                } else {
                    distanceBetweenCenters = Math.abs((((ExtendedRectangle) pair.getKey()).centerPoint.getX() + ((ExtendedRectangle) pair.getKey()).getWidth() / 2) - ((((ExtendedRectangle) sh).getCenterPoint().getX()) + ((ExtendedRectangle) sh).getWidth() / 2));
                }
                if (directionToHallway.equals(searchDirection)) {
                    if (pair.getKey() instanceof Hallway) {
                        Hallway hall = (Hallway) hallway;
                        boolean ok = false;
                        if (searchDirection.equals("Left") || searchDirection.equals("Right")) {
                            //(((ExtendedRectangle) sh).centerPoint.getY() < ((ExtendedRectangle) pair.getKey()).centerPoint.getY() && ((ExtendedRectangle) sh).centerPoint.getY() + ((ExtendedRectangle) sh).getLength() > ((ExtendedRectangle) pair.getKey()).centerPoint.getY() + ((ExtendedRectangle) pair.getKey()).getLength()) || (((ExtendedRectangle) sh).getCenterPoint().getY() > ((ExtendedRectangle) pair.getKey()).centerPoint.getY() && ((ExtendedRectangle) sh).getCenterPoint().getY() < ((ExtendedRectangle) pair.getKey()).centerPoint.getY() + ((ExtendedRectangle) pair.getKey()).getLength()) || (((ExtendedRectangle) sh).getCenterPoint().getY() + ((ExtendedRectangle) sh).getLength() > ((ExtendedRectangle) pair.getKey()).centerPoint.getY() && ((ExtendedRectangle) sh).getCenterPoint().getY() + ((ExtendedRectangle) sh).getLength() < ((ExtendedRectangle) pair.getKey()).centerPoint.getY() + ((ExtendedRectangle) pair.getKey()).getLength())
                            if (castedShape.getCenterPoint().getY() + castedShape.getLength() / 2 > hall.getCenterPoint().getY() && castedShape.getCenterPoint().getY() + castedShape.getLength() / 2 < hall.getCenterPoint().getY() + hall.getLength()) {
                                ok = true;
                            }
                            //(((ExtendedRectangle) sh).centerPoint.getX() < ((ExtendedRectangle) pair.getKey()).centerPoint.getX() && ((ExtendedRectangle) sh).centerPoint.getX() + ((ExtendedRectangle) sh).getWidth() > ((ExtendedRectangle) pair.getKey()).centerPoint.getX() + ((ExtendedRectangle) pair.getKey()).getWidth()) || (((ExtendedRectangle) sh).getCenterPoint().getX() > ((ExtendedRectangle) pair.getKey()).centerPoint.getX() && ((ExtendedRectangle) sh).getCenterPoint().getX() < ((ExtendedRectangle) pair.getKey()).centerPoint.getX() + ((ExtendedRectangle) pair.getKey()).getWidth()) || (((ExtendedRectangle) sh).getCenterPoint().getX() + ((ExtendedRectangle) sh).getWidth() > ((ExtendedRectangle) pair.getKey()).centerPoint.getX() && ((ExtendedRectangle) sh).getCenterPoint().getX() + ((ExtendedRectangle) sh).getWidth() < ((ExtendedRectangle) pair.getKey()).centerPoint.getX() + ((ExtendedRectangle) pair.getKey()).getWidth())
                        } else {
                            if (castedShape.getCenterPoint().getX() + castedShape.getWidth() / 2 > hall.getCenterPoint().getX() && castedShape.getCenterPoint().getX() + castedShape.getWidth() / 2 < hall.getCenterPoint().getX() + hall.getWidth()) {
                                ok = true;
                            }
                        }
                        if (ok == true) {
                            ExtendedShape neighbor = findFirstShapeInHallwayFromStraight(pair.getKey(), searchDirection, sh);
                            if (neighbor != null) {
                                return neighbor;
                            }
                        }
                    } else {
                        if (distanceBetweenCenters < 25) {
                            return pair.getKey();
                        }
                    }
                }
            }
        }
        return null;
    }
}
