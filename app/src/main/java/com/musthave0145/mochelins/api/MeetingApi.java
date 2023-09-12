package com.musthave0145.mochelins.api;

import com.musthave0145.mochelins.model.Meeting;
import com.musthave0145.mochelins.model.MeetingListRes;
import com.musthave0145.mochelins.model.MeetingRes;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MeetingApi {

    // 모임 리스트 가져오는 API
    @GET("/meeting/list")
    Call<MeetingListRes> getMeetingList(@Header("Authorization") String token,
                                        @Query("offset") int offset, @Query("limit") int limit,
                                        @Query("lat") double lat, @Query("lng") double lng,
                                        @Query("dis") double dis);

    // 특정 모임의 상세내용 가져오는 API
    @GET("/meeting/{meetingId}")
    Call<MeetingRes> getMeetingDetail(@Header("Authorization") String token,
                                      @Path("meetingId") int meetingId);

    // 모임 생성 API
    @Multipart
    @POST("/meeting")
    Call<MeetingListRes> addMeeting(@Header("Authorization") String token,
                                    @Part("content") RequestBody content,
                                    @Part("storeName") RequestBody storeName,
                                    @Part("storeLat") RequestBody storeLat,
                                    @Part("storeLng") RequestBody storeLng,
                                    @Part("storeAddr") RequestBody storeAddr,
                                    @Part("date") RequestBody date,
                                    @Part("maximum") RequestBody maximum,
                                    @Part("pay") RequestBody pay,
                                    @Part MultipartBody.Part photo);
    // 모임 생성 - 사진 X
    @Multipart
    @POST("/meeting")
    Call<MeetingListRes> addMeeting(@Header("Authorization") String token,
                                    @Part("content") RequestBody content,
                                    @Part("storeName") RequestBody storeName,
                                    @Part("storeLat") RequestBody storeLat,
                                    @Part("storeLng") RequestBody storeLng,
                                    @Part("storeAddr") RequestBody storeAddr,
                                    @Part("date") RequestBody date,
                                    @Part("maximum") RequestBody maximum,
                                    @Part("pay") RequestBody pay);

    // 모임 생성 - 페이 X
    @Multipart
    @POST("/meeting")
    Call<MeetingListRes> addMeeting(@Header("Authorization") String token,
                                    @Part("content") RequestBody content,
                                    @Part("storeName") RequestBody storeName,
                                    @Part("storeLat") RequestBody storeLat,
                                    @Part("storeLng") RequestBody storeLng,
                                    @Part("storeAddr") RequestBody storeAddr,
                                    @Part("date") RequestBody date,
                                    @Part("maximum") RequestBody maximum,
                                    @Part MultipartBody.Part photo);

    // 모임 생성 - 사진과 페이 둘 다 X
    @Multipart
    @POST("/meeting")
    Call<MeetingListRes> addMeeting(@Header("Authorization") String token,
                                    @Part("content") RequestBody content,
                                    @Part("storeName") RequestBody storeName,
                                    @Part("storeLat") RequestBody storeLat,
                                    @Part("storeLng") RequestBody storeLng,
                                    @Part("storeAddr") RequestBody storeAddr,
                                    @Part("date") RequestBody date,
                                    @Part("maximum") RequestBody maximum);

    @Multipart
    @PUT("/meeting/{meetingId}")
    Call<MeetingListRes> editMeeting(@Header("Authorization") String token,
                                    @Path("meetingId") int meetingId,
                                    @Part("content") RequestBody content,
                                    @Part("storeName") RequestBody storeName,
                                    @Part("storeLat") RequestBody storeLat,
                                    @Part("storeLng") RequestBody storeLng,
                                    @Part("storeAddr") RequestBody storeAddr,
                                    @Part("date") RequestBody date,
                                    @Part("maximum") RequestBody maximum,
                                    @Part("pay") RequestBody pay,
                                    @Part MultipartBody.Part photo);
    @Multipart
    @PUT("/meeting/{meetingId}")
    Call<MeetingListRes> editMeeting(@Header("Authorization") String token,
                                    @Path("meetingId") int meetingId,
                                    @Part("content") RequestBody content,
                                    @Part("storeName") RequestBody storeName,
                                    @Part("storeLat") RequestBody storeLat,
                                    @Part("storeLng") RequestBody storeLng,
                                    @Part("storeAddr") RequestBody storeAddr,
                                    @Part("date") RequestBody date,
                                    @Part("maximum") RequestBody maximum,
                                    @Part MultipartBody.Part photo);
    @Multipart
    @PUT("/meeting/{meetingId}")
    Call<MeetingListRes> editMeeting(@Header("Authorization") String token,
                                    @Path("meetingId") int meetingId,
                                    @Part("content") RequestBody content,
                                    @Part("storeName") RequestBody storeName,
                                    @Part("storeLat") RequestBody storeLat,
                                    @Part("storeLng") RequestBody storeLng,
                                    @Part("storeAddr") RequestBody storeAddr,
                                    @Part("date") RequestBody date,
                                    @Part("maximum") RequestBody maximum,
                                    @Part("pay") RequestBody pay);
    @Multipart
    @PUT("/meeting/{meetingId}")
    Call<MeetingListRes> editMeeting(@Header("Authorization") String token,
                                    @Path("meetingId") int meetingId,
                                    @Part("content") RequestBody content,
                                    @Part("storeName") RequestBody storeName,
                                    @Part("storeLat") RequestBody storeLat,
                                    @Part("storeLng") RequestBody storeLng,
                                    @Part("storeAddr") RequestBody storeAddr,
                                    @Part("date") RequestBody date,
                                    @Part("maximum") RequestBody maximum);




    // 모임 참가 API
    @POST("/meeting/{meetingId}/attend")
    Call<MeetingRes> attendMeeting(@Header("Authorization") String token,
                                   @Path("meetingId") int meetingId);

    // 모임 취소 API
    @DELETE("/meeting/{meetingId}/attend")
    Call<MeetingRes> cancleMeeting(@Header("Authorization") String token,
                                   @Path("meetingId") int meetingId);

    // 모임 수정 API

    // 모임 삭제 API
    @DELETE("/meeting/{meetingId}")
    Call<MeetingRes> deleteMeeting(@Header("Authorization") String token,
                                   @Path("meetingId") int meetingId);

}
