/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amihaeseisergiu.proiect;

import java.util.Objects;

/**
 *
 * @author andre
 */
public class InputSchedule {
    String grupa;
    String oraStart;
    String oraFinal;
    String materie;
    public InputSchedule(String grupa,String oraStart,String oraFinal,String materie)
    {
        this.grupa=grupa;
        this.oraStart=oraStart;
        this.oraFinal=oraFinal;
        this.materie=materie;
    }

    public String getGrupa() {
        return grupa;
    }

    public void setGrupa(String grupa) {
        this.grupa = grupa;
    }

    public String getOraStart() {
        return oraStart;
    }

    public void setOraStart(String oraStart) {
        this.oraStart = oraStart;
    }

    public String getOraFinal() {
        return oraFinal;
    }

    public void setOraFinal(String oraFinal) {
        this.oraFinal = oraFinal;
    }

    public String getMaterie() {
        return materie;
    }

    public void setMaterie(String materie) {
        this.materie = materie;
    }

    @Override
    public String toString() {
        return "InputSchedule{" + "grupa=" + grupa + ", oraStart=" + oraStart + ", oraFinal=" + oraFinal + ", materie=" + materie + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
 
        final InputSchedule other = (InputSchedule) obj;
        return Objects.equals(this.grupa, other.grupa) && (Objects.equals(this.oraStart, other.oraStart)) && (Objects.equals(this.oraFinal, other.oraFinal)) && (Objects.equals(this.materie, other.materie));
    }
    
}
