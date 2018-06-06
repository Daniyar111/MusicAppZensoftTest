package com.example.saint.musicappzensoft;

import android.app.Application;
import android.content.Context;

import com.example.saint.musicappzensoft.data.db.SQLiteHelper;
import com.example.saint.musicappzensoft.data.network.NetworkBuilder;
import com.example.saint.musicappzensoft.data.network.RetrofitService;

public class MusicApplication extends Application {

    private RetrofitService sService;
    private SQLiteHelper mSQLiteHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        sService = NetworkBuilder.initService();
        mSQLiteHelper = new SQLiteHelper(getApplicationContext());
    }

    public static MusicApplication get(Context context) {
        return (MusicApplication) context.getApplicationContext();
    }

    public RetrofitService getService() {
        return sService;
    }

    public SQLiteHelper getSQLiteHelper() {
        return mSQLiteHelper;
    }
}
