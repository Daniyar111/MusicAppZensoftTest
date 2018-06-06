package com.example.saint.musicappzensoft.ui.main;

import android.content.ServiceConnection;

import com.example.saint.musicappzensoft.data.entity.MusicModel;
import com.example.saint.musicappzensoft.ui.LifeCycle;

import java.util.ArrayList;

public interface MainContract {

    interface View{

        void toast();

        void goToWithInternet();

        void goToWithoutInternet();

        void onMusicPlayByInternet(int position, ArrayList<MusicModel> musicModels);

        void onMusicPlayByStorage(int position, ArrayList<MusicModel> musicModels);

        void changeButtonPlay(boolean isPlaying);
    }

    interface Presenter extends LifeCycle<View>{

        void fragmentSwitcher();

        void showSavedInstance();

        ServiceConnection getServiceConnection();

        void getMusicList(ArrayList<MusicModel> musicModels);

        void musicMoverByInternet(int position);

        void musicMoverByStorage(int position);

        void onClickPrevious();

        void onClickNext();

        void onClickPlay();

        void setVolume(int progress);

        ServiceConnection getConnection();

        boolean isServiceBound();

        void saveOnDestroyed();
    }
}
