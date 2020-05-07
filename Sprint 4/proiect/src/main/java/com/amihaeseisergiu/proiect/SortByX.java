/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

import java.io.Serializable;
import java.util.Comparator;

/**
 *
 * @author Alex
 */
public class SortByX implements Comparator<ExtendedShape>, Serializable {
    
    static final long serialVersionUID = 1L;

    @Override
    public int compare(ExtendedShape o1, ExtendedShape o2) {
        if (o1.getCenterPoint().getX() > o2.getCenterPoint().getX()) {
            return 1;
        } else {
            return -1;
        }
    }

}
