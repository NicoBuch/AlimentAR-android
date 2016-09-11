package com.android.proyectoalimentar.map;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.android.proyectoalimentar.AlimentarApp;
import com.android.proyectoalimentar.R;
import com.android.proyectoalimentar.model.FoodLocation;
import com.android.proyectoalimentar.repository.FoodLocationsRepository;
import com.android.proyectoalimentar.repository.MarkersRepository;
import com.android.proyectoalimentar.repository.RepoCallback;
import com.android.proyectoalimentar.utils.OnPageChangeListenerWrapper;
import com.android.proyectoalimentar.utils.OnTabSelectedListenerWrapper;
import com.annimon.stream.Stream;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int TAB_GIVERS = 0;
    private static final int TAB_RECEIVERS = 1;
    private static LatLng DEFAULT_POSITION = new LatLng(-34.614550, -58.443239);

    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.location_details) ViewPager locationDetails;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private GoogleMap map;
    private LocationAdapter locationAdapter;

    private Marker selectedMarker;

    @Inject FoodLocationsRepository foodLocationsRepository;
    @Inject MarkersRepository markersRepository;
    @Inject LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        setupDrawer();
        setupTabs();
        setupLocationDetailsViewPager();

        AlimentarApp.inject(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setupDrawer() {
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(v -> toggleDrawer());
    }

    private void toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    private void setupTabs() {
        tabLayout.addOnTabSelectedListener(new OnTabSelectedListenerWrapper() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                populateMap();
            }
        });
    }

    private void populateMap() {
        switch (tabLayout.getSelectedTabPosition()) {
            case TAB_GIVERS:
                populateFoodGivers();
                break;
            case TAB_RECEIVERS:
                populateFoodReceivers();
                break;
        }
    }

    private void populateFoodGivers() {
        if (clearMap()) {
            foodLocationsRepository.getFoodGivers(createFoodRepoCallback());
        }
    }

    private void populateFoodReceivers() {
        if (clearMap()) {
            foodLocationsRepository.getFoodReceivers(createFoodRepoCallback());
        }
    }

    private boolean clearMap() {
        if (map == null) {
            return false;
        }
        map.clear();
        selectedMarker = null;
        return true;
    }

    private RepoCallback<List<FoodLocation>> createFoodRepoCallback() {
        return new RepoCallback<List<FoodLocation>>() {
            @Override
            public void onSuccess(List<FoodLocation> foodProviders) {
                Stream.of(foodProviders)
                        .map(foodLocation -> new Pair<>(addMarker(foodLocation), foodLocation))
                        .forEach(pair -> markersRepository.addMarker(pair.first, pair.second));
                locationAdapter.setFoodLocations(foodProviders);
                locationDetails.setCurrentItem(0, false);
                selectFoodLocationAtPosition(0);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(MapActivity.this, "Failed to load food providers",
                        Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void setupLocationDetailsViewPager() {
        locationAdapter = new LocationAdapter(this);
        locationDetails.setAdapter(locationAdapter);
        new HeightModifierOnPageChangedListener(locationDetails, locationAdapter);
        locationDetails.addOnPageChangeListener(new OnPageChangeListenerWrapper() {
            @Override
            public void onPageSelected(int position) {
                selectFoodLocationAtPosition(position);
            }
        });
    }

    private void selectFoodLocationAtPosition(int position) {
        FoodLocation foodLocation = locationAdapter.getFoodLocationAt(position);
        Marker marker = markersRepository.getMarker(foodLocation);
        selectMarker(marker);
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
        map.setOnMarkerClickListener(marker -> {
            selectMarker(marker);

            FoodLocation foodLocation = markersRepository.getFoodLocation(selectedMarker);
            int foodLocationPosition = locationAdapter.getLocationPosition(foodLocation);
            locationDetails.setCurrentItem(foodLocationPosition, true);

            map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
            return true;
        });

        populateFoodGivers();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_POSITION, 12f));
    }

    private void selectMarker(Marker marker) {
        if (selectedMarker != null) {
            selectedMarker.setIcon(BitmapDescriptorFactory.fromResource(
                    R.drawable.marker_unselected));
        }
        selectedMarker = marker;
        selectedMarker.setIcon(BitmapDescriptorFactory.fromResource(
                R.drawable.marker_selected));
    }

    private Marker addMarker(FoodLocation foodProvider) {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker_unselected);
        return map.addMarker(
                new MarkerOptions()
                        .position(foodProvider.getLocation())
                        .title(foodProvider.getName())
                        .snippet(foodProvider.getDescription())
                        .icon(icon));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
