package com.android.proyectoalimentar.di.module;

import android.content.Context;
import android.location.LocationManager;
import android.view.LayoutInflater;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    LocationManager provideLocationManager() {
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Provides
    LayoutInflater provideLayoutInflater() {
        return LayoutInflater.from(context);
    }

}
