package com.example.saint.musicappzensoft.config;

import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

public class AppConstants {

    public static final String COMMON_ENDPOINT = "studio";
    public static final String FOLDER_NAME = "/ZensoftMusics/";
    public static final int READ_PERMISSION_REQUEST = 1;
    public static final Uri AUDIO_STORAGE_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    public static final Uri ALBUM_STORAGE_URI = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
    public static final String EXTERNAL_STORAGE = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final int DATA_BYTE = 4096;
    public static final int INPUT_STREAM_SIZE = 8192;
    public static final String MP3_FORMAT = ".mp3";
}
