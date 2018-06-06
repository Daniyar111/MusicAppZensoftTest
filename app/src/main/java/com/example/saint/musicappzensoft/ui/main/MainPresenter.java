package com.example.saint.musicappzensoft.ui.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.IBinder;

import com.example.saint.musicappzensoft.data.db.SQLiteHelper;
import com.example.saint.musicappzensoft.data.entity.MusicModel;
import com.example.saint.musicappzensoft.data.manager.SystemServiceManager;
import com.example.saint.musicappzensoft.services.MusicService;

import java.util.ArrayList;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
    private SystemServiceManager mSystemServiceManager;
    private ServiceConnection mConnection;
    private MusicService mService;
    private boolean mIsBound;
    private boolean hasInternet;
    private boolean mPositionFlag = true;
    private int mPosition;
    private ArrayList<MusicModel> mMusicModels = new ArrayList<>();
    private SQLiteHelper mSQLiteHelper;

    MainPresenter(SystemServiceManager systemServiceManager, MusicService service, SQLiteHelper sqLiteHelper) {
        mSystemServiceManager = systemServiceManager;
        mService = service;
        mSQLiteHelper = sqLiteHelper;
    }

    @Override
    public void fragmentSwitcher() {
        ConnectivityManager manager = (ConnectivityManager) mSystemServiceManager.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            if (manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnected()) {
                hasInternet = true;
                if (isViewAttached()) mView.goToWithInternet();
            } else {
                hasInternet = false;
                if (isViewAttached()){
                    mView.goToWithoutInternet();
                    mView.toast();
                }
            }
        }
    }

    @Override
    public void showSavedInstance() {
        if (mService.isServiceStartOld() && isViewAttached()) {
            mView.onMusicPlayByInternet(mSQLiteHelper.getMusicPosition(), mSQLiteHelper.getMusicModels());
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
    public void musicMoverByInternet(int position) {
        if (mPositionFlag) {
            mPosition = position;
        }
        mService.playMusic(position, mMusicModels);
        if (mMusicModels != null && isViewAttached()) {
            mView.onMusicPlayByInternet(position, mMusicModels);
        }
    }

    @Override
    public void musicMoverByStorage(int position) {
        if (mPositionFlag) {
            mPosition = position;
        }
        mService.playMusicByStorage(position, mMusicModels);
        if (mMusicModels != null && isViewAttached()) {
            mView.onMusicPlayByStorage(position, mMusicModels);
        }
    }

    @Override
    public void onClickPrevious() {
        mPositionFlag = false;
        if (mPosition == 0) {
            mPosition = mMusicModels.size() - 1;
            musicMover();
        } else {
            mPosition--;
            musicMover();
        }
    }

    @Override
    public void onClickNext() {
        mPositionFlag = false;
        if (mPosition == mMusicModels.size() - 1) {
            mPosition = 0;
            musicMover();
        } else {
            mPosition++;
            musicMover();
        }
    }

    @Override
    public void onClickPlay() {
        if (!mService.handleMusicPlay() && isViewAttached()) {
            mView.changeButtonPlay(false);
        } else {
            if (isViewAttached()) mView.changeButtonPlay(true);
        }
    }

    private void musicMover() {
        if (hasInternet) musicMoverByInternet(mPosition);
        else musicMoverByStorage(mPosition);
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

    @Override
    public void saveOnDestroyed() {
        mSQLiteHelper.saveMusicModels(mMusicModels, mPosition);
    }

    @Override
    public void bind(MainContract.View view) {
        mView = view;
    }

    @Override
    public void unbind() {
        mView = null;
    }

    private boolean isViewAttached() {
        return mView != null;
    }
}
