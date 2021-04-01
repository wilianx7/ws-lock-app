package com.unesc.wslock.services;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PUT;

public interface MQTTService {
    @PUT("mqtt/open-door")
    Call<String> openDoor(@Header("Authorization") String authorization, @Body RequestBody object);

    @PUT("mqtt/close-door")
    Call<String> closeDoor(@Header("Authorization") String authorization, @Body RequestBody object);
}
