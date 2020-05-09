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
    Hallway hallway;

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
                }
            }
        }
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
                //  System.out.println(((ExtendedRectangle) r.getKey()).getId() + " " + r.getValue());
                //  System.out.println(((ExtendedRectangle) r.getKey()).getId() + " " + newPairs);
                graph.replace(r.getKey(), newPairs);
                //graph.replace((ExtendedShape) r, newPairs);
                //   System.out.println(r.getId());
                //  System.out.println("Down: " + downs);
                //  System.out.println("Up: " + ups);
                //  System.out.println("Right: " + rights);
                //  System.out.println("Left: " + lefts);
            }
        }
        setNeighbors();
    }

    public void setNeighbors() {
        for (Map.Entry<ExtendedShape, List<Pair<ExtendedShape, String>>> entry : graph.entrySet()) {
            if (entry.getKey() != hallway) {
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
                            // System.out.println(((ExtendedRectangle)entry.getKey()).getId() + " " + relationToHallway);
                        }
                    }
                    ExtendedShape leftNeighbor = findLeftNeighbor(entry.getKey(), directionToHallway, orderToHallway);
                    ExtendedShape rightNeighbor = findRightNeighbor(entry.getKey(), directionToHallway, orderToHallway);
                    ExtendedShape straightNeighbor = findStraightNeighbor(entry.getKey(), directionToHallway, orderToHallway);
                    //    if (leftNeighbor != null && rightNeighbor != null) {
                    //        System.out.println(((ExtendedRectangle) entry.getKey()).getId() + " " + ((ExtendedRectangle) leftNeighbor).getId() + " " + ((ExtendedRectangle) rightNeighbor).getId());
                    //    }
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
                        // System.out.println(((ExtendedRectangle) entry.getKey()).getId() + " " + ((ExtendedRectangle) leftNeighbor).getId() + " " + ((ExtendedRectangle) rightNeighbor).getId());
                        // System.out.println("");
                    } else {
                        List<Pair<ExtendedShape, String>> neighbors = new ArrayList<>();
                        if (leftNeighbor != null) {
                            Pair<ExtendedShape, String> p1 = new Pair<>(leftNeighbor, "Left");
                            neighbors.add(p1);
                            //  neighborGraph.put(entry.getKey(), neighbors);
                            // System.out.println(((ExtendedRectangle) entry.getKey()).getId() + " " + ((ExtendedRectangle) leftNeighbor).getId() + " " + ((ExtendedRectangle) rightNeighbor).getId());
                            // System.out.println("");
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
                        } else if (rightNeighbor == null && leftNeighbor == null && straightNeighbor == null) {
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
        for (Map.Entry<ExtendedShape, List<Pair<ExtendedShape, String>>> entry : neighborGraph.entrySet()) {
            output += ((ExtendedRectangle) entry.getKey()).getName() + "\n";
            if (entry.getValue() != null) {
                for (Pair<ExtendedShape, String> p : entry.getValue()) {
                    output += ((ExtendedRectangle) p.getKey()).getName() + ":" + p.getValue() + "\n";
                }
            }
        }
        return output;
    }

    private ExtendedShape findLeftNeighbor(ExtendedShape sh, String dth, int otw) {
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
                            if (dth.equals(directionToHallway) && orderToHallway == otw + 1) {
                                return entry.getKey();
                            }
                        } else {
                            if (dth.equals(directionToHallway) && orderToHallway == otw - 1) {
                                return entry.getKey();
                            }
                        }
                        // System.out.println(((ExtendedRectangle)entry.getKey()).getId() + " " + relationToHallway);
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
                            if (searchDirection.equals(directionToHallway) && orderToHallway == 1) {
                                return entry.getKey();
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
        if (searchDirection.equals("Down") || searchDirection.equals("Left")) {
            if (max != 0) {
                return currentShape;
            }
        }
        return null;
    }

    private ExtendedShape findRightNeighbor(ExtendedShape sh, String dth, int otw) {
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
                            if (dth.equals(directionToHallway) && orderToHallway == otw + 1) {
                                return entry.getKey();
                            }
                        } else {
                            if (dth.equals(directionToHallway) && orderToHallway == otw - 1) {
                                return entry.getKey();
                            }
                        }
                        // System.out.println(((ExtendedRectangle)entry.getKey()).getId() + " " + relationToHallway);
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
                            if (searchDirection.equals(directionToHallway) && orderToHallway == 1) {
                                return entry.getKey();
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
        if (searchDirection.equals("Up") || searchDirection.equals("Right")) {
            if (max != 0) {
                return currentShape;
            }
        }
        return null;
    }

    private ExtendedShape findStraightNeighbor(ExtendedShape sh, String dth, int otw) {
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
                            return entry.getKey();
                        }
                        // System.out.println(((ExtendedRectangle)entry.getKey()).getId() + " " + relationToHallway);
                    }
                }
            }
        }
        return null;
    }
}
