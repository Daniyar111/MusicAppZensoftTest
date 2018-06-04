package com.example.saint.musicappzensoft.ui.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.widget.Toast;

import com.example.saint.musicappzensoft.data.db.SQLiteHelper;
import com.example.saint.musicappzensoft.data.entity.MusicModel;
import com.example.saint.musicappzensoft.data.manager.ResourceManager;
import com.example.saint.musicappzensoft.data.manager.SystemServiceManager;
import com.example.saint.musicappzensoft.services.MusicService;

import java.util.ArrayList;

public class MainPresenter implements MainContract.Presenter{

    private MainContract.View mView;
    private SystemServiceManager mSystemServiceManager;
    private ServiceConnection mConnection;
    private MusicService mService;
    private boolean mIsBound;

    private boolean mPositionFlag = true;
    private int mPosition;
    private ArrayList<MusicModel> mMusicModels = new ArrayList<>();
    private SQLiteHelper mSQLiteHelper;

    public MainPresenter(SystemServiceManager systemServiceManager, MusicService service, SQLiteHelper sqLiteHelper){
        mSystemServiceManager = systemServiceManager;
        mService = service;
        mSQLiteHelper = sqLiteHelper;
    }

    @Override
    public void fragmentSwitcher() {
        ConnectivityManager manager = (ConnectivityManager) mSystemServiceManager.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager != null){
            if(manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnected()){
                if(isViewAttached()) mView.goToWithInternet();
            }
            else{
                if(isViewAttached()) mView.goToWithoutInternet();
            }
        }
    }

    @Override
    public ServiceConnection getServiceConnection() {
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mIsBound = true;
                MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
                mService = binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mIsBound = false;
            }
        };
        return mConnection;
    }

    @Override
    public void getMusicList(ArrayList<MusicModel> musicModels) {
        mMusicModels = musicModels;
    }

    @Override
    public void musicMover(int position) {
        if(mPositionFlag){
            mPosition = position;
        }
//        mSQLiteHelper.deleteMusicTable();
//        mSQLiteHelper.saveMusicPosition(position);
//        mSQLiteHelper.saveMusicModels(mMusicModels);

        mService.playMusic(position, mMusicModels);
        if(mMusicModels != null && isViewAttached()){
            mView.onMusicPlay(position, mMusicModels);
        }
    }

//    @Override
//    public void savedMusicMover() {
//        if(mMusicModels != null && isViewAttached() && mSQLiteHelper != null){
//            mView.onMusicPlay(mSQLiteHelper.getMusicPosition(), mSQLiteHelper.getMusicModels());
//        }
//    }

    @Override
    public void onClickPrevious() {
        mPositionFlag = false;
        if(mPosition == 0){
            mPosition = mMusicModels.size() - 1;
            musicMover(mPosition);
        }
        else{
            mPosition--;
            musicMover(mPosition);
        }
    }

    @Override
    public void onClickNext() {
        mPositionFlag = false;
        if(mPosition == mMusicModels.size() - 1){
            mPosition = 0;
            musicMover(mPosition);
        }
        else{
            mPosition++;
            musicMover(mPosition);
        }
    }

    @Override
    public void onClickPlay() {
        if(!mService.handleMusicPlay() && isViewAttached()){
            mView.changeButtonPlay(false);
        }
        else{
            if(isViewAttached()){
                mView.changeButtonPlay(true);
            }
        }
    }

    @Override
    public void setVolume(int progress) {
        float volumeNumber = progress / 100f;
        mService.volumeController(volumeNumber);
    }

    @Override
    public ServiceConnection getConnection() {
        return mConnection;
    }

    @Override
    public boolean isServiceBound() {
        return mIsBound;
    }

//    @Override
//    public boolean isMusicPlaying() {
//        return mService.isServiceWorking();
//    }

    @Override
    public void bind(MainContract.View view) {
        mView = view;
    }

    @Override
    public void unbind() {
        mView = null;
    }

    private boolean isViewAttached(){
        return mView != null;
    }
}
