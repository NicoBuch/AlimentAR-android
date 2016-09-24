package com.android.proyectoalimentar.di.component;

import com.android.proyectoalimentar.di.module.AppModule;
import com.android.proyectoalimentar.ui.login.LoginActivity;
import com.android.proyectoalimentar.ui.map.MapFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(LoginActivity activity);

    void inject(MapFragment activity);

}

