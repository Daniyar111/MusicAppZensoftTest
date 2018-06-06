package com.example.saint.musicappzensoft.ui.with_internet;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.example.saint.musicappzensoft.config.AppConstants;
import com.example.saint.musicappzensoft.data.db.SQLiteHelper;
import com.example.saint.musicappzensoft.data.entity.MusicModel;
import com.example.saint.musicappzensoft.data.manager.RetrofitServiceManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithNetPresenter implements WithNetContract.Presenter {

    private RetrofitServiceManager mServiceManager;
    private WithNetContract.View mView;
    private ArrayList<MusicModel> mMusicModels = new ArrayList<>();

    WithNetPresenter(RetrofitServiceManager serviceManager) {
        mServiceManager = serviceManager;
    }

    @Override
    public void getMusics() {
        if (isViewAttached()) {
            mView.showLoadingIndicator();
            mServiceManager.getMusicList().enqueue(new Callback<ArrayList<MusicModel>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<MusicModel>> call, @NonNull Response<ArrayList<MusicModel>> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        if (isViewAttached()) {
                            mMusicModels = response.body();
                            mView.hideLoadingIndicator();
                            mView.onSuccess(mMusicModels);
                        }
                    } else {
                        if (isViewAttached()) {
                            mView.hideLoadingIndicator();
                            mView.onError(response.message());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<MusicModel>> call, @NonNull Throwable t) {
                    if (isViewAttached()) {
                        mView.onError(t.getMessage());
                        mView.hideLoadingIndicator();
                    }
                }
            });
        }
    }

    @Override
    public void musicIsDownloaded() {
        if (isViewAttached()) mView.toastDownloaded();
    }

    static class DownloadFileFromURL extends AsyncTask<String, String, String> {

        private URL mURL;
        private URLConnection mConnection;
        private InputStream mInputStream;
        private OutputStream mOutputStream;
        private File mMusicFolder, mMusicFile;
        private String mMusicName;
        private byte[] mData = new byte[AppConstants.DATA_BYTE];
        private long mTotal = 0;
        private int mCount, mLengthOfFile;
        private WithNetAdapter.ViewHolder mHolder;
        private MusicModel mMusicModel;
        private SQLiteHelper mSQLiteHelper;
        private WithNetPresenter mPresenter;

        DownloadFileFromURL(WithNetAdapter.ViewHolder holder, MusicModel musicModel, SQLiteHelper sqLiteHelper, WithNetPresenter presenter) {
            mHolder = holder;
            mMusicModel = musicModel;
            mSQLiteHelper = sqLiteHelper;
            mPresenter = presenter;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mHolder.mImageViewDownload.setVisibility(View.GONE);
            mHolder.mProgressBarDownload.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... f_url) {
            try {
                mURL = new URL(expandUrl(f_url[0]));
                mConnection = mURL.openConnection();
                mConnection.connect();

                mInputStream = new BufferedInputStream(mURL.openStream(), AppConstants.INPUT_STREAM_SIZE);
                mMusicFolder = new File(AppConstants.EXTERNAL_STORAGE + AppConstants.FOLDER_NAME);
                if (!mMusicFolder.exists()) {
                    mMusicFolder.mkdir();
                }
                mLengthOfFile = mConnection.getContentLength();
                mMusicName = mMusicModel.getSong() + AppConstants.MP3_FORMAT;

                mMusicFile = new File(AppConstants.EXTERNAL_STORAGE + AppConstants.FOLDER_NAME + mMusicName);
                mOutputStream = new FileOutputStream(mMusicFile);

                while ((mCount = mInputStream.read(mData)) != -1) {
                    mTotal += mCount;
                    publishProgress("" + (int) ((mTotal * 100) / mLengthOfFile));
                    mOutputStream.write(mData, 0, mCount);
                }
                mOutputStream.flush();
                mOutputStream.close();
                mInputStream.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            mHolder.mProgressBarDownload.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            mHolder.mProgressBarDownload.setVisibility(View.GONE);
            mHolder.mImageViewCheck.setVisibility(View.VISIBLE);
            mSQLiteHelper.saveDownloadedMusic(mMusicModel.getUrl(), true);
            mPresenter.musicIsDownloaded();
        }

        private String expandUrl(String shortenedUrl) throws IOException {
            URL url = new URL(shortenedUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);
            httpURLConnection.setInstanceFollowRedirects(false);
            String expandedUrl = httpURLConnection.getHeaderField("Location");
            httpURLConnection.disconnect();
            return expandedUrl;
        }
    }

    @Override
    public void bind(WithNetContract.View view) {
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
