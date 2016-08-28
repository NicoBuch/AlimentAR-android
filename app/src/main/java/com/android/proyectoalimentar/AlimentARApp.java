package com.android.proyectoalimentar;

import android.app.Application;
import android.content.Context;

import com.android.proyectoalimentar.di.component.DaggerAppComponent;
import com.android.proyectoalimentar.di.module.AppModule;
import com.android.proyectoalimentar.map.MainActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class AlimentarApp extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        setupCalligraphy();
    }

    public static Context getContext() {
        return context;
    }

    public static void inject(MainActivity target) {
        DaggerAppComponent.builder()
                .appModule(new AppModule(target))
                .build()
                .inject(target);
    }

    private void setupCalligraphy() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Medium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

}
