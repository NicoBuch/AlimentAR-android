package com.android.proyectoalimentar.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by leonelbadi on 11/2/18.
 */

public class Fridge {

    @SerializedName("fridge_id")
    Integer fridgeId;
    double lat;
    double lon;
    String address;
    String email;

    public Fridge(int fridgeId, double lat, double lon, String address, String email ){
        this.lat = lat;
        this.lon = lon;
        this.address = address;
        this.email = email;
        this.fridgeId = fridgeId;
    }

    public Integer getFridgeId() {
        return fridgeId;
    }

    public double getLat() {
        return lat;
    }

    public String getAddress() {
        return address;
    }

    public double getLon() {
        return lon;
    }

    public String getEmail() {
        return email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setFridgeId(Integer fridgeId) {
        this.fridgeId = fridgeId;
    }
}
