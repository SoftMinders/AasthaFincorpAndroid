package com.af.aasthafincorp.Utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static Retrofit retrofit = null;

    public static Retrofit getApiClient() {
        System.setProperty("http.keepAlive", "false");
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();
        if (retrofit == null) {
            Gson gson = new GsonBuilder().serializeNulls().setLenient().create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(ConstantClass.endPoints)
                    .addConverterFactory(GsonConverterFactory.create(gson))

                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getApiClient2() {
        System.setProperty("http.keepAlive", "false");
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();
        if (retrofit == null) {
            Gson gson = new GsonBuilder().serializeNulls().setLenient().create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(ConstantClass.endPoints)


                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
}
