/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

import java.io.Serializable;
import java.util.Comparator;

/**
 *
 * @author Alex
 */
public class ExtendedShape implements Serializable {
    
    static final long serialVersionUID = 1L;

    Point centerPoint;
    private String color;
    Point startPoint = new Point();

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    ExtendedShape(Point p) {
        centerPoint = p;
    }

    public Point getCenterPoint() {
        return centerPoint;
    }

    public void setCenterPoint(Point centerPoint) {
        this.centerPoint = centerPoint;
    }
}