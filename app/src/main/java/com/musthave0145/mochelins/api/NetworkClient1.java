package com.musthave0145.mochelins.api;



import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.musthave0145.mochelins.config.Config;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient1 {

    // 1. retrofit 을 멤버변수로 만들고, 다른 클래스에서도 사용할 수 있도록 공유한다.
    public static Retrofit retrofit;

    // 2. 메소드를 만들어준다.
    public static Retrofit getRetrofitClient(Context context){
        if(retrofit == null){
            // 네트워크 통신한 로그를 확인할 때 필요한 코드
            HttpLoggingInterceptor loggingInterceptor =
                    new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // 네트워크 연결시키는 코드.
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES) // 연결할때 1분 기다려줌
                    .readTimeout(1, TimeUnit.MINUTES)    // 읽을때
                    .writeTimeout(1, TimeUnit.MINUTES)
                    .addInterceptor(loggingInterceptor)
                    .build();
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(Config.HOST)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

}