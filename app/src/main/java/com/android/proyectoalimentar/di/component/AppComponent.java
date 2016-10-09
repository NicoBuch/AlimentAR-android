package com.android.proyectoalimentar.di.component;

import com.android.proyectoalimentar.di.module.AppModule;
import com.android.proyectoalimentar.di.module.NetworkModule;
import com.android.proyectoalimentar.ui.confirm_donation.ConfirmDonationView;
import com.android.proyectoalimentar.ui.donations.DonationsFragment;
import com.android.proyectoalimentar.ui.login.LoginActivity;
import com.android.proyectoalimentar.ui.map.MapFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { AppModule.class, NetworkModule.class })
public interface AppComponent {

    void inject(LoginActivity activity);

    void inject(MapFragment activity);

    void inject(DonationsFragment activity);

    void inject(ConfirmDonationView activity);

}

