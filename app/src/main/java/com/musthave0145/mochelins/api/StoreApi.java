package com.musthave0145.mochelins.api;

import com.musthave0145.mochelins.model.MeetingListRes;
import com.musthave0145.mochelins.model.MeetingRes;
import com.musthave0145.mochelins.model.ReviewListRes;
import com.musthave0145.mochelins.model.ReviewRes;
import com.musthave0145.mochelins.model.Store;
import com.musthave0145.mochelins.model.StoreRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StoreApi {
    @GET("/maps/{storeId}")
    Call<StoreRes> getStoreList(@Header("Authorization") String token,
                             @Path("storeId") int storeId);

    // 특정 리뷰 가저오는 API
    @GET("/review/{reviewId}")
    Call<ReviewListRes> getReviewDetail(@Header("Authorization") String token,
                                        @Path("reviewId") int reviewId);

    // 특정 가게의 상세정보를 가져오는 API
    @GET("/store/{storeId}")
    Call<StoreRes> getStoreDetail(@Header("Authorization") String token,
                                  @Path("storeId") int storeId);

    // 특정 가게의 미팅 리스트를 가져오는 API
    @GET("/store/{storeId}/meeting")
    Call<MeetingListRes> getStoreMeetingList(@Header("Authorization") String token,
                                             @Path("storeId") int storeId,
                                             @Query("offset") int offset, @Query("limit") int limit);

    // 특정 가게의 리뷰 리스트를 가져오는 API
    @GET("/store/{storeId}/review")
    Call<ReviewRes> getStoreReviewList(@Header("Authorization") String token,
                                       @Path("storeId") int storeId,
                                       @Query("offset") int offset, @Query("limit") int limit);
}
