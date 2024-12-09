package com.af.aasthafincorp.Utility;

import java.util.Collections;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientTest {
    private static Retrofit retrofit = null;

    public static Retrofit getApiClient() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .protocols(Collections.singletonList(Protocol.HTTP_1_1)) // Enforce HTTP/1.1
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(ConstantClass.endPoints)
                    .client(okHttpClient) // Set the custom OkHttpClient
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
