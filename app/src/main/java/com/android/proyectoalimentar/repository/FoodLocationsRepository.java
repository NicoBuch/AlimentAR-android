package com.android.proyectoalimentar.repository;

import com.android.proyectoalimentar.model.FoodLocation;
import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

public class FoodLocationsRepository {

    @Inject
    public FoodLocationsRepository() {
    }

    public void getFoodGivers(RepoCallback<List<FoodLocation>> repoCallback) {
        List<FoodLocation> foodLocations = new LinkedList<>();
        foodLocations.addAll(getFoodProviders());
        repoCallback.onSuccess(foodLocations);
    }

    public void getFoodReceivers(RepoCallback<List<FoodLocation>> repoCallback) {
        List<FoodLocation> foodLocations = new LinkedList<>();
        foodLocations.addAll(getFoodConsumers());
        repoCallback.onSuccess(foodLocations);
    }

    private List<FoodLocation> getFoodProviders() {
        List<FoodLocation> foodProviders = new LinkedList<>();
        foodProviders.add(new FoodLocation("Wolox", "Driving innovation", new LatLng(-34.580977, -58.424232), "Guemes 1000", true));
        foodProviders.add(new FoodLocation("ITBA", "Para estudiar", new LatLng(-34.603175, -58.367790), "Madero 500", true));
        return foodProviders;
    }

    public List<FoodLocation> getFoodConsumers() {
        List<FoodLocation> foodConsumers = new LinkedList<>();
        foodConsumers.add(new FoodLocation("Comedor 1", "Aqui va la descripcion", new LatLng(-34.582242, -58.427342), "Direccion", false));
        foodConsumers.add(new FoodLocation("Comedor 2", "Aqui va la descripcion", new LatLng(-34.586488, -58.416403), "Direccion", false));
        foodConsumers.add(new FoodLocation("Comedor 3", "Aqui va la descripcion", new LatLng(-34.601988, -58.375141), "Direccion", false));
        foodConsumers.add(new FoodLocation("Comedor 4", "Aqui va la descripcion", new LatLng(-34.606303, -58.376508), "Direccion", false));
        return foodConsumers;
    }

}
