package com.android.proyectoalimentar.di.component;

import com.android.proyectoalimentar.di.module.AppModule;
import com.android.proyectoalimentar.login.LoginActivity;
import com.android.proyectoalimentar.map.MapActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(LoginActivity activity);

    void inject(MapActivity activity);

}

