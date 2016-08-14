package com.android.proyectoalimentar.model;

import com.google.android.gms.maps.model.LatLng;

public class FoodLocation {

    private String name;
    private String description;
    private LatLng location;
    private String address;
    private boolean isProvider;

    public FoodLocation(
            String name,
            String description,
            LatLng location,
            String address,
            boolean isProvider) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.address = address;
        this.isProvider = isProvider;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LatLng getLocation() {
        return location;
    }

    public String getAddress() {
        return address;
    }

    public boolean isProvider() {
        return isProvider;
    }

}
