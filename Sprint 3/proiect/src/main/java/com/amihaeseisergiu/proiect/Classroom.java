/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alex
 */
public class Classroom extends ExtendedRectangle implements Serializable {
    
    private String[] schedule;
    private final static String shapeType = "closed";
    List<String> listaOraStartLuni = new ArrayList<>();
    int marimeListaLuni;
    List<String> listaOraSfarsitLuni = new ArrayList<>();
    List<String> listaMaterieLuni = new ArrayList<>();
    List<String> listaOraStartMarti = new ArrayList<>();
    int marimeListaMarti;
    List<String> listaOraSfarsitMarti = new ArrayList<>();
    List<String> listaMaterieMarti = new ArrayList<>();
    List<String> listaOraStartMiercuri = new ArrayList<>();
    int marimeListaMiercuri;
    List<String> listaOraSfarsitMiercuri = new ArrayList<>();
    List<String> listaMaterieMiercuri = new ArrayList<>();
    List<String> listaOraStartJoi = new ArrayList<>();
    int marimeListaJoi;
    List<String> listaOraSfarsitJoi = new ArrayList<>();
    List<String> listaMaterieJoi = new ArrayList<>();
    List<String> listaOraStartVineri = new ArrayList<>();
    int marimeListaVineri;
    List<String> listaOraSfarsitVineri = new ArrayList<>();
    List<String> listaMaterieVineri = new ArrayList<>();
     List<String> listaOraStartSambata = new ArrayList<>();
    int marimeListaSambata;
    List<String> listaOraSfarsitSambata = new ArrayList<>();
    List<String> listaMaterieSambata= new ArrayList<>();
      List<String> listaOraStartDuminica = new ArrayList<>();
    int marimeListaDuminica;
    List<String> listaOraSfarsitDuminica = new ArrayList<>();
    List<String> listaMaterieDuminica= new ArrayList<>();
   

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

    public String foo() {
        return "foo";
    }
}
