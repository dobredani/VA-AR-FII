/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Alex
 */
public class Classroom extends ExtendedRectangle {
    
    Map<Integer, List<InputSchedule>> mapaInputuri = new TreeMap();

    public Classroom(Point p) {
        super(p);
        this.type = "classRoom";
        this.shapeType = "Classroom";
    }


    public String foo() {
        return "foo";
    }
}
