package com.musthave0145.mochelins.api;

import com.musthave0145.mochelins.model.PlaceSelectList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlaceSelectApi {

    @GET("/search/place")
    Call<PlaceSelectList> getPlaceSelectList(@Query("lat") double lat,
                                             @Query("lng") double lng,
                                             @Query("search") String keyword);
}
