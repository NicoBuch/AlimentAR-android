package com.android.proyectoalimentar.repository;

import com.android.proyectoalimentar.model.Donation;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MarkersRepository {

    private Map<Marker, Donation> markerFoodLocations;
    private Map<Donation, Marker> foodLocationMarkers;

    @Inject
    public MarkersRepository() {
        markerFoodLocations = new HashMap<>();
        foodLocationMarkers = new HashMap<>();
    }

    public void addMarker(Marker marker, Donation foodLocation) {
        markerFoodLocations.put(marker, foodLocation);
        foodLocationMarkers.put(foodLocation, marker);
    }

    public Donation getFoodLocation(Marker marker) {
        return markerFoodLocations.get(marker);
    }

    public Marker getMarker(Donation foodLocation) {
        return foodLocationMarkers.get(foodLocation);
    }

    public void clear() {
        markerFoodLocations.clear();
        foodLocationMarkers.clear();
    }

}
