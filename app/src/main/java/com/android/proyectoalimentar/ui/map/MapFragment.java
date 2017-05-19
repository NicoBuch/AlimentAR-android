package com.android.proyectoalimentar.ui.map;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.proyectoalimentar.AlimentarApp;
import com.android.proyectoalimentar.R;
import com.android.proyectoalimentar.model.Donation;
import com.android.proyectoalimentar.model.FoodLocation;
import com.android.proyectoalimentar.repository.FoodLocationsRepository;
import com.android.proyectoalimentar.repository.MarkersRepository;
import com.android.proyectoalimentar.repository.RepoCallback;
import com.android.proyectoalimentar.services.LocationUpdatesService;
import com.android.proyectoalimentar.ui.confirm_donation.ConfirmDonationView;
import com.android.proyectoalimentar.ui.drawer.DrawerActivity;
import com.android.proyectoalimentar.ui.drawer.DrawerItem;
import com.android.proyectoalimentar.utils.LocationUtils;
import com.android.proyectoalimentar.utils.MapCalculator;
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

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final int TAB_GIVERS = 0;
    private static final int TAB_RECEIVERS = 1;
    private static LatLng DEFAULT_POSITION = new LatLng(-34.614550, -58.443239);

    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.location_details)
    ViewPager locationDetails;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.confirm_donation)
    ConfirmDonationView confirmDonationView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private GoogleMap map;
    private LocationAdapter locationAdapter;

    private Marker selectedMarker;

    @Inject
    FoodLocationsRepository foodLocationsRepository;
    @Inject
    MarkersRepository markersRepository;


    // The BroadcastReceiver used to listen from broadcasts from the service.
    private LocationReceiver locationReceiver;
    private Location lastLocation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        ButterKnife.bind(this, view);

        setupDrawer();
        setupTabs();
        setupLocationDetailsViewPager();
        setupConfirmDonationView();
        setupSwipeRefresh();

        AlimentarApp.inject(this);

        locationReceiver = new LocationReceiver();


        return view;
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            switch (tabLayout.getSelectedTabPosition()) {
                case TAB_GIVERS:
                    populateFoodGivers(false);
                    break;
                case TAB_RECEIVERS:
                    populateFoodReceivers(false);
                    break;
            }
        });

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fragmentManager = getChildFragmentManager();
        SupportMapFragment fragment = (SupportMapFragment)
                fragmentManager.findFragmentById(R.id.map_container);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fragment.getMapAsync(this);
            fragmentManager.beginTransaction().replace(R.id.map_container, fragment).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this.getContext()).registerReceiver(locationReceiver,
                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(this.getContext()).unregisterReceiver(locationReceiver);
        super.onPause();
    }

    private void setupDrawer() {
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(v -> ((DrawerActivity) getActivity()).toggleDrawer());
    }

    private void setupTabs() {
        tabLayout.addOnTabSelectedListener(new OnTabSelectedListenerWrapper() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                markersRepository.clear();
                populateMap();
            }
        });
    }

    private void setupConfirmDonationView() {
        confirmDonationView.setDonationConfirmationListener(
                new ConfirmDonationView.DonationConfirmationListener() {
                    @Override
                    public void onDonationConfirmed() {
                        ((DrawerActivity) getActivity()).openDrawerItem(DrawerItem.DONATIONS);
                        confirmDonationView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onDonationCanceled() {
                        confirmDonationView.setVisibility(View.GONE);
                    }
                });
    }

    private void populateMap() {
        switch (tabLayout.getSelectedTabPosition()) {
            case TAB_GIVERS:
                populateFoodGivers(true);
                break;
            case TAB_RECEIVERS:
                populateFoodReceivers(true);
                break;
        }
    }

    private void populateFoodGivers(boolean useCache) {
        if (clearMap()) {
            LatLng targetPosition = map.getCameraPosition().target;
            foodLocationsRepository.getFoodGivers(
                    targetPosition.latitude,
                    targetPosition.longitude,
                    MapCalculator.getVisibleRadius(map), useCache,
                    createFoodRepoCallback());
            locationAdapter.setDonationButtonAvailable(true);
        }
    }

    private void populateFoodReceivers(boolean useCache) {
        if (clearMap()) {
            LatLng targetPosition = map.getCameraPosition().target;
            foodLocationsRepository.getFoodReceivers(
                    targetPosition.latitude,
                    targetPosition.longitude,
                    MapCalculator.getVisibleRadius(map), useCache,
                    createFoodRepoCallback());
            locationAdapter.setDonationButtonAvailable(false);
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

    private RepoCallback<List<Donation>> createFoodRepoCallback() {
        return new RepoCallback<List<Donation>>() {
            @Override
            public void onSuccess(List<Donation> foodProviders) {
                swipeRefreshLayout.setRefreshing(false);
                if (getActivity() == null || getActivity().isFinishing()) {
                    return;
                }
                Stream.of(foodProviders)
                        .map(foodLocation -> new Pair<>(addMarker(foodLocation), foodLocation))
                        .forEach(pair -> markersRepository.addMarker(pair.first, pair.second));
                locationAdapter.setFoodLocations(foodProviders);
                locationDetails.setCurrentItem(0, false);
                selectFoodLocationAtPosition(0);
            }

            @Override
            public void onError(String error) {
                swipeRefreshLayout.setRefreshing(false);
                if (getActivity() == null || getActivity().isFinishing()) {
                    return;
                }
                Toast.makeText(getActivity(), "Problema de conexión",
                        Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void setupLocationDetailsViewPager() {
        locationAdapter = new LocationAdapter(getActivity(), foodLocation -> {
            confirmDonationView.setFoodLocation(foodLocation);
            confirmDonationView.setVisibility(View.VISIBLE);
        });
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
        Donation foodLocation = locationAdapter.getFoodLocationAt(position);
        Marker marker = markersRepository.getMarker(foodLocation);
        selectMarker(marker);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(marker -> {
            selectMarker(marker);

            Donation foodLocation = markersRepository.getFoodLocation(selectedMarker);
            int foodLocationPosition = locationAdapter.getLocationPosition(foodLocation);
            locationDetails.setCurrentItem(foodLocationPosition, true);

            map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
            return true;
        });

        populateFoodGivers(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_POSITION, 12f));
        setMyLocationButton();
        setRefreshMarkersListener();

    }

    /**
     * Method that set a listener to refresh all the donations/food receivers.
     */
    private void setRefreshMarkersListener(){
        map.setOnCameraIdleListener(() -> populateMap());
    }

    private void setMyLocationButton() {
        if (ActivityCompat.checkSelfPermission(MapFragment.this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MapFragment.this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            LocationUtils.requestFineLocationPermissions(MapFragment.this.getActivity());
            return;
        }
        map.setMyLocationEnabled(true);
        map.setOnMyLocationButtonClickListener(() -> {
            boolean havePermissions = LocationUtils.checkPermissions(MapFragment.this.getContext());
            if (!havePermissions) {
                // If the context doesn' have correct permission then request fine access
                // location permission.
                LocationUtils.requestFineLocationPermissions(MapFragment.this.getActivity());
                return true;

            } else {
                //If context have fine location permission, then should continue with the
                // map location move.
                return false;
            }
        });
    }

    private void selectMarker(Marker marker) {
        if (selectedMarker != null) {
            selectedMarker.setIcon(BitmapDescriptorFactory.fromResource(
                    R.drawable.marker_unselected));
        }
        selectedMarker = marker;
        if (selectedMarker != null) {
            selectedMarker.setIcon(BitmapDescriptorFactory.fromResource(
                    R.drawable.marker_selected));
        }
    }

    private Marker addMarker(Donation donation) {
        FoodLocation foodProvider = donation.getDonator();
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker_unselected);
        return map.addMarker(
                new MarkerOptions()
                        .position(foodProvider.getLocation())
                        .title(foodProvider.getName())
                        .snippet(foodProvider.getDescription())
                        .icon(icon));
    }

    /**
     * Receiver for broadcasts sent by {@link LocationUpdatesService}.
     */
    private class LocationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            lastLocation = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
            if (lastLocation != null) {
                map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(lastLocation.getLatitude()
                        , lastLocation.getLongitude())));
                //Update map
            }
        }
    }

}
