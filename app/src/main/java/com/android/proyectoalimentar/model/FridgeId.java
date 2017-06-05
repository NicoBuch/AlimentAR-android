package com.android.proyectoalimentar.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by leonelbadi on 4/6/17.
 */

public class FridgeId {

    @SerializedName("fridge_id")
    Integer fridgeId;

    public FridgeId(int fridgeId){
        this.fridgeId = fridgeId;
    }

    public Integer getFridgeId() {
        return fridgeId;
    }

    public void setFridgeId(Integer fridgeId) {
        this.fridgeId = fridgeId;
    }
}
