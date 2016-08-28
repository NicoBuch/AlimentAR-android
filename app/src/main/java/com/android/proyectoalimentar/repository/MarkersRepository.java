package com.android.proyectoalimentar.repository;

import com.android.proyectoalimentar.model.FoodLocation;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MarkersRepository {

    private Map<Marker, FoodLocation> markerFoodLocations;
    private Map<FoodLocation, Marker> foodLocationMarkers;

    @Inject
    public MarkersRepository() {
        markerFoodLocations = new HashMap<>();
        foodLocationMarkers = new HashMap<>();
    }

    public void addMarker(Marker marker, FoodLocation foodLocation) {
        markerFoodLocations.put(marker, foodLocation);
        foodLocationMarkers.put(foodLocation, marker);
    }

    public FoodLocation getFoodLocation(Marker marker) {
        return markerFoodLocations.get(marker);
    }

    public Marker getMarker(FoodLocation foodLocation) {
        return foodLocationMarkers.get(foodLocation);
    }

}
