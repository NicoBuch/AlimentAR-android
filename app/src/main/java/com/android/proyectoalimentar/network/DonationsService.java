package com.android.proyectoalimentar.network;

import com.android.proyectoalimentar.model.Donation;
import com.android.proyectoalimentar.model.FoodLocation;
import com.android.proyectoalimentar.model.FridgeId;
import com.android.proyectoalimentar.model.Qualification;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
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

    @POST("donations/{id}/deactivate")
    Call<Void> deactivate(@Path("id") int id);

    @POST("donations/{id}/qualify")
    Call<Void> qualify(@Path("id") Integer id, @Body Qualification qualification);

    @POST("donations/{id}/assign_fridge")
    Call<Void> assignFridge(@Path("id") Integer id, @Body FridgeId fridgeId);
}
