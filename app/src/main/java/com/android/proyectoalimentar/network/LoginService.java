package com.android.proyectoalimentar.network;

import com.android.proyectoalimentar.model.AuthenticatedUser;
import com.android.proyectoalimentar.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginService {

    @POST("volunteers/fb_connect")
    Call<AuthenticatedUser> facebookLogin(@Query("access_token") String accessToken);

    @GET("volunteers/me")
    Call<User> getMyInformation();

}
