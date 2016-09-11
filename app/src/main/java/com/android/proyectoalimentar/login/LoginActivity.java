package com.android.proyectoalimentar.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.proyectoalimentar.AlimentarApp;
import com.android.proyectoalimentar.R;
import com.android.proyectoalimentar.map.MapActivity;
import com.android.proyectoalimentar.model.AuthenticatedUser;
import com.android.proyectoalimentar.network.LoginService;
import com.android.proyectoalimentar.network.RetrofitServices;
import com.android.proyectoalimentar.utils.UserStorage;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {

    @BindView(R.id.login_button)
    LoginButton loginButton;

    private CallbackManager callbackManager;
    private LoginService loginService;

    @Inject
    UserStorage userStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlimentarApp.inject(this);
        if (userStorage.isUserLogged()) {
            startActivity(new Intent(this, MapActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.login_fragment);
        ButterKnife.bind(this);

        loginService = RetrofitServices.getService(LoginService.class);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile", "email");

        callbackManager = CallbackManager.Factory.create();
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginWithToken(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                showFbLoginError();
            }

            @Override
            public void onError(FacebookException exception) {
                showFbLoginError();
            }
        });
    }

    private void loginWithToken(String accessToken) {
        loginService.facebookLogin(accessToken)
                .enqueue(new Callback<AuthenticatedUser>() {
                    @Override
                    public void onResponse(Call<AuthenticatedUser> call,
                                           Response<AuthenticatedUser> response) {
                        if (response.isSuccessful()) {
                            userStorage.login(response.body());
                            startActivity(new Intent(LoginActivity.this, MapActivity.class));
                            finish();
                        } else {
                            showFbLoginError();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthenticatedUser> call, Throwable t) {
                        showFbLoginError();
                    }
                });
    }

    private void showFbLoginError() {
        Toast.makeText(LoginActivity.this, R.string.facebook_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
