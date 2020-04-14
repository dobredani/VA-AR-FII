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
public class Classroom extends ExtendedRectangle {
    
    private String[] schedule;
    private final static String shapeType = "closed";
    
    public Classroom(Point p) {
        super(p);
        this.type = "Classroom";
    }

    public String[] getSchedule() {
        return schedule;
    }

    public void setSchedule(String[] schedule) {
        this.schedule = schedule;
    }
    
    public String foo()
    {
        return "foo";
    }
}
