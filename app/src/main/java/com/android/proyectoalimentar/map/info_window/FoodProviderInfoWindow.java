package com.android.proyectoalimentar.map.info_window;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.proyectoalimentar.R;
import com.android.proyectoalimentar.model.FoodLocation;
import com.android.proyectoalimentar.repository.MarkersRepository;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import javax.inject.Inject;

public class FoodProviderInfoWindow implements GoogleMap.InfoWindowAdapter {

    private MarkersRepository markersRepository;
    private LayoutInflater layoutInflater;

    @Inject
    public FoodProviderInfoWindow(MarkersRepository markersRepository,
                                  LayoutInflater layoutInflater) {
        this.markersRepository = markersRepository;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        FoodLocation foodLocation = markersRepository.getFoodLocation(marker);
        if (foodLocation == null) {
            return null;
        }
        View markerView = layoutInflater.inflate(R.layout.food_provider_window, null);
        populateWithFoodLocationInfo(markerView, foodLocation);
        return markerView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return getInfoWindow(marker);
    }

    private void populateWithFoodLocationInfo(View markerView, FoodLocation foodLocation) {
        TextView title = (TextView) markerView.findViewById(R.id.title);
        title.setText(foodLocation.getName());

        TextView address = (TextView) markerView.findViewById(R.id.address);
        address.setText(foodLocation.getAddress());
    }

}
