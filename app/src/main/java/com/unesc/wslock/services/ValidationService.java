package com.unesc.wslock.services;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface ValidationService {
    @PUT("validations/unique")
    Call<String> unique(@Body RequestBody object);
}
