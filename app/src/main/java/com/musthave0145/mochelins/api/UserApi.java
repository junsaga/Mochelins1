package com.musthave0145.mochelins.api;

import com.musthave0145.mochelins.model.ResultRes;
import com.musthave0145.mochelins.model.User;
import com.musthave0145.mochelins.model.UserInfoMeetingRes;
import com.musthave0145.mochelins.model.UserInfoRes;
import com.musthave0145.mochelins.model.UserInfoReviewRes;
import com.musthave0145.mochelins.model.UserRes;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {
    // 회원가입 API
    @POST("/user/register")
    Call<UserRes> register(@Body User user);

    // 로그인 API
    @POST("/user/login")
    Call<UserRes> login(@Body User user);

    @POST("/user/login/kakao")
    Call<UserRes> login_kakao(@Body User user);

    // 로그아웃 API
    @DELETE("/user/logout")
    Call<UserRes> logout(@Header("Authorization") String token);


    @GET("/user/my_page")
    Call<UserInfoRes> my_page(@Header("Authorization") String token);

    @GET("/user/{userId}")
    Call<UserInfoRes> user_info(@Header("Authorization") String token,
                                @Path("userId") int userId);

    @Multipart
    @PUT("/user/edit")
    Call<ResultRes> user_edit(@Header("Authorization") String token,
                              @Part("email") RequestBody email,
                              @Part("name") RequestBody name,
                              @Part("nickname") RequestBody nickname);
    @Multipart
    @PUT("/user/edit")
    Call<ResultRes> user_edit(@Header("Authorization") String token,
                              @Part("email") RequestBody email,
                              @Part("name") RequestBody name,
                              @Part("nickname") RequestBody nickname,
                              @Part("password") RequestBody password);
    @Multipart
    @PUT("/user/edit")
    Call<ResultRes> user_edit(@Header("Authorization") String token,
                              @Part("email") RequestBody email,
                              @Part("name") RequestBody name,
                              @Part("nickname") RequestBody nickname,
                              @Part MultipartBody.Part profile);

    @Multipart
    @PUT("/user/edit")
    Call<ResultRes> user_edit(@Header("Authorization") String token,
                              @Part("email")RequestBody email,
                              @Part("name") RequestBody name,
                              @Part("nickname") RequestBody nickname,
                              @Part("password") RequestBody password,
                              @Part MultipartBody.Part profile);

    @GET("/user/{userId}/review")
    Call<UserInfoReviewRes> user_review(@Header("Authorization") String token,
                                        @Path("userId") int userId,
                                        @Query("offset") int offset,
                                        @Query("limit") int limit);

    @GET("/user/{userId}/likes")
    Call<UserInfoReviewRes> user_like(@Header("Authorization") String token,
                                      @Path("userId") int userId,
                                      @Query("offset") int offset,
                                      @Query("limit") int limit);

    @GET("/user/{userId}/meeting")
    Call<UserInfoMeetingRes> user_meeting(@Header("Authorization") String token,
                                       @Path("userId") int userId,
                                       @Query("offset") int offset,
                                       @Query("limit") int limit);

}
