package com.unesc.wslock.services;

import com.unesc.wslock.models.Auth;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthService {
    @POST("login")
    Call<Auth> login(@Body RequestBody object);

    @POST("refresh")
    Call<Auth> refresh(@Header("Authorization") String authorization);
}
