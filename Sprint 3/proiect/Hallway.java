/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

/**
 *
 * @author Alex
 */
public class Hallway extends ExtendedRectangle {
    
    private static final String shapeType = "connector";
    
    public Hallway(Point p) {
        super(p);
        this.type = "Hallway";
    }
    
}
