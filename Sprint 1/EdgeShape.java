package com.amihaeseisergiu.laborator6;

import java.awt.geom.Line2D;

public class EdgeShape extends Line2D.Double {
    
    public EdgeShape(double x0, double y0, double x1, double y1)
    {
        super(x0, y0, x1, y1);
    }
    
    @Override
    public String toString()
    {
        return "Edge";
    }
}
