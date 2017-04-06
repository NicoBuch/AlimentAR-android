package com.android.proyectoalimentar.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.proyectoalimentar.AlimentarApp;
import com.android.proyectoalimentar.Configuration;
import com.android.proyectoalimentar.R;
import com.android.proyectoalimentar.network.NotificationService;
import com.android.proyectoalimentar.utils.StorageUtils;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String DEVICE_TYPE = "android";
    private static final String[] TOPICS = {"global"};

    @Inject
    NotificationService notificationService;

    public RegistrationIntentService() {
        super(TAG);
        AlimentarApp.inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Log.i(TAG,"Registration started");
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG, "GCM Registration Token: " + token);
            sendRegistrationTokenToServer(token);


            // Notify UI that registration has completed.
            Intent registrationComplete = new Intent(Configuration.REGISTRATION_COMPLETE);
            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
        }catch (Exception e){
            Log.e(TAG, "GCM Registration Token: " + "Registration Error", e);
            storeSendStatus(false);
        }
    }


    private void sendRegistrationTokenToServer(final String token) throws Exception{
        notificationService.registerToken(token, DEVICE_TYPE).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(!response.isSuccessful()) {
                    storeSendStatus(false);
                    Log.i(TAG, "GCM Registration Token: " + "Registration error");
                    return;
                }else{
                    Log.i(TAG, "GCM Registration Token: " + "Registration complete");
                    storeSendStatus(true);
                    storeToken(token);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i(TAG, "GCM Registration Token: " + "Registration error");
                storeSendStatus(false);
            }
        });
    }

    private void storeSendStatus(boolean send){
        StorageUtils.storeInSharedPreferences(Configuration.SENT_TOKEN_TO_SERVER, send);
    }

    private void storeToken(String token){
        StorageUtils.storeInSharedPreferences(Configuration.TOKEN, token);
    }
}
