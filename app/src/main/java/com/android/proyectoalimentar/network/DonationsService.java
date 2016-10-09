package com.android.proyectoalimentar.network;

import com.android.proyectoalimentar.model.Donation;
import com.android.proyectoalimentar.model.FoodLocation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DonationsService {

    @GET("fridges")
    Call<List<FoodLocation>> fetchCenters(
            @Query("lat") double lat, @Query("lng") double lng, @Query("radius") double radius);

    @GET("donations/open")
    Call<List<Donation>> fetchDonators(
            @Query("lat") double lat, @Query("lng") double lng, @Query("radius") double radius);

    @POST("donations/{id}/activate")
    Call<Void> activateDonation(@Path("id") int id);

    @GET("donations/active")
    Call<List<Donation>> fetchDonationsList();

}
