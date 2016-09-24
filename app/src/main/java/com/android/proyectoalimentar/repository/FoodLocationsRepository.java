package com.android.proyectoalimentar.repository;

import com.android.proyectoalimentar.model.FoodLocation;
import com.android.proyectoalimentar.network.DonationsService;
import com.android.proyectoalimentar.network.RetrofitServices;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class FoodLocationsRepository {

    private final DonationsService donationsService;

    private List<FoodLocation> givers = new LinkedList<>();
    private List<FoodLocation> receivers = new LinkedList<>();

    @Inject
    public FoodLocationsRepository() {
        donationsService = RetrofitServices.getService(DonationsService.class);
    }

    public void getFoodGivers(double lat, double lng, double radius,
                              RepoCallback<List<FoodLocation>> repoCallback) {
        if (!givers.isEmpty()) {
            repoCallback.onSuccess(givers);
            return;
        }
        donationsService.fetchDonators(lat, lng, radius)
                .enqueue(createSimpleCallback(repoCallback, givers));
    }

    public void getFoodReceivers(double lat, double lng, double radius,
                                 RepoCallback<List<FoodLocation>> repoCallback) {
        if (!receivers.isEmpty()) {
            repoCallback.onSuccess(receivers);
            return;
        }
        donationsService.fetchCenters(lat, lng, radius)
                .enqueue(createSimpleCallback(repoCallback, receivers));
    }

    private Callback<List<FoodLocation>> createSimpleCallback(
            final RepoCallback<List<FoodLocation>> repoCallback, final List<FoodLocation> list) {
        return new Callback<List<FoodLocation>>() {
            @Override
            public void onResponse(Call<List<FoodLocation>> call,
                                   Response<List<FoodLocation>> response) {
                if (response.isSuccessful()) {
                    list.clear();
                    list.addAll(response.body());
                    repoCallback.onSuccess(list);
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
