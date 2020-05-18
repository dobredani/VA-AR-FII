/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sergiu
 */
public class Office extends ExtendedRectangle {
    
    private List<String> professors = new ArrayList<>();
    
    public Office(Point p) {
        super(p);
        this.type = "office";
        this.shapeType = "Office";
    }

    public List<String> getProfessors() {
        return professors;
    }

    public void setProfessors(List<String> professors) {
        this.professors = professors;
    }
    
}