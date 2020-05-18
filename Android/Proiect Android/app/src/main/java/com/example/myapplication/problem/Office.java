package com.example.myapplication.problem;

import java.util.List;

public class Office extends Location {
    List<String> professors;


    public Office(int id, String name, List<String> professors){
        super(id,name);
        this.professors = professors;
    }


    public List<String> getProfessors() {return this.professors; }


}
