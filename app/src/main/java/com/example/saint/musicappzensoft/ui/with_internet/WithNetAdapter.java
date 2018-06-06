package com.example.saint.musicappzensoft.ui.with_internet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.saint.musicappzensoft.R;
import com.example.saint.musicappzensoft.data.db.SQLiteHelper;
import com.example.saint.musicappzensoft.data.entity.MusicModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class WithNetAdapter extends BaseAdapter {

    private ArrayList<MusicModel> mMusicModels;
    private Context mContext;
    private MusicModel mModel;
    private WithNetAdapterCallBack mAdapterCallBack;
    private SQLiteHelper mSQLiteHelper;
    private Set<Integer> mDownloadSet = new HashSet<>();

    WithNetAdapter(Context context, ArrayList<MusicModel> musicModels, WithNetAdapterCallBack adapterCallBack, SQLiteHelper sqLiteHelper) {
        mContext = context;
        mMusicModels = musicModels;
        mAdapterCallBack = adapterCallBack;
        mSQLiteHelper = sqLiteHelper;
    }

    @Override
    public int getCount() {
        return mMusicModels.size();
    }

    @Override
    public Object getItem(int position) {
        return mMusicModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_withnet, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        mModel = (MusicModel) getItem(position);
        holder.mTextViewSongName.setText(mModel.getSong());
        holder.mTextViewArtists.setText(mModel.getArtists());
        Picasso.get().load(mModel.getCoverImage()).into(holder.mImageViewMusic);
        imageDownloadController(holder, position);

        holder.mImageViewDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterCallBack.onDownloadClick(position, mMusicModels, holder);
            }
        });
        return convertView;
    }

    class ViewHolder {

        TextView mTextViewSongName, mTextViewArtists;
        ImageView mImageViewMusic, mImageViewDownload, mImageViewCheck;
        ProgressBar mProgressBarDownload;

        ViewHolder(View view) {
            mTextViewSongName = view.findViewById(R.id.textViewSongName);
            mTextViewArtists = view.findViewById(R.id.textViewArtists);
            mImageViewMusic = view.findViewById(R.id.imageMusic);
            mImageViewDownload = view.findViewById(R.id.imageDownload);
            mImageViewCheck = view.findViewById(R.id.imageCheck);
            mProgressBarDownload = view.findViewById(R.id.progressBarDownload);
        }
    }

    private void imageDownloadController(ViewHolder holder, final int position) {
        holder.mImageViewDownload.setOnFocusChangeListener(null);
        holder.mImageViewCheck.setOnFocusChangeListener(null);
        holder.mImageViewDownload.setVisibility(mDownloadSet.contains(position) ? View.GONE : View.VISIBLE);
        holder.mImageViewCheck.setVisibility(mDownloadSet.contains(position) ? View.VISIBLE : View.GONE);
        if (mSQLiteHelper.isDownloadedMusic(mMusicModels.get(position).getUrl())) {
            holder.mImageViewDownload.setVisibility(View.GONE);
            holder.mImageViewCheck.setVisibility(View.VISIBLE);
        }
        holder.mImageViewDownload.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mDownloadSet.add(position);
                } else {
                    mDownloadSet.remove(position);
                }
            }
        });
    }
}
