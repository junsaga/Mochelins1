package com.musthave0145.mochelins.api;

import com.musthave0145.mochelins.model.Comment;
import com.musthave0145.mochelins.model.CommentRes;
import com.musthave0145.mochelins.model.ReviewListRes;
import com.musthave0145.mochelins.model.ReviewRes;

import java.util.List;

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

public interface ReviewApi {

    // 리뷰를 가져오는 API
    @GET("/review/list")
    Call<ReviewRes> getReviewList(@Header("Authorization") String token,
                                  @Query("offset") int offset, @Query("limit") int limit,
                                  @Query("lat") double lat, @Query("lng") double lng,
                                  @Query("dis") double dis);

    // 리뷰 작성 API
    @Multipart
    @POST("/review/add")
    Call<ReviewRes> addReview(@Header("Authorization") String token,
                              @Part MultipartBody.Part photo,
                              @Part("content") RequestBody content,
                              @Part("storeName") RequestBody storeName,
                              @Part("storeLat") RequestBody storeLat,
                              @Part("storeLng") RequestBody storeLng,
                              @Part("storeAddr") RequestBody storeAddr,
                              @Part("rating") RequestBody rating,
                              @Part("tag") RequestBody tag
    );

    // 리뷰 수정 API
    @Multipart
    @PUT("/review/{reviewId}")
    Call<ReviewRes> editReview(@Header("Authorization") String token,
                               @Path("reviewId") int reviewId,
                               @Part("content") RequestBody content,
                               @Part("storeName") RequestBody storeName,
                               @Part("storeLat") RequestBody storeLat,
                               @Part("storeLng") RequestBody storeLng,
                               @Part("storeAddr") RequestBody storeAddr,
                               @Part("rating") RequestBody rating
    );
    // 리뷰 수정 API
    @Multipart
    @PUT("/review/{reviewId}")
    Call<ReviewRes> editReview(@Header("Authorization") String token,
                               @Path("reviewId") int reviewId,
                               @Part("content") RequestBody content,
                               @Part("storeName") RequestBody storeName,
                               @Part("storeLat") RequestBody storeLat,
                               @Part("storeLng") RequestBody storeLng,
                               @Part("storeAddr") RequestBody storeAddr,
                               @Part("rating") RequestBody rating,
                               @Part MultipartBody.Part photo
    );
    // 리뷰 수정 API
    @Multipart
    @PUT("/review/{reviewId}")
    Call<ReviewRes> editReview(@Header("Authorization") String token,
                               @Path("reviewId") int reviewId,
                               @Part("content") RequestBody content,
                               @Part("storeName") RequestBody storeName,
                               @Part("storeLat") RequestBody storeLat,
                               @Part("storeLng") RequestBody storeLng,
                               @Part("storeAddr") RequestBody storeAddr,
                               @Part("rating") RequestBody rating,
                               @Part("tag") RequestBody tag
    );
    // 리뷰 수정 API
    @Multipart
    @PUT("/review/{reviewId}")
    Call<ReviewRes> editReview(@Header("Authorization") String token,
                               @Path("reviewId") int reviewId,
                               @Part("content") RequestBody content,
                               @Part("storeName") RequestBody storeName,
                               @Part("storeLat") RequestBody storeLat,
                               @Part("storeLng") RequestBody storeLng,
                               @Part("storeAddr") RequestBody storeAddr,
                               @Part("rating") RequestBody rating,
                               @Part("tag") RequestBody tag,
                               @Part MultipartBody.Part photo
    );

    // 특정 리뷰 가저오는 API
    @GET("/review/{reviewId}")
    Call<ReviewRes> getReviewDetail(@Header("Authorization") String token,
                                        @Path("reviewId") int reviewId);

    // 특정 리뷰 삭제하는 API
    @DELETE("/review/{reviewId}")
    Call<ReviewListRes> deleteReview(@Header("Authorization") String token,
                                     @Path("reviewId") int reviewId);


    // 좋아요 API
    @POST("/review/{reviewId}/like")
    Call<ReviewRes> likeReview(@Header("Authorization") String token,
                               @Path("reviewId") int reviewId);

    // 좋아요 삭제 API
    @DELETE("/review/{reviewId}/like")
    Call<ReviewRes> likeDeleteReview(@Header("Authorization") String token,
                                     @Path("reviewId") int reviewId);

    // 특정 게시물의 댓글 가져오는 API
    @GET("review/{reviewId}/comment")
    Call<CommentRes> reviewCommentList(@Header("Authorization") String token,
                                       @Path("reviewId") int reviewId,
                                       @Query("offset") int offset, @Query("limit") int limit);

    // 특정 게시물에 댓글 다는 API
    @POST("review/{reviewId}/comment")
    Call<CommentRes> reviewCommentAdd(@Header("Authorization") String token,
                                      @Path("reviewId") int reviewId,
                                      @Body Comment comment);

    // 특정 게시물에 단 댓글 삭제하는 API
    @DELETE("review/comment/{reviewId}")
    Call<CommentRes> reviewCommentDelete(@Header("Authorization") String token,
                                         @Path("reviewId") int reviewId);

    // 특정 게시물에 단 댓글 수정하는 API
    @PUT("review/comment/{reviewId}")
    Call<CommentRes> reviewCommentUpdate(@Header("Authorization") String token,
                                        @Path("reviewId") int reviewId,
                                        @Body Comment comment);
}
