/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.util.Pair;

/**
 *
 * @author Alex
 */
public class Graph {

    Map<ExtendedShape, List< Pair<ExtendedShape, String>>> graph;

    public Graph() {
        graph = new HashMap<>();
    }

    public Map<ExtendedShape, List< Pair<ExtendedShape, String>>> getGraph() {
        return graph;
    }

    public void setGraph(Map<ExtendedShape, List< Pair<ExtendedShape, String>>> graph) {
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

    public void deleteShape(ExtendedShape shape) {
        ExtendedShape s = null;
        for (Map.Entry<ExtendedShape, List< Pair<ExtendedShape, String>>> entry : graph.entrySet()) {
            if (entry.getKey() == shape) {
                s = entry.getKey();
                break;
            }
        }
        graph.remove(s);

        for (Map.Entry<ExtendedShape, List< Pair<ExtendedShape, String>>> entry : graph.entrySet()) {
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

    @Override
    public String toString() {
        String output = "Graph: \n";
        for (Map.Entry<ExtendedShape, List< Pair<ExtendedShape, String>>> entry : graph.entrySet()) {
            output += ((ExtendedRectangle) entry.getKey()).getName() + "\n";
            for (Pair<ExtendedShape, String> p : entry.getValue()) {
                output += ((ExtendedRectangle) p.getKey()).getName() + ":" + p.getValue() + "\n";
            }
        }
        return output;
    }
    
    public String toJson()
    {
        String json = "[\n";
        boolean first = true;
        for (Map.Entry<ExtendedShape, List< Pair<ExtendedShape, String>>> entry : graph.entrySet()) {
            if(first)
            {
                json += "{\n";
                first = false;
            }
            else
            {
                json += ",\n{\n";
            }
            json += "\"name\": \"" + ((ExtendedRectangle) entry.getKey()).getName() + "\",\n";
            json += "\"type\": \"Rectangle\",\n"; //to be replaced when we add the types
            json += "\"neighbors\": [\n";
            first = true;
            for (Pair<ExtendedShape, String> p : entry.getValue()) {
                if(first)
                {
                    json += "{\n";
                    first = false;
                }
                else
                {
                    json += ",\n{\n";
                }
                json += "\"name\": \"" + ((ExtendedRectangle) p.getKey()).getName() + "\",\n";
                json += "\"direction\": \"" + p.getValue() + "\"\n";
                json += "}\n";
            }
            json += "]\n";
            json += "}";
        }
        json += "\n]\n";
        return json;
    }
}
