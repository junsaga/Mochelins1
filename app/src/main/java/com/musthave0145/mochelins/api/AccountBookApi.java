package com.musthave0145.mochelins.api;

import com.musthave0145.mochelins.model.Account;
import com.musthave0145.mochelins.model.AccountListRes;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AccountBookApi {

    @POST("/account")
    Call<Void> addAccount(@Header("Authorization") String token,
                             @Body Account account);


}
