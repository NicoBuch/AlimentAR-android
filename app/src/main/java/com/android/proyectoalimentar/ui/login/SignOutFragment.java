package com.android.proyectoalimentar.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.android.proyectoalimentar.AlimentarApp;
import com.android.proyectoalimentar.R;
import com.android.proyectoalimentar.repository.RepoCallback;
import com.android.proyectoalimentar.repository.UserRepository;
import com.android.proyectoalimentar.utils.UserStorage;

import javax.inject.Inject;


public class SignOutFragment extends Fragment {


    private static final long exponentialBackOffTimeLimit = 1000 * 5; //5 sec limit

    @Inject
    UserStorage userStorage;
    @Inject
    UserRepository userRepository;

    long exponentialBackOffTime = 1;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        AlimentarApp.inject(this);

        userRepository.signOut(createSimpleRepoCallBack());
    }

    private RepoCallback<Void> createSimpleRepoCallBack(){
        return new RepoCallback<Void>() {
            @Override
            public void onSuccess(Void value) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }

            @Override
            public void onError(String error) {
                if(exponentialBackOffTime > exponentialBackOffTimeLimit){
                    if (getActivity() == null || getActivity().isFinishing()) {
                        return;
                    }
                    Toast.makeText(getContext(), R.string.signout_error, Toast.LENGTH_SHORT);
                }else {
                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        userRepository.signOut(createSimpleRepoCallBack());
                    }, exponentialBackOffTime);
                    exponentialBackOffTime = exponentialBackOffTime * 2;
                }
            }
        };
    }

}
