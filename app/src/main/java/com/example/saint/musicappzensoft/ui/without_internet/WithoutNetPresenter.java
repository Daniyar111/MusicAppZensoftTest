package com.example.saint.musicappzensoft.ui.without_internet;

import android.content.pm.PackageManager;

import com.example.saint.musicappzensoft.config.AppConstants;
import com.example.saint.musicappzensoft.data.entity.MusicModel;
import com.example.saint.musicappzensoft.data.manager.PermissionManager;

import java.util.ArrayList;

public class WithoutNetPresenter implements WithoutNetContract.Presenter {

    private WithoutNetContract.View mView;
    private ArrayList<MusicModel> mMusicModels = new ArrayList<>();
    private PermissionManager mPermissionManager;

    WithoutNetPresenter(PermissionManager permissionManager) {
        mPermissionManager = permissionManager;
    }

    @Override
    public void getMusics() {
        if (!mPermissionManager.isExternalStoragePermission()) {
            mMusicModels = mPermissionManager.getExternalStorageTrack();
            if (isViewAttached()) {
                mView.onSuccess(mMusicModels);
            }
        }
    }

    @Override
    public void onPermissionResult(int requestCode, int[] grantResults) {
        switch (requestCode) {
            case AppConstants.READ_PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mPermissionManager.isReadStoragePermissionGranted()) {
                        mMusicModels = mPermissionManager.getExternalStorageTrack();
                        if (isViewAttached()) {
                            mView.onSuccess(mMusicModels);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void bind(WithoutNetContract.View view) {
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
