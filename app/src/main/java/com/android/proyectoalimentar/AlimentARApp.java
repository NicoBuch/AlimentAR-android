package com.android.proyectoalimentar;

import android.app.Application;

import com.android.proyectoalimentar.di.component.DaggerAppComponent;
import com.android.proyectoalimentar.di.module.AppModule;
import com.android.proyectoalimentar.map.MainActivity;

public class AlimentARApp extends Application {

    public static void inject(MainActivity target) {
        DaggerAppComponent.builder()
                .appModule(new AppModule(target))
                .build()
                .inject(target);
    }

}
