/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

import java.awt.Rectangle;

/**
 *
 * @author Alex
 */
public class ExtendedRectangle extends ExtendedShape {

    private double length = 100;
    private double width = 100;
    private String name;
    private Rectangle rectangle = new Rectangle();
    protected String type;
    protected String shapeType;
    private int id;
    Hallway hallway = null;

    public ExtendedRectangle(Point p) {
        super(p);
    }

    public Point getCoordinates() {
        //  Bounds bounds = rectangle.getLayoutBounds();
        //  Point2D coordinates = rectangle.localToScene(bounds.getMinX(), bounds.getMinY());
        // int X = (int) coordinates.getX();
        // int Y = (int) coordinates.getY();
        Point p = new Point(250, 250);
        return p;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
        startPoint.setX(centerPoint.getX() + width / 2);
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
        startPoint.setY(centerPoint.getY() + length / 2);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ID :" + id;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the shapeType
     */
    public String getShapeType() {
        return shapeType;
    }

    /**
     * @param shapeType the shapeType to set
     */
    public void setShapeType(String shapeType) {
        this.shapeType = shapeType;
    }

    public Hallway getHallway() {
        return hallway;
    }

    public void setHallway(Hallway hallway) {
        this.hallway = hallway;
    }

}
