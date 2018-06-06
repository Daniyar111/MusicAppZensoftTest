package com.example.saint.musicappzensoft.ui.main;

import com.example.saint.musicappzensoft.data.entity.MusicModel;

import java.util.ArrayList;

public interface MainCallBack {

    void onMusicItemClicked(int position, boolean hasInternet);

    void getMusicsData(ArrayList<MusicModel> musicModels);
}
