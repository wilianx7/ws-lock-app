package com.unesc.wslock.services;

import com.unesc.wslock.models.Lock;
import com.unesc.wslock.models.lists.LockList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LockService {
    @GET("locks")
    Call<LockList> index(@Header("Authorization") String authorization, @Query("with_relations") String withRelations);

    @GET("locks/{id}")
    Call<Lock> show(@Header("Authorization") String authorization, @Path("id") String id);

    @DELETE("locks/{id}")
    Call<Boolean> destroy(@Header("Authorization") String authorization, @Path("id") String id);

    @PUT("locks/create-or-update")
    Call<Lock> createOrUpdate(@Header("Authorization") String authorization, @Body RequestBody object);
}
