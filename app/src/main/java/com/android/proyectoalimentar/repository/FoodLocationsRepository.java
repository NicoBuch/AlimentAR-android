package com.android.proyectoalimentar.repository;

import com.android.proyectoalimentar.model.Donation;
import com.android.proyectoalimentar.model.FoodLocation;
import com.android.proyectoalimentar.model.FridgeId;
import com.android.proyectoalimentar.network.DonationsService;
import com.android.proyectoalimentar.network.RetrofitServices;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class FoodLocationsRepository {

    private final DonationsService donationsService;

    private List<Donation> givers = new LinkedList<>();
    private List<Donation> receivers = new LinkedList<>();

    @Inject
    public FoodLocationsRepository() {
        donationsService = RetrofitServices.getService(DonationsService.class);
    }

    public void getFoodGivers(double lat, double lng, double radius, boolean useCache,
                              RepoCallback<List<Donation>> repoCallback) {
        //If the list of givers is not empty and you must use cache
        if (!givers.isEmpty() && useCache) {
            repoCallback.onSuccess(givers);
            return;
        }
        donationsService.fetchDonators(lat, lng, radius)
                .enqueue(createSimpleCallback(repoCallback, givers));
    }

    public void getFoodReceivers(double lat, double lng, double radius, boolean useCache,
                                 RepoCallback<List<Donation>> repoCallback) {
        //If the list of givers is not empty and you must use cache
        if (!receivers.isEmpty() && useCache) {
            repoCallback.onSuccess(receivers);
            return;
        }
        List<FoodLocation> foodLocations = new LinkedList<>();
        donationsService.fetchCenters(lat, lng, radius)
                .enqueue(createSimpleCallback(new RepoCallback<List<FoodLocation>>() {
                    @Override
                    public void onSuccess(List<FoodLocation> foodLocations) {
                        receivers.clear();
                        for (FoodLocation foodLocation : foodLocations) {
                            receivers.add(new Donation(foodLocation));
                        }
                        repoCallback.onSuccess(receivers);
                    }

                    @Override
                    public void onError(String error) {
                        repoCallback.onError(error);
                    }
                }, foodLocations));
    }

    public void assignFridge(int donationId, int fridgeId, RepoCallback<Boolean> repoCallback){
        donationsService.assignFridge(donationId,new FridgeId(fridgeId))
        .enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                repoCallback.onSuccess(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                repoCallback.onSuccess(false);
            }
        });
    }

    private <T> Callback<List<T>> createSimpleCallback(
            final RepoCallback<List<T>> repoCallback, final List<T> list) {
        return new Callback<List<T>>() {
            @Override
            public void onResponse(Call<List<T>> call,
                                   Response<List<T>> response) {
                if (response.isSuccessful()) {
                    list.clear();
                    list.addAll(response.body());
                    repoCallback.onSuccess(list);
                } else {
                    repoCallback.onError(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<T>> call, Throwable t) {
                repoCallback.onError(t.getLocalizedMessage());
            }
        };
    }

}
