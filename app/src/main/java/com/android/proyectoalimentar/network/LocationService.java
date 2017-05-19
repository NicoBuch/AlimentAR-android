package com.android.proyectoalimentar.network;

import com.android.proyectoalimentar.model.AuthenticatedUser;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by leonelbadi on 14/5/17.
 */

public interface LocationService {

    @PUT("volunteers/location")
    @FormUrlEncoded
    Call<Void> updateLocation(@Field("lat") double lat, @Field("lng") double longitude);
}
