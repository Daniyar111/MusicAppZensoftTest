package com.example.saint.musicappzensoft.ui.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saint.musicappzensoft.MusicApplication;
import com.example.saint.musicappzensoft.R;
import com.example.saint.musicappzensoft.data.entity.MusicModel;
import com.example.saint.musicappzensoft.data.manager.SystemServiceManager;
import com.example.saint.musicappzensoft.services.MusicService;
import com.example.saint.musicappzensoft.ui.BaseActivity;
import com.example.saint.musicappzensoft.ui.with_internet.WithNetFragment;
import com.example.saint.musicappzensoft.ui.without_internet.WithoutNetFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements MainContract.View, MainCallBack, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private MainPresenter mPresenter;
    private SlidingUpPanelLayout mSlidingUpPanelLayout;
    private ImageView mImageViewPanelMusic;
    private TextView mTextViewSongName, mTextViewArtists;
    private Button mButtonPrevious, mButtonPlay, mButtonNext;
    private SeekBar mSeekBarVolume;
    private Intent intentService;

    @Override
    protected int getViewLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getViewLayout());
        getToolbar(getResources().getString(R.string.music_player));

        mSlidingUpPanelLayout = findViewById(R.id.playerPanel);
        mImageViewPanelMusic = findViewById(R.id.imagePanelMusic);
        mTextViewSongName = findViewById(R.id.textViewSongName);
        mTextViewArtists = findViewById(R.id.textViewArtists);
        mButtonPrevious = findViewById(R.id.buttonPrevious);
        mButtonPlay = findViewById(R.id.buttonPlay);
        mButtonNext = findViewById(R.id.buttonNext);
        mSeekBarVolume = findViewById(R.id.volumeBar);

        mPresenter = new MainPresenter(new SystemServiceManager(this), new MusicService(), MusicApplication.get(this).getSQLiteHelper());
        mPresenter.bind(this);

        mPresenter.fragmentSwitcher();

        mButtonPrevious.setOnClickListener(this);
        mButtonPlay.setOnClickListener(this);
        mButtonNext.setOnClickListener(this);
        mSeekBarVolume.setOnSeekBarChangeListener(this);
        intentService = new Intent(this, MusicService.class);
        startService(intentService);
        bindService(intentService, mPresenter.getServiceConnection(), Context.BIND_AUTO_CREATE);
//        if(mPresenter.isMusicPlaying()){
//            mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
//            mButtonPlay.setBackgroundResource(R.drawable.ic_pause_black_24dp);
//            mPresenter.savedMusicMover();
//        }

    }

    @Override
    public void goToWithInternet() {
        switchFragment(new WithNetFragment());
    }

    @Override
    public void goToWithoutInternet() {
        switchFragment(new WithoutNetFragment());
    }

    @Override
    public void onMusicPlay(int position, ArrayList<MusicModel> musicModels) {
        Picasso.get().load(musicModels.get(position).getCoverImage()).into(mImageViewPanelMusic);
        mTextViewSongName.setText(musicModels.get(position).getSong());
        mTextViewArtists.setText(musicModels.get(position).getArtists());
    }

    @Override
    public void changeButtonPlay(boolean isPlaying) {
        if(isPlaying){
            mButtonPlay.setBackgroundResource(R.drawable.ic_pause_black_24dp);
        }
        else{
            mButtonPlay.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
        }
    }

    @Override
    public void onMusicItemClicked(int position) {
        mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        mButtonPlay.setBackgroundResource(R.drawable.ic_pause_black_24dp);
        mPresenter.musicMover(position);
    }

    @Override
    public void getMusicsData(ArrayList<MusicModel> musicModels) {
        mPresenter.getMusicList(musicModels);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonPrevious:
                mPresenter.onClickPrevious();
                break;
            case R.id.buttonPlay:
                mPresenter.onClickPlay();
                break;
            case R.id.buttonNext:
                mPresenter.onClickNext();
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mPresenter.setVolume(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unbind();
        if(mPresenter.isServiceBound()){
            unbindService(mPresenter.getConnection());
        }
    }
}
