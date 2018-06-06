package com.example.saint.musicappzensoft.data.network;

import android.support.annotation.NonNull;

import com.example.saint.musicappzensoft.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class NetworkBuilder {

    private static RetrofitService sService = null;

    public static RetrofitService initService() {

        if (sService == null) {
            sService = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getClient())
                    .build()
                    .create(RetrofitService.class);
        }
        return sService;
    }

    private static OkHttpClient getClient() {

        return new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {

                        Request.Builder ongoing = chain.request()
                                .newBuilder()
                                .addHeader("Accept", "application/json;versions=1");

                        return chain.proceed(ongoing.build());
                    }
                })
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }
}
