package com.amihaeseisergiu.laborator6;

import java.awt.geom.Rectangle2D;

public class Square extends Rectangle2D.Double {
    
    public Square(int x0, int y0, int size)
    {
        super(x0 - size / 2, y0 - size / 2, size, size);
    }
    
    @Override
    public String toString()
    {
        return "Square";
    }
}
