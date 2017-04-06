package com.android.proyectoalimentar;

import android.app.Application;
import android.content.Context;

import com.android.proyectoalimentar.di.component.DaggerAppComponent;
import com.android.proyectoalimentar.di.module.AppModule;
import com.android.proyectoalimentar.di.module.NetworkModule;
import com.android.proyectoalimentar.services.RegistrationIntentService;
import com.android.proyectoalimentar.ui.donations.DonationsFragment;
import com.android.proyectoalimentar.ui.drawer.DrawerActivity;
import com.android.proyectoalimentar.ui.login.LoginActivity;
import com.android.proyectoalimentar.ui.map.MapFragment;
import com.facebook.FacebookSdk;

import net.danlew.android.joda.JodaTimeAndroid;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class AlimentarApp extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        FacebookSdk.sdkInitialize(getApplicationContext());
        JodaTimeAndroid.init(this);
        setupCalligraphy();
    }

    public static Context getContext() {
        return context;
    }

    public static void inject(MapFragment target) {
        DaggerAppComponent.builder()
                .appModule(new AppModule(target.getActivity()))
                .build()
                .inject(target);
    }

    public static void inject(LoginActivity target) {
        DaggerAppComponent.builder()
                .appModule(new AppModule(target))
                .build()
                .inject(target);
    }

    public static void inject(DrawerActivity target) {
        DaggerAppComponent.builder()
                .appModule(new AppModule(target))
                .build()
                .inject(target);
    }

    public static void inject(RegistrationIntentService service){
        DaggerAppComponent.builder()
                .appModule(new AppModule(service))
                .build()
                .inject(service);
    }

    private void setupCalligraphy() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Medium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

}
