package com.android.proyectoalimentar.di.component;

import com.android.proyectoalimentar.di.module.AppModule;
import com.android.proyectoalimentar.map.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(MainActivity activity);

}

