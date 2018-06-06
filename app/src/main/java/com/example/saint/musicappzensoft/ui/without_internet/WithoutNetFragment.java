package com.example.saint.musicappzensoft.ui.without_internet;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.saint.musicappzensoft.R;
import com.example.saint.musicappzensoft.data.entity.MusicModel;
import com.example.saint.musicappzensoft.data.manager.PermissionManager;
import com.example.saint.musicappzensoft.ui.BaseFragment;
import com.example.saint.musicappzensoft.ui.main.MainCallBack;

import java.util.ArrayList;

public class WithoutNetFragment extends BaseFragment implements WithoutNetContract.View, AdapterView.OnItemClickListener {

    private Context mContext;
    private ListView mListViewMusics;
    private WithoutNetPresenter mPresenter;
    private WithoutNetAdapter mAdapter;
    private MainCallBack mCallBack;

    @Override
    protected int getViewLayout() {
        return R.layout.fragment_withoutnet;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            mContext = getActivity().getApplicationContext();
        }
        mListViewMusics = view.findViewById(R.id.listViewMusics);
        mPresenter = new WithoutNetPresenter(new PermissionManager(mContext, getActivity()));
        mPresenter.bind(this);
        mPresenter.getMusics();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPresenter.onPermissionResult(requestCode, grantResults);
    }

    @Override
    public void onSuccess(ArrayList<MusicModel> musicModels) {
        mAdapter = new WithoutNetAdapter(mContext, musicModels);
        mListViewMusics.setAdapter(mAdapter);
        mCallBack.getMusicsData(musicModels);
        mListViewMusics.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCallBack.onMusicItemClicked(position, false);
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
