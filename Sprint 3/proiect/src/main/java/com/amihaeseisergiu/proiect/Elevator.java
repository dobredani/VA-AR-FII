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
public class Elevator extends ExtendedRectangle {
    
    private final static String shapeType = "connector";
    
    public Elevator(Point p) {
        super(p);
        this.type = "Elevator";
    }
    
}
