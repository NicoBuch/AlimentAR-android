package com.android.proyectoalimentar.repository;

import com.android.proyectoalimentar.model.Donation;
import com.android.proyectoalimentar.network.DonationsService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class DonationsRepository {

    @Inject DonationsService donationsService;

    @Inject
    public DonationsRepository() {
    }

    public void createDonation(int foodLocationId, RepoCallback<Boolean> repoCallback) {
        donationsService.activateDonation(foodLocationId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        repoCallback.onSuccess(response.isSuccessful());
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        repoCallback.onError(t.getMessage());
                    }
                });
    }

    public void getDonationsList(RepoCallback<List<Donation>> repoCallback) {
        donationsService.fetchDonationsList()
                .enqueue(new Callback<List<Donation>>() {
                    @Override
                    public void onResponse(Call<List<Donation>> call,
                                           Response<List<Donation>> response) {
                        if (response.isSuccessful()) {
                            repoCallback.onSuccess(response.body());
                        } else {
                            repoCallback.onError(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Donation>> call, Throwable t) {
                        repoCallback.onError(t.getMessage());
                    }
                });
    }

    public void deactivateDonation(Donation donation ,RepoCallback<Boolean> repoCallback){
        donationsService.deactivate(donation.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                repoCallback.onSuccess(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                repoCallback.onError(t.getMessage());
            }
        });
    }

}
