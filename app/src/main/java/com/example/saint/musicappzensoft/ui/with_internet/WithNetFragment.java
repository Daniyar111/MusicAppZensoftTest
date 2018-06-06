package com.example.saint.musicappzensoft.ui.with_internet;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.saint.musicappzensoft.MusicApplication;
import com.example.saint.musicappzensoft.R;
import com.example.saint.musicappzensoft.data.entity.MusicModel;
import com.example.saint.musicappzensoft.data.manager.RetrofitServiceManager;
import com.example.saint.musicappzensoft.ui.BaseFragment;
import com.example.saint.musicappzensoft.ui.main.MainCallBack;
import com.example.saint.musicappzensoft.utils.AndroidUtils;

import java.util.ArrayList;

public class WithNetFragment extends BaseFragment implements WithNetContract.View, AdapterView.OnItemClickListener, WithNetAdapterCallBack {

    private Context mContext;
    private ListView mListViewMusics;
    private ProgressBar mProgressBarMain;
    private WithNetPresenter mPresenter;
    private WithNetAdapter mAdapter;
    private MainCallBack mCallBack;

    @Override
    protected int getViewLayout() {
        return R.layout.fragment_withnet;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            mContext = getActivity().getApplicationContext();
        }
        mListViewMusics = view.findViewById(R.id.listViewMusics);
        mProgressBarMain = view.findViewById(R.id.progressBarMain);

        mPresenter = new WithNetPresenter(new RetrofitServiceManager(mContext));
        mPresenter.bind(this);

        mPresenter.getMusics();
    }

    @Override
    public void onSuccess(ArrayList<MusicModel> musicModels) {
        mAdapter = new WithNetAdapter(mContext, musicModels, WithNetFragment.this, MusicApplication.get(mContext).getSQLiteHelper());
        mListViewMusics.setAdapter(mAdapter);
        mListViewMusics.setOnItemClickListener(this);
        mCallBack.getMusicsData(musicModels);
    }

    @Override
    public void onError(String message) {
        AndroidUtils.showToast(mContext, message);
    }

    @Override
    public void toastDownloaded() {
        AndroidUtils.showToast(mContext, mContext.getString(R.string.music_is_downloaded));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCallBack.onMusicItemClicked(position, true);
    }

    @Override
    public void onDownloadClick(int position, ArrayList<MusicModel> musicModels, WithNetAdapter.ViewHolder holder) {
        new WithNetPresenter.DownloadFileFromURL(holder, musicModels.get(position),
                MusicApplication.get(mContext).getSQLiteHelper(), mPresenter)
                .execute(musicModels.get(position).getUrl());
    }

    @Override
    public void showLoadingIndicator() {
        mProgressBarMain.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingIndicator() {
        mProgressBarMain.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallBack = (MainCallBack) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement MainCallBack");
        }
    }
}
