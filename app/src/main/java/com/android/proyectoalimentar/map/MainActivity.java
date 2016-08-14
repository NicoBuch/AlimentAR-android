package com.android.proyectoalimentar.map;

import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.Pair;
import android.widget.Toast;

import com.android.proyectoalimentar.AlimentARApp;
import com.android.proyectoalimentar.R;
import com.android.proyectoalimentar.location_detail.FoodLocationDetailView;
import com.android.proyectoalimentar.map.info_window.FoodProviderInfoWindow;
import com.android.proyectoalimentar.model.FoodLocation;
import com.android.proyectoalimentar.repository.FoodLocationsRepository;
import com.android.proyectoalimentar.repository.MarkersRepository;
import com.android.proyectoalimentar.repository.RepoCallback;
import com.annimon.stream.Stream;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private static LatLng DEFAULT_POSITION = new LatLng(-34.614550, -58.443239);

    private GoogleMap map;
    private FoodLocationDetailView foodLocationDetailView;

    @Inject FoodLocationsRepository foodLocationsRepository;
    @Inject MarkersRepository markersRepository;
    @Inject LocationManager locationManager;
    @Inject FoodProviderInfoWindow foodProviderInfoWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        foodLocationDetailView = (FoodLocationDetailView) findViewById(R.id.food_location_detail);

        AlimentARApp.inject(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setInfoWindowAdapter(foodProviderInfoWindow);

        foodLocationsRepository.getFoodLocations(new RepoCallback<List<FoodLocation>>() {
            @Override
            public void onSuccess(List<FoodLocation> foodProviders) {
                Stream.of(foodProviders)
                        .map(foodLocation -> new Pair<>(addMarker(foodLocation), foodLocation))
                        .forEach(pair -> markersRepository.addMarker(pair.first, pair.second));
            }

            @Override
            public void onError(String error) {
                Toast.makeText(MainActivity.this, "Failed to load food providers",
                        Toast.LENGTH_SHORT).show();
            }
        });

        map.setOnInfoWindowClickListener(this::openFoodLocationDetails);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_POSITION, 12f));
    }

    private Marker addMarker(FoodLocation foodProvider) {
        return map.addMarker(
                new MarkerOptions()
                        .position(foodProvider.getLocation())
                        .title(foodProvider.getName())
                        .snippet(foodProvider.getDescription())
                        .icon(BitmapDescriptorFactory.defaultMarker(
                                foodProvider.isProvider()
                                        ? BitmapDescriptorFactory.HUE_RED
                                        : BitmapDescriptorFactory.HUE_CYAN)));
    }

    private void openFoodLocationDetails(Marker marker) {
        FoodLocation foodLocation = markersRepository.getFoodLocation(marker);
        foodLocationDetailView.setFoodLocation(foodLocation);
        foodLocationDetailView.show();
    }

}
