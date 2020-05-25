package com.example.ticktock.view;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.ticktock.R;
import com.example.ticktock.network.VideoResponse;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URI;

public class VideoInfoHolder extends RecyclerView.ViewHolder {
    private TextView user_id;
    private TextView nickname;
    private TextView description;
    private TextView likes;
    private ImageView avatar;
    private VideoView videoView;
    private ImageView likeImage;
    private ImageView unlikeImage;
    private Context context;

    public VideoInfoHolder(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();

        user_id = itemView.findViewById(R.id.id);
        nickname = itemView.findViewById(R.id.nickname);
        description = itemView.findViewById(R.id.description);
        likes = itemView.findViewById(R.id.likes);
        avatar = itemView.findViewById(R.id.avatar);
        videoView = itemView.findViewById(R.id.videoView);
        likeImage = itemView.findViewById(R.id.likeImage);
        unlikeImage = itemView.findViewById(R.id.unlikeImage);
    }

    public void bind(final VideoResponse videoInfo) {
        user_id.setText(videoInfo.id);
        nickname.setText("@" + videoInfo.nickname);
        description.setText(videoInfo.description);
        likes.setText(String.valueOf(videoInfo.likecount));

//        URI uri = URI.create(videoInfo.avatar);
//        Glide.with(context).clear(avatar);
        Uri uri = Uri.parse(videoInfo.avatar);
        Log.d("uri", String.valueOf(uri));
        Glide.with(context).load(uri).into(avatar);
    }
}
