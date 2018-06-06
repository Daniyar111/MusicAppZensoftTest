package com.example.saint.musicappzensoft.data.manager;

import android.content.Context;

import com.example.saint.musicappzensoft.MusicApplication;
import com.example.saint.musicappzensoft.data.entity.MusicModel;
import com.example.saint.musicappzensoft.data.network.RetrofitService;

import java.util.ArrayList;

import retrofit2.Call;

public final class RetrofitServiceManager {

    private Context mContext;
    private RetrofitService mService;

    public RetrofitServiceManager(Context context) {
        mContext = context;
        mService = MusicApplication.get(mContext).getService();
    }

    public Call<ArrayList<MusicModel>> getMusicList() {
        return mService.getMusicList();
    }
}
