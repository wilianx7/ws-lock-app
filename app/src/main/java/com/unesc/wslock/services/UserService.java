package com.unesc.wslock.services;

import com.unesc.wslock.models.User;
import com.unesc.wslock.models.lists.UserList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    @GET("users")
    Call<UserList> index(@Header("Authorization") String authorization, @Query("with_relations") String withRelations, @Query("has_access_to_lock_id") int hasAccessToLockId);

    @GET("users/{id}")
    Call<User> show(@Header("Authorization") String authorization, @Path("id") String id);

    @DELETE("users/{id}")
    Call<Boolean> destroy(@Header("Authorization") String authorization, @Path("id") String id);

    @PUT("users/create-or-update")
    Call<User> create(@Body RequestBody object);

    @PUT("users/create-or-update")
    Call<User> update(@Header("Authorization") String authorization, @Body RequestBody object);
}
