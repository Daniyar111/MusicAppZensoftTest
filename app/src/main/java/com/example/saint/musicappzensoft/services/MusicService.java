package com.example.saint.musicappzensoft.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.saint.musicappzensoft.data.entity.MusicModel;

import java.io.IOException;
import java.util.ArrayList;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener{

    private MusicBinder mBinder = new MusicBinder();
    private MediaPlayer mPlayer;

    @Override
    public void onCreate() {
        super.onCreate();

        mPlayer = new MediaPlayer();
        mPlayer.setVolume(0.5f, 0.5f);
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnErrorListener(this);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

    }

    public class MusicBinder extends Binder {
        public MusicService getService(){
            return MusicService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "Start id: " + startId, Toast.LENGTH_LONG).show();

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
//        Toast.makeText(this, "onBind", Toast.LENGTH_LONG).show();
        return mBinder;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mPlayer.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mPlayer.start();
    }

    public boolean handleMusicPlay(){
        if(mPlayer.isPlaying() || mPlayer.isLooping()){
            mPlayer.pause();
            return false;
        }
        mPlayer.start();
        return true;
    }

    public void playMusic(int position, ArrayList<MusicModel> musicModels){
        mPlayer.reset();

        Uri uri = Uri.parse(musicModels.get(position).getUrl());
        if(uri == null){
            return;
        }
        try {
            mPlayer.setDataSource(getApplicationContext(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.prepareAsync();
    }

    public void volumeController(float volumeNumber){
        mPlayer.setVolume(volumeNumber, volumeNumber);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("DANI", "SERVICE IS DESTROYED");
        Toast.makeText(this, "service is destroyed", Toast.LENGTH_LONG).show();
        mPlayer.stop();
        mPlayer.release();
        stopSelf();
    }
}
