package com.example.saint.musicappzensoft.config;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public final class PermissionConfig {

    public static boolean isReadDataPermissionNotGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isShowRequestPermissionRationale(Activity activity) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    public static void requestReadStoragePermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AppConstants.READ_PERMISSION_REQUEST);
    }

    public static boolean isReadDataPermissionGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkExternalStoragePermission(Context context, Activity activity) {
        if (isReadDataPermissionNotGranted(context)) {
            if (isShowRequestPermissionRationale(activity)) {
                requestReadStoragePermission(activity);
                return true;
            } else {
                requestReadStoragePermission(activity);
                return true;
            }
        }
        return false;
    }
}
