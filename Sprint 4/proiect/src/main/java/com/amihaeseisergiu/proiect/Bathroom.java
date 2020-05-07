/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

import java.io.Serializable;

/**
 *
 * @author Alex
 */
public class Bathroom extends ExtendedRectangle implements Serializable {
    
    private final static String shapeType = "closed";
    
    public Bathroom(Point p) {
        super(p);
        this.type = "Bathroom";
    }
    
}
