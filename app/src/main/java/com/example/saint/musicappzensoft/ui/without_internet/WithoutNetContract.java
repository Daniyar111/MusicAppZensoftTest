package com.example.saint.musicappzensoft.ui.without_internet;

import com.example.saint.musicappzensoft.data.entity.MusicModel;
import com.example.saint.musicappzensoft.ui.LifeCycle;

import java.util.ArrayList;

public interface WithoutNetContract {

    interface View{

        void onSuccess(ArrayList<MusicModel> arrayList);
    }

    interface Presenter extends LifeCycle<View>{

        void getMusics();

        void onPermissionResult(int requestCode, int[] grantResults);
    }
}
