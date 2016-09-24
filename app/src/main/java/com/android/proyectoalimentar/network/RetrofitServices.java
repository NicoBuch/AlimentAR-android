package com.android.proyectoalimentar.network;

import com.android.proyectoalimentar.Configuration;
import com.android.proyectoalimentar.network.serializers.DateTimeTypeAdapter;
import com.android.proyectoalimentar.utils.StorageUtils;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServices {

    private static final String AUTHORIZATION = "Authorization";

    private static Retrofit sRetrofit;
    private static Map<Class, Object> sServices;

    static {
        init();
    }

    public static void init() {
        sServices = new HashMap<>();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    if (StorageUtils.keyExists(Configuration.ACCESS_TOKEN)) {
                        request = chain.request().newBuilder()
                            .addHeader(AUTHORIZATION, StorageUtils.getStringFromSharedPreferences(
                                    Configuration.ACCESS_TOKEN, null))
                            .build();
                    }
                    return chain.proceed(request);
                })
                .build();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(DateTime.class, new DateTimeTypeAdapter())
                .create();

        sRetrofit = new Retrofit.Builder()
                .baseUrl(Configuration.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }

    public static <T> T getService(Class<T> clazz) {
        T service = (T) sServices.get(clazz);
        if (service != null) return service;
        service = sRetrofit.create(clazz);
        sServices.put(clazz, service);
        return service;
    }

}
