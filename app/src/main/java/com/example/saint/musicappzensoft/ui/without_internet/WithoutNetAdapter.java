package com.example.saint.musicappzensoft.ui.without_internet;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saint.musicappzensoft.R;
import com.example.saint.musicappzensoft.data.entity.MusicModel;

import java.util.ArrayList;

public class WithoutNetAdapter extends BaseAdapter {

    private ArrayList<MusicModel> mMusicModels;
    private Context mContext;
    private MusicModel mModel;

    public WithoutNetAdapter(Context context, ArrayList<MusicModel> musicModels) {
        mContext = context;
        mMusicModels = musicModels;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_withoutnet, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        mModel = (MusicModel) getItem(position);
        holder.mTextViewSongName.setText(mModel.getSong());
        holder.mTextViewArtists.setText(mModel.getArtists());
        if (mMusicModels.get(position).getCoverImage() == null) {
            holder.mImageViewMusic.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default_cover));
        } else {
            Drawable image = Drawable.createFromPath(mMusicModels.get(position).getCoverImage());
            holder.mImageViewMusic.setImageDrawable(image);
        }
        return convertView;
    }

    class ViewHolder {

        TextView mTextViewSongName, mTextViewArtists;
        ImageView mImageViewMusic;
        ViewHolder(View view) {
            mTextViewSongName = view.findViewById(R.id.textViewSongName);
            mTextViewArtists = view.findViewById(R.id.textViewArtists);
            mImageViewMusic = view.findViewById(R.id.imageMusic);
        }
    }
}
