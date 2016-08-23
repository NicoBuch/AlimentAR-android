package com.android.proyectoalimentar;

import android.app.Application;
import android.content.Context;

import com.android.proyectoalimentar.di.component.DaggerAppComponent;
import com.android.proyectoalimentar.di.module.AppModule;
import com.android.proyectoalimentar.map.MainActivity;

public class AlimentARApp extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
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

}
