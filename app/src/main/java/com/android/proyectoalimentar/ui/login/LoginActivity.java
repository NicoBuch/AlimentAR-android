package com.android.proyectoalimentar.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.android.proyectoalimentar.AlimentarApp;
import com.android.proyectoalimentar.R;
import com.android.proyectoalimentar.model.AuthenticatedUser;
import com.android.proyectoalimentar.network.LoginService;
import com.android.proyectoalimentar.network.RetrofitServices;
import com.android.proyectoalimentar.ui.drawer.DrawerActivity;
import com.android.proyectoalimentar.utils.UserStorage;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {

    private final List<String> FB_PERMISSIONS = Arrays.asList("user_friends", "email");

    private CallbackManager callbackManager;
    private LoginService loginService;

    @BindView(R.id.facebook_login) View facebookLoginButton;
    @BindColor(R.color.light_gray) int disabledColor;
    @BindColor(R.color.com_facebook_blue) int enabledColor;

    @Inject UserStorage userStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlimentarApp.inject(this);
        if (userStorage.isUserLogged()) {
            startActivity(new Intent(this, DrawerActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.login_fragment);
        ButterKnife.bind(this);

        facebookLoginButton.setEnabled(false);

        loginService = RetrofitServices.getService(LoginService.class);

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginWithToken(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                Log.i("FB login", "Facebook Login Canceled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("FB Login", "Error: " + error.toString());
            }
        });
    }

    @OnClick(R.id.facebook_login)
    void loginWithFacebook(View facebookLoginButton) {
        if (facebookLoginButton.isEnabled()) {
            if (AccessToken.getCurrentAccessToken() != null) {
                loginWithToken(AccessToken.getCurrentAccessToken().getToken());
            } else {
                LoginManager.getInstance().logInWithReadPermissions(this, FB_PERMISSIONS);
            }
        }
    }

    @OnCheckedChanged(R.id.accept_terms)
    void onTermsAcceptedChanged(CompoundButton button, boolean checked) {
        facebookLoginButton.setEnabled(checked);
        facebookLoginButton.setBackgroundColor(checked ? enabledColor : disabledColor);
    }

    private void loginWithToken(String accessToken) {
        loginService.facebookLogin(accessToken)
                .enqueue(new Callback<AuthenticatedUser>() {
                    @Override
                    public void onResponse(Call<AuthenticatedUser> call,
                                           Response<AuthenticatedUser> response) {
                        if (response.isSuccessful()) {
                            userStorage.login(response.body());
                            startActivity(new Intent(LoginActivity.this, DrawerActivity.class));
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
