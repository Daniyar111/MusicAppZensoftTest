package com.example.saint.musicappzensoft.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.saint.musicappzensoft.data.entity.MusicModel;

import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "MUSIC_PLAYER";
    private static final int DB_VERSION = 2;

    private static final String TABLE_SAVED_INSTANCE = "TABLE_SAVED_INSTANCE";

    private static final String ID = "_id";

    private static final String COVER_IMAGE = "COVER_IMAGE";
    private static final String SONG_NAME = "SONG_NAME";
    private static final String ARTISTS = "ARTISTS";
    private static final String DOWNLOAD_LINK = "DOWNLOAD_LINK";
    private static final String POSITION = "POSITION";

    private static final String CREATE_TABLE_SAVED_INSTANCE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_SAVED_INSTANCE + "(" +
            ID + " INTEGER_PRIMARY_KEY, " +
            COVER_IMAGE + " TEXT, " +
            SONG_NAME + " TEXT, " +
            ARTISTS + " TEXT, " +
            DOWNLOAD_LINK + " TEXT, " +
            POSITION + " INTEGER);";

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SAVED_INSTANCE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVED_INSTANCE);
        onCreate(db);
    }

    public void saveMusicModels(ArrayList<MusicModel> musicModels){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        for (int i = 0; i < musicModels.size(); i++) {
            MusicModel musicModel = musicModels.get(i);
            cv.put(COVER_IMAGE, musicModel.getCoverImage());
            cv.put(SONG_NAME, musicModel.getSong());
            cv.put(ARTISTS, musicModel.getArtists());
            cv.put(DOWNLOAD_LINK, musicModel.getUrl());
            db.insert(TABLE_SAVED_INSTANCE, null, cv);
        }
        db.close();
    }

    public void saveMusicPosition(int position){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(POSITION, position);
        db.insert(TABLE_SAVED_INSTANCE, null, cv);
        db.close();
    }

    public void deleteMusicTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SAVED_INSTANCE, null, null);
        db.close();
    }

    public ArrayList<MusicModel> getMusicModels(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<MusicModel> musicModels = new ArrayList<>();
        Cursor cursor = db.query(TABLE_SAVED_INSTANCE, null, null, null, null, null, null);

        if(cursor.moveToFirst()){
            int coverImageIndex = cursor.getColumnIndex(COVER_IMAGE);
            int songNameIndex = cursor.getColumnIndex(SONG_NAME);
            int artistsIndex = cursor.getColumnIndex(ARTISTS);
            int downloadLinkIndex = cursor.getColumnIndex(DOWNLOAD_LINK);
            do {
                MusicModel musicModel = new MusicModel();
                String coverImage = cursor.getString(coverImageIndex);
                String songName = cursor.getString(songNameIndex);
                String artists = cursor.getString(artistsIndex);
                String downloadLink = cursor.getString(downloadLinkIndex);
                musicModel.setCoverImage(coverImage);
                musicModel.setSong(songName);
                musicModel.setArtists(artists);
                musicModel.setUrl(downloadLink);
                musicModels.add(musicModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return musicModels;
    }

    public int getMusicPosition(){
        SQLiteDatabase db = this.getWritableDatabase();
        int position = 0;
        Cursor cursor = db.query(TABLE_SAVED_INSTANCE, null, null, null,null, null, null);

        if(cursor.moveToFirst()){
            int positionIndex = cursor.getColumnIndex(POSITION);
            do {
                position = cursor.getInt(positionIndex);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return position;
    }
}

