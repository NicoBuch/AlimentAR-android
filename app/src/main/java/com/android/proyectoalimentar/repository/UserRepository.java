package com.android.proyectoalimentar.repository;

import com.android.proyectoalimentar.model.User;
import com.android.proyectoalimentar.network.LoginService;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by leonelbadi on 5/12/16.
 */

public class UserRepository {

    @Inject
    LoginService loginService;

    @Inject UserRepository(){

    }

    public void getMyInformation(RepoCallback<User> repoCallback){
        loginService.getMyInformation().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    repoCallback.onSuccess(response.body());
                }else{
                    repoCallback.onError(null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                repoCallback.onError(t.getMessage());
            }
        });
    }
}
