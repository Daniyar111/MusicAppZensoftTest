package com.example.saint.musicappzensoft.data.network;

import com.example.saint.musicappzensoft.config.AppConstants;
import com.example.saint.musicappzensoft.data.entity.MusicModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitService {

    @GET(AppConstants.COMMON_ENDPOINT)
    Call<ArrayList<MusicModel>> getMusicList();
}
