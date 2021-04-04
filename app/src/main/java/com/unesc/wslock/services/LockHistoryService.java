package com.unesc.wslock.services;

import com.unesc.wslock.models.lists.LockHistoryList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface LockHistoryService {
    @GET("lock-histories")
    Call<LockHistoryList> index(@Header("Authorization") String authorization, @Query("with_relations") String withRelations, @Query("filter[lock_id]") String lockId);
}
