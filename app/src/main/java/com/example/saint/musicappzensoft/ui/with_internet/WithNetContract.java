package com.example.saint.musicappzensoft.ui.with_internet;

import com.example.saint.musicappzensoft.data.entity.MusicModel;
import com.example.saint.musicappzensoft.ui.IProgressBar;
import com.example.saint.musicappzensoft.ui.LifeCycle;

import java.util.ArrayList;

public interface WithNetContract {

    interface View extends IProgressBar{

        void onSuccess(ArrayList<MusicModel> musicModels);

        void onError(String message);

        void toastDownloaded();
    }

    interface Presenter extends LifeCycle<View>{

        void getMusics();

        void musicIsDownloaded();
    }
}
