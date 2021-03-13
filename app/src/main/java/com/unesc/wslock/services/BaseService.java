package com.unesc.wslock.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseService {
    /** localhost */
    public static final String BASE_URL = "http://10.0.2.2:8000/";

    private static Retrofit retrofitInstance;

    public static Retrofit getRetrofitInstance() {
        if (retrofitInstance == null) {
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(BaseService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofitInstance;
    }
}
