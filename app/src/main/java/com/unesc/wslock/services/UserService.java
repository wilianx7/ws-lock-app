package com.unesc.wslock.services;

import com.unesc.wslock.models.User;
import com.unesc.wslock.models.lists.UserList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {
    @GET("users")
    Call<UserList> index();

    @GET("users/{id}")
    Call<User> show(@Path("id") String id);

    @DELETE("users/{id}")
    Call<Boolean> destroy(@Path("id") String id);

    @PUT("users/create-or-update")
    Call<User> createOrUpdate(@Body RequestBody object);
}
