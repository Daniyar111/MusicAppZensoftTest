package com.example.saint.musicappzensoft.data.manager;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.saint.musicappzensoft.config.AppConstants;
import com.example.saint.musicappzensoft.config.PermissionConfig;
import com.example.saint.musicappzensoft.data.entity.MusicModel;

import java.util.ArrayList;

public final class PermissionManager {

    private Context mContext;
    private Activity mActivity;

    public PermissionManager(Context context, Activity activity) {
        mContext = context;
        mActivity = activity;
    }

    public boolean isExternalStoragePermission() {
        return PermissionConfig.checkExternalStoragePermission(mContext, mActivity);
    }

    public boolean isReadStoragePermissionGranted() {
        return PermissionConfig.isReadDataPermissionGranted(mContext);
    }

    public ArrayList<MusicModel> getExternalStorageTrack() {
        ContentResolver contentResolver = mActivity.getContentResolver();
        Cursor cursorAudio = contentResolver.query(AppConstants.AUDIO_STORAGE_URI, null, MediaStore.Audio.Media.DATA + " LIKE ? ",
                new String[]{
                        "%ZensoftMusics%"
                }, "", null);

        ArrayList<MusicModel> musicModels = new ArrayList<>();

        if (isCursorNotNull(cursorAudio) && cursorAudio.moveToFirst()) {
            do {
                long id = cursorAudio.getLong(cursorAudio.getColumnIndex(MediaStore.Audio.Media._ID));
                String songName = cursorAudio.getString(cursorAudio.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist = cursorAudio.getString(cursorAudio.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String data = cursorAudio.getString(cursorAudio.getColumnIndex(MediaStore.Audio.Media.DATA));
                String albumId = cursorAudio.getString(cursorAudio.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

                MusicModel musicModel = new MusicModel();
                musicModel.setSong(songName);
                musicModel.setArtists(artist);
                musicModel.setId(id);
                musicModel.setUrl(data);
                musicModels.add(musicModel);

                Cursor cursorAlbum = contentResolver.query(AppConstants.ALBUM_STORAGE_URI, null, MediaStore.Audio.Albums._ID + " = ? ",
                        new String[]{
                                albumId
                        }, "", null);

                if (isCursorNotNull(cursorAlbum) && cursorAlbum.moveToFirst()) {
                    String albumCover = cursorAlbum.getString(cursorAlbum.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                    if (albumCover != null) musicModel.setCoverImage(albumCover);
                }
                if (isCursorNotNull(cursorAlbum)) cursorAlbum.close();
            } while (cursorAudio.moveToNext());
        }
        if (isCursorNotNull(cursorAudio)) cursorAudio.close();
        return musicModels;
    }

    private boolean isCursorNotNull(Cursor cursor) {
        return cursor != null;
    }
}
