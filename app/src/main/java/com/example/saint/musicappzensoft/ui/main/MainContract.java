package com.example.saint.musicappzensoft.ui.main;

import android.content.ServiceConnection;

import com.example.saint.musicappzensoft.data.entity.MusicModel;
import com.example.saint.musicappzensoft.ui.LifeCycle;

import java.util.ArrayList;

public interface MainContract {

    interface View{

        void goToWithInternet();

        void goToWithoutInternet();

        void onMusicPlay(int position, ArrayList<MusicModel> musicModels);

        void changeButtonPlay(boolean isPlaying);
    }

    interface Presenter extends LifeCycle<View>{

        void fragmentSwitcher();

        ServiceConnection getServiceConnection();

        void getMusicList(ArrayList<MusicModel> musicModels);

        void musicMover(int position);

//        void savedMusicMover();

        void onClickPrevious();

        void onClickNext();

        void onClickPlay();

        void setVolume(int progress);

        ServiceConnection getConnection();

        boolean isServiceBound();

//        boolean isMusicPlaying();
    }
}
