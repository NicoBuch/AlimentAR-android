package com.android.proyectoalimentar.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Service that handle the id token for notifications.
 */
public interface NotificationService {

    @POST("o_auth/save_token")
    @FormUrlEncoded
    Call<Void> registerToken(@Field("device_token") String token, @Field("device_type") String deviceType);

    @POST("o_auth/delete_token")
    @FormUrlEncoded
    Call<Void> deleteToken(@Field("device_token") String token);

}
