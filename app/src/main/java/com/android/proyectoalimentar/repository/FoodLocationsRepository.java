package com.android.proyectoalimentar.repository;

import com.android.proyectoalimentar.model.FoodLocation;
import com.android.proyectoalimentar.network.DonationsService;
import com.android.proyectoalimentar.network.RetrofitServices;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class FoodLocationsRepository {

    private final DonationsService donationsService;

    @Inject
    public FoodLocationsRepository() {
        donationsService = RetrofitServices.getService(DonationsService.class);
    }

    public void getFoodGivers(double lat, double lng, double radius,
                              RepoCallback<List<FoodLocation>> repoCallback) {
        donationsService.fetchDonators(lat, lng, radius)
                .enqueue(createSimpleCallback(repoCallback));
    }

    public void getFoodReceivers(double lat, double lng, double radius,
                                 RepoCallback<List<FoodLocation>> repoCallback) {
        donationsService.fetchCenters(lat, lng, radius)
                .enqueue(createSimpleCallback(repoCallback));
    }

    private Callback<List<FoodLocation>> createSimpleCallback(
            final RepoCallback<List<FoodLocation>> repoCallback) {
        return new Callback<List<FoodLocation>>() {
            @Override
            public void onResponse(Call<List<FoodLocation>> call,
                                   Response<List<FoodLocation>> response) {
                if (response.isSuccessful()) {
                    repoCallback.onSuccess(response.body());
                } else {
                    repoCallback.onError(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<FoodLocation>> call, Throwable t) {
                repoCallback.onError(t.getLocalizedMessage());
            }
        };
    }

}
