package com.android.proyectoalimentar.di.module;

import android.content.Context;

import com.android.proyectoalimentar.network.DonationsService;
import com.android.proyectoalimentar.network.LocationService;
import com.android.proyectoalimentar.network.LoginService;
import com.android.proyectoalimentar.network.NotificationService;
import com.android.proyectoalimentar.network.RetrofitServices;

import dagger.Module;
import dagger.Provides;

@Module
public class NetworkModule {


    @Provides
    DonationsService provideDonationsService() {
        return RetrofitServices.getService(DonationsService.class);
    }

    @Provides
    LoginService provideLoginService() {
        return RetrofitServices.getService(LoginService.class);
    }

    @Provides
    NotificationService provideNotificationService(){
        return RetrofitServices.getService(NotificationService.class);
    }

    @Provides
    LocationService provideLocationService(){
        return RetrofitServices.getService(LocationService.class);
    }


}
