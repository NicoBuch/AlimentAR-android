package com.android.proyectoalimentar.di.component;

import com.android.proyectoalimentar.di.module.AppModule;
import com.android.proyectoalimentar.di.module.NetworkModule;
import com.android.proyectoalimentar.services.LocationUpdatesService;
import com.android.proyectoalimentar.services.RegistrationIntentService;
import com.android.proyectoalimentar.ui.confirm_donation.ConfirmDonationView;
import com.android.proyectoalimentar.ui.donations.DonationsFragment;
import com.android.proyectoalimentar.ui.drawer.DrawerActivity;
import com.android.proyectoalimentar.ui.login.LoginActivity;
import com.android.proyectoalimentar.ui.login.SignOutFragment;
import com.android.proyectoalimentar.ui.map.MapFragment;
import com.android.proyectoalimentar.ui.view.QualifyVolunteerView;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { AppModule.class, NetworkModule.class })
public interface AppComponent {

    void inject(LoginActivity activity);

    void inject(MapFragment activity);

    void inject(DonationsFragment activity);

    void inject(ConfirmDonationView activity);

    void inject(DrawerActivity drawerActivity);

    void inject(RegistrationIntentService service);

    void inject(LocationUpdatesService service);

    void inject(QualifyVolunteerView qualifyVolunteerView);

    void inject(SignOutFragment activity);

}

