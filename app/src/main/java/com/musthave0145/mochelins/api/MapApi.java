package com.musthave0145.mochelins.api;


import com.musthave0145.mochelins.model.MapListRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface MapApi {
    @GET("/maps")
    Call<MapListRes> getPlaceList(
            @Header("Authorization") String token,
            @Query("lat") double lat,
                                  @Query("lng") double lng,
                                  @Query("dis") double dis
                                  );

}
