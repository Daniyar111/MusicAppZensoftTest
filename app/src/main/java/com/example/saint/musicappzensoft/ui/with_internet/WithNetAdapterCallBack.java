package com.example.saint.musicappzensoft.ui.with_internet;

import com.example.saint.musicappzensoft.data.entity.MusicModel;

import java.util.ArrayList;

public interface WithNetAdapterCallBack {

    void onDownloadClick(int position, ArrayList<MusicModel> musicModels, WithNetAdapter.ViewHolder holder);
}
