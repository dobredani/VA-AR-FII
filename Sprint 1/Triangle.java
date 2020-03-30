package com.amihaeseisergiu.laborator6;

import java.awt.Polygon;

public class Triangle extends Polygon {
    
    public Triangle(int x0, int y0, int size)
    {
        this.addPoint(x0, y0-size);
        this.addPoint(x0-size, y0+size);
        this.addPoint(x0+size, y0+size);
    }
    
    @Override
    public String toString()
    {
        return "Triangle";
    }
}
