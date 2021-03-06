package com.android.proyectoalimentar.ui.drawer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.proyectoalimentar.AlimentarApp;
import com.android.proyectoalimentar.Configuration;
import com.android.proyectoalimentar.R;
import com.android.proyectoalimentar.model.NotificationType;
import com.android.proyectoalimentar.model.User;
import com.android.proyectoalimentar.repository.RepoCallback;
import com.android.proyectoalimentar.repository.UserRepository;
import com.android.proyectoalimentar.services.LocationUpdatesService;
import com.android.proyectoalimentar.services.RegistrationIntentService;
import com.android.proyectoalimentar.ui.view.QualifyVolunteerView;
import com.android.proyectoalimentar.utils.LocationUtils;
import com.android.proyectoalimentar.utils.StorageUtils;
import com.annimon.stream.Stream;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DrawerActivity extends AppCompatActivity{

    private static final DrawerItem DEFAULT_ITEM = DrawerItem.MAP;
    private static final String TAG = DrawerActivity.class.getSimpleName();


    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.profile_image)
    SimpleDraweeView profileImage;
    @BindView(R.id.qualify_volunteer_view)
    QualifyVolunteerView qualifyVolunteerView;

    @Inject
    UserRepository userRepository;

    private Map<DrawerItem, DrawerItemContainer> drawerItems;
    private DrawerItem selectedItem;

    // A reference to the service used to get location updates.
    private LocationUpdatesService locationService = null;

    // Tracks the bound state of the service.
    private boolean mBound = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity);
        AlimentarApp.inject(this);
        ButterKnife.bind(this);
        //Register GCM Token
        registerToken();
        //Fetch user information
        fetchMyInformation();

        drawerItems = new HashMap<>();
        Stream.of(DrawerItem.values())
                .forEach(item ->
                        drawerItems.put(item, new DrawerItemContainer(DrawerActivity.this, item)));
        openDrawerItem(DEFAULT_ITEM, savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to the service.
        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
        reactToIntent();
    }



    public void openDrawerItem(DrawerItem drawerItem, Bundle bundle) {
        selectedItem = drawerItem;
        hideDrawer();
        Fragment fragmentToOpen = drawerItems.get(drawerItem).getFragment();
        if(fragmentToOpen.isAdded()){
            return;
        }
        fragmentToOpen.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragmentToOpen)
                .commit();
    }

    public void toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            hideDrawer();
        } else {
            showDrawer();
        }
    }

    private void showDrawer() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    private void hideDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        if (selectedItem == DrawerItem.MAP) {
            super.onBackPressed();
        } else {
            openDrawerItem(DrawerItem.MAP, new Bundle());
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void fetchMyInformation() {
        userRepository.getMyInformation(new RepoCallback<User>() {
            @Override
            public void onSuccess(User user) {
                refreshUserInformation(user);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void refreshUserInformation(User user) {
        if (user.getName() != null && !user.getName().isEmpty()) {
            name.setText(user.getName());
        }
        if (user.getAvatar() != null && user.getAvatar().getThumb() != null) {
            profileImage.setImageURI(user.getAvatar().getThumb());
        }
    }

    private void registerToken() {
        if (!StorageUtils.getBooleanFromSharedPreferences(Configuration.SENT_TOKEN_TO_SERVER, false)) {
            //Only register token if is not registered yet.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    // Monitors the state of the connection to the service.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
            locationService = binder.getService();
            registerToLocationService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            locationService = null;
            mBound = false;
        }
    };


    private void registerToLocationService(){
        // If is not requesting location update, then must do.
        if (!LocationUtils.requestingLocationUpdates(this)) {
            if (!LocationUtils.checkPermissions(this)) {
                LocationUtils.requestFineLocationPermissions(this);
            }
        }

    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LocationUtils.REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
                // We have to verify that the service is already binded.
                if(locationService != null)
                {
                    locationService.requestLocationUpdates();
                }
            } else {
                // Permission denied.
            }
        }
    }


    /**
     * React to a intent
     */
    private void reactToIntent(){
        Intent intent = getIntent();
        NotificationType notificationType;
        if(intent == null){
            return;
        }
        notificationType = getNotificationType(intent);
        if( notificationType == null){
            return;
        }
        switch (notificationType){
            case QUALIFICATION_REQUEST:
                reactToQualificationRequest(intent);
                break;
            default:
        }

    }

    private void reactToQualificationRequest(Intent intent){
        String donationId = intent.getStringExtra(Configuration.DONATION);
        String donatorName = intent.getStringExtra(Configuration.DONATOR_NAME);

        //This shouldn't happen except there was an error on the notification system.
        if(donationId ==null || donationId.isEmpty()){
            return;
        }

        //Here we show the qualify view.
        qualifyVolunteerView.setInformation(donationId,donatorName);
        qualifyVolunteerView.setVisibility(View.VISIBLE);
    }

    private NotificationType getNotificationType(Intent intent){
        return (NotificationType) intent.getSerializableExtra(Configuration.NOTIFICATION_TYPE);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
    }


}
