package com.android.proyectoalimentar.model;


import com.android.proyectoalimentar.Configuration;

public class Qualification {

    private int qualification;

    public Qualification(int qualification){
        if(!isValid(qualification)){
            throw new IllegalArgumentException("Qualification invalid");
        };
        this.qualification = qualification;
    }

    private boolean isValid(int qualification){
        if(qualification <= Configuration.MAX_QUALIFICATION && qualification >= Configuration.MIN_QUALIFICATION){
            return true;
        }
        return false;
    }
}
