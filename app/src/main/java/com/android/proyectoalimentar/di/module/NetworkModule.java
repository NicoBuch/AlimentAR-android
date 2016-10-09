package com.android.proyectoalimentar.di.module;

import com.android.proyectoalimentar.network.DonationsService;
import com.android.proyectoalimentar.network.RetrofitServices;

import dagger.Module;
import dagger.Provides;

@Module
public class NetworkModule {

    @Provides
    DonationsService provideDonationsService() {
        return RetrofitServices.getService(DonationsService.class);
    }

}
