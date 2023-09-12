package com.musthave0145.mochelins.api;

import com.musthave0145.mochelins.model.SearchMeetingRes;
import com.musthave0145.mochelins.model.SearchRecentRes;
import com.musthave0145.mochelins.model.SearchRelRes;
import com.musthave0145.mochelins.model.SearchResultRes;
import com.musthave0145.mochelins.model.SearchReviewRes;
import com.musthave0145.mochelins.model.SearchStoreRes;
import com.musthave0145.mochelins.model.SearchUserRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface SearchApi {


    @GET("/search/review")
    Call<SearchReviewRes> getSRList(@Header("Authorization") String token,
                                    @Query("offset") int offset, @Query("limit") int limit,
                                    @Query("search") String search
    );

    @GET("/search/store")
    Call<SearchStoreRes> getSSList(@Header("Authorization") String token,
                                   @Query("offset") int offset, @Query("limit") int limit,
                                   @Query("search") String search
    );
    @GET("/search/user")
    Call<SearchUserRes> getSUList(@Header("Authorization") String token,
                                  @Query("offset") int offset, @Query("limit") int limit,
                                  @Query("search") String search
    );


    @GET("/search/meeting")
    Call<SearchMeetingRes> getSMList(@Header("Authorization") String token,
                                     @Query("offset") int offset, @Query("limit") int limit,
                                     @Query("search") String search
    );
    @GET("/search")
    Call<SearchResultRes> getResultList(@Header("Authorization") String token,
                                       @Query("search") String search
    );

    @GET("/search/recent")
    Call<SearchRecentRes> getResentList(@Header("Authorization") String token
                                        );

    @GET("/search/relation")
    Call<SearchRelRes> getRelList(@Header("Authorization") String token,
                                  @Query("search") String search
    );

}
