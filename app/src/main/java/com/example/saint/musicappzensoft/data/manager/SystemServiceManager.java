package com.example.saint.musicappzensoft.data.manager;

import android.content.Context;

public final class SystemServiceManager {

    private Context mContext;

    public SystemServiceManager(Context context) {
        mContext = context;
    }

    public Object getSystemService(String service) {
        return mContext.getSystemService(service);
    }
}
