package com.android.proyectoalimentar.network;

import com.android.proyectoalimentar.model.FoodLocation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DonationsService {

    @GET("fridges")
    Call<List<FoodLocation>> fetchCenters(
            @Query("lat") double lat, @Query("lng") double lng, @Query("radius") double radius);

    @GET("donators")
    Call<List<FoodLocation>> fetchDonators(
            @Query("lat") double lat, @Query("lng") double lng, @Query("radius") double radius);

}
