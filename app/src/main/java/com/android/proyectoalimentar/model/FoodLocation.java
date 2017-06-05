package com.android.proyectoalimentar.model;

import com.google.android.gms.maps.model.LatLng;

public class FoodLocation {

    private int id;
    private String name;
    private String description;
    private double lat;
    private double lng;
    private String address;
    private Avatar avatar;

    private transient int donationId;

    public FoodLocation(String name, String description, String address) {
        this.name = name;
        this.description = description;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LatLng getLocation() {
        return new LatLng(lat, lng);
    }

    public String getAddress() {
        return address;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FoodLocation that = (FoodLocation) o;

        if (Double.compare(that.lat, lat) != 0) return false;
        if (Double.compare(that.lng, lng) != 0) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        return address != null ? address.equals(that.address) : that.address == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name != null ? name.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        temp = Double.doubleToLongBits(lat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lng);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }

    public static FoodLocation nullValue() {
        return new FoodLocation("Nombre del lugar", "Descripcion", "Direccion 800");
    }

}
