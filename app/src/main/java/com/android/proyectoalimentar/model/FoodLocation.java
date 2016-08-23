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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FoodLocation that = (FoodLocation) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (location != null ? !location.equals(that.location) : that.location != null)
            return false;
        return address != null ? address.equals(that.address) : that.address == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }

}
