/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.util.Pair;

/**
 * @author Alex
 */
public class Graph implements Serializable {

    static final long serialVersionUID = 1L;

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
                    ExtendedShape straightNeighbor = findStraightNeighbor(entry.getKey(), directionToHallway, orderToHallway, hallway);
                    if(neighborGraph.get(straightNeighbor) != null)
                    {
                        for(Pair<ExtendedShape, String> pair : neighborGraph.get(straightNeighbor)) {
                            if(pair.getValue().equals("Straight")) {
                                straightNeighbor = null;
                            }
                        }
                    }
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
                                        hallwaysToAvoid.add(hallway);
                                        ExtendedShape neighbor = findFirstShapeInHallwayFromLeft(entry.getKey(), dth, sh, hallway, hallwaysToAvoid);
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
                                        hallwaysToAvoid.add(hallway);
                                        ExtendedShape neighbor = findFirstShapeInHallwayFromLeft(entry.getKey(), dth, sh, hallway, hallwaysToAvoid);
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
                                        hallwaysToAvoid.add(hallway);
                                        ExtendedShape neighbor = findFirstShapeInHallwayFromLeft(entry.getKey(), searchDirection, sh, hallway, hallwaysToAvoid);
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
                            // System.out.println(((ExtendedRectangle)entry.getKey()).getId() + " " + relationToHallway);
                        }
                    }
                }
            }
        }
        if (searchDirection.equals("Down") || searchDirection.equals("Left")) {
            if (max != 0) {
                if (currentShape instanceof Hallway) {
                    hallwaysToAvoid.add(hallway);
                    ExtendedShape neighbor = findFirstShapeInHallwayFromLeft(currentShape, searchDirection, sh, hallway, hallwaysToAvoid);
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
                                        hallwaysToAvoid.add(hallway);
                                        ExtendedShape neighbor = findFirstShapeInHallwayFromLeft(entry.getKey(), searchDirection, sh, hallway, hallwaysToAvoid);
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
                            // System.out.println(((ExtendedRectangle)entry.getKey()).getId() + " " + relationToHallway);
                        }
                    }
                }
            }
        }
        if (searchDirection.equals("Down") || searchDirection.equals("Left")) {
            if (max != 0) {
                if (currentShape instanceof Hallway) {
                    hallwaysToAvoid.add(hallway);
                    ExtendedShape neighbor = findFirstShapeInHallwayFromLeft(currentShape, searchDirection, sh, hallway, hallwaysToAvoid);
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
                                        hallwaysToAvoid.add(hallway);
                                        ExtendedShape neighbor = findFirstShapeInHallwayFromLeft(entry.getKey(), searchDirection, sh, hallway, hallwaysToAvoid);
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
                            // System.out.println(((ExtendedRectangle)entry.getKey()).getId() + " " + relationToHallway);
                        }
                    }
                }
            }
        }
        if (searchDirection.equals("Down") || searchDirection.equals("Left")) {
            if (max != 0) {
                if (currentShape instanceof Hallway) {
                    hallwaysToAvoid.add(hallway);
                    ExtendedShape neighbor = findFirstShapeInHallwayFromLeft(currentShape, searchDirection, sh, hallway, hallwaysToAvoid);
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
        // System.out.println(((ExtendedRectangle) sh).getName());
        List<ExtendedShape> hallwaysToAvoid = new ArrayList<>();
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
                                        hallwaysToAvoid.add(hallway);
                                        ExtendedShape neighbor = findFirstShapeInHallwayFromRight(entry.getKey(), dth, sh, hallway, hallwaysToAvoid);
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
                                        hallwaysToAvoid.add(hallway);
                                        ExtendedShape neighbor = findFirstShapeInHallwayFromRight(entry.getKey(), dth, sh, hallway, hallwaysToAvoid);
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
                            // System.out.println(((ExtendedRectangle)entry.getKey()).getId() + " " + relationToHallway);
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
                                        hallwaysToAvoid.add(hallway);
                                        ExtendedShape neighbor = findFirstShapeInHallwayFromRight(entry.getKey(), searchDirection, sh, hallway, hallwaysToAvoid);
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
                    ExtendedShape neighbor = findFirstShapeInHallwayFromRight(currentShape, searchDirection, sh, hallway, hallwaysToAvoid);
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
                                        hallwaysToAvoid.add(hallway);
                                        ExtendedShape neighbor = findFirstShapeInHallwayFromRight(entry.getKey(), searchDirection, sh, hallway, hallwaysToAvoid);
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
                    ExtendedShape neighbor = findFirstShapeInHallwayFromRight(currentShape, searchDirection, sh, hallway, hallwaysToAvoid);
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
                                        hallwaysToAvoid.add(hallway);
                                        ExtendedShape neighbor = findFirstShapeInHallwayFromRight(entry.getKey(), searchDirection, sh, hallway, hallwaysToAvoid);
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
                    ExtendedShape neighbor = findFirstShapeInHallwayFromRight(currentShape, searchDirection, sh, hallway, hallwaysToAvoid);
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

    private ExtendedShape findStraightNeighbor(ExtendedShape sh, String dth, int otw, Hallway hallway) {
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

        //  double minDistance = Double.MAX_VALUE;
        //  ExtendedShape currentShape = null;
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
                        if (searchDirection.equals(directionToHallway) && orderToHallway == otw) {
                            if (entry.getKey() instanceof Hallway) {
                                ExtendedShape neighbor = findFirstShapeInHallwayFromStraight(entry.getKey(), searchDirection, sh, hallway);
                                if (neighbor != null) {
                                    return neighbor;
                                }
                            } else {
                                return entry.getKey();
                            }
                        }
                        // System.out.println(((ExtendedRectangle)entry.getKey()).getId() + " " + relationToHallway);
                    }
                }
            }
        }
        return null;
    }

    private Hallway findShapeHallway(ExtendedShape shape) {
        for (Pair<ExtendedShape, String> list : graph.get(shape)) {
            if (list.getKey() instanceof Hallway) {
              //  System.out.println(((ExtendedRectangle) shape).getName() + " " + ((ExtendedRectangle) list.getKey()).getName());
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
    private ExtendedShape findFirstShapeInHallwayFromLeft(ExtendedShape hallway, String dth, ExtendedShape shape, ExtendedShape hallwayToAvoid, List<ExtendedShape> hallwaysToAvoid) {
        int otw = 0;
        int otwMin = 0;
        int otwMax = 0;
        for (Pair<ExtendedShape, String> pair : graph.get(hallway)) {
            if (pair.getKey() == hallwayToAvoid) {
                //relationToHallway2 = pair.getValue();
                //directionToHallway2 = relationToHallway2.split(" ")[0];
                otwMin = Integer.valueOf(pair.getValue().split(" ")[1]);
            } else if (pair.getKey() == shape) {
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

                if (entry.getKey() != hallway && entry.getKey() != shape && ok == true && !(hallwaysToAvoid.contains(entry.getKey()))) {
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
                                            hallwaysToAvoid.add(hallway);
                                            ExtendedShape neighbor = findFirstShapeInHallwayFromLeft(entry.getKey(), directionToHallway, shape, hallway, hallwaysToAvoid);
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
                                            hallwaysToAvoid.add(hallway);
                                            ExtendedShape neighbor = findFirstShapeInHallwayFromLeft(entry.getKey(), directionToHallway, shape, hallway, hallwaysToAvoid);
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
                                            hallwaysToAvoid.add(hallway);
                                            ExtendedShape neighbor = findFirstShapeInHallwayFromLeft(entry.getKey(), directionToHallway, shape, hallway, hallwaysToAvoid);
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
                        hallwaysToAvoid.add(hallway);
                        ExtendedShape neighbor = findFirstShapeInHallwayFromLeft(currentShape, currentShapeDirectionToHallway, shape, hallway, hallwaysToAvoid);
                        if (neighbor != null) {
                            return neighbor;
                        }
                    } else {
                        return currentShape;
                    }
                }
            }
        }

        return null;
    }

    // hallway - holul in care tre sa caut vecinul
    // dth - directia holului in care tre sa caut vecinul fata de holul initial
    // shape - forma la care tre sa gasesc vecinul
    // hallwayToAvoid - holul initial
    // otw - al catalea e holul in care caut fata de holul initial
    private ExtendedShape findFirstShapeInHallwayFromRight(ExtendedShape hallway, String dth, ExtendedShape shape, ExtendedShape hallwayToAvoid, List<ExtendedShape> hallwaysToAvoid) {
        System.out.println(((ExtendedRectangle)hallway).getName() + " " + ((ExtendedRectangle)shape).getName());
        int otw = 0;
        int otwMin = 0;
        int otwMax = 0;
        for (Pair<ExtendedShape, String> pair : graph.get(hallway)) {
            if (pair.getKey() == hallwayToAvoid) {
                //relationToHallway2 = pair.getValue();
                //directionToHallway2 = relationToHallway2.split(" ")[0];
                otwMin = Integer.valueOf(pair.getValue().split(" ")[1]);
            } else if (pair.getKey() == shape) {
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

                if (entry.getKey() != hallway && entry.getKey() != shape && ok == true && !(hallwaysToAvoid.contains(entry.getKey()))) {
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
                                            hallwaysToAvoid.add(hallway);
                                            ExtendedShape neighbor = findFirstShapeInHallwayFromRight(entry.getKey(), directionToHallway, shape, hallway, hallwaysToAvoid);
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
                                            hallwaysToAvoid.add(hallway);
                                            ExtendedShape neighbor = findFirstShapeInHallwayFromRight(entry.getKey(), directionToHallway, shape, hallway, hallwaysToAvoid);
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
                                            hallwaysToAvoid.add(hallway);
                                            ExtendedShape neighbor = findFirstShapeInHallwayFromRight(entry.getKey(), directionToHallway, shape, hallway, hallwaysToAvoid);
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
                        hallwaysToAvoid.add(hallway);
                        ExtendedShape neighbor = findFirstShapeInHallwayFromRight(currentShape, currentShapeDirectionToHallway, shape, hallway, hallwaysToAvoid);
                        if (neighbor != null) {
                            return neighbor;
                        }
                    } else {
                        return currentShape;
                    }
                }
            }
        }

        return null;
    }

    private ExtendedShape findFirstShapeInHallwayFromStraight(ExtendedShape hallway, String searchDirection, ExtendedShape sh, ExtendedShape hallwayToAvoid) {
        //  System.out.println(((ExtendedRectangle)hallway).getName() + " " + searchDirection);
        List<Pair<ExtendedShape, String>> list = graph.get(hallway);
        if (list != null) {
            for (Pair<ExtendedShape, String> pair : list) {
                String relationToHallway = pair.getValue();
                String directionToHallway = relationToHallway.split(" ")[0];
                int orderToHallway = Integer.valueOf(relationToHallway.split(" ")[1]);
                if (directionToHallway.equals(searchDirection) && orderToHallway == 1) {
                    if (pair.getKey() instanceof Hallway) {
                        ExtendedShape neighbor = findFirstShapeInHallwayFromStraight(pair.getKey(), searchDirection, sh, hallway);
                        if (neighbor != null) {
                            return neighbor;
                        }
                    } else {
                        return pair.getKey();
                    }
                }
            }
        }
        return null;
    }
}