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

    public Graph() {
        graph = new HashMap<>();
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
    }

    @Override
    public String toString() {
        String output = "Graph: \n";
        for (Map.Entry<ExtendedShape, List<Pair<ExtendedShape, String>>> entry : graph.entrySet()) {
            output += ((ExtendedRectangle) entry.getKey()).getId() + "\n";
            for (Pair<ExtendedShape, String> p : entry.getValue()) {
                output += ((ExtendedRectangle) p.getKey()).getId() + ":" + p.getValue() + "\n";
            }
        }
        return output;
    }
}