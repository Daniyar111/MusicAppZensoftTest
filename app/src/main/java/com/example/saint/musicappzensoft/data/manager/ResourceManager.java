package com.example.saint.musicappzensoft.data.manager;

import android.content.Context;

public final class ResourceManager {

    private Context mContext;

    public ResourceManager(Context context){
        mContext = context;
    }

    public String getStringResource(int resId){
        return mContext.getString(resId);
    }
}
