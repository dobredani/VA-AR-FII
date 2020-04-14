/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alex
 */
public class Floor {
    private List<ExtendedShape> shapes = new ArrayList<>();
    private Graph graph = new Graph();

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
    
    public String toJson()
    {
        return graph.toJson();
    }
}
