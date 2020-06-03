package com.example.ticktock.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ticktock.R;
import com.example.ticktock.network.VideoResponse;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class VideoInfoListAdapter extends RecyclerView.Adapter<VideoInfoHolder> {
    private List<VideoResponse> videoInfoList;
    public void setData(List<VideoResponse> data) { 
        videoInfoList = data; 
    }

    @NonNull
    @Override
    public VideoInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video, parent, false);
        return new VideoInfoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoInfoHolder holder, int position) {
        holder.bind(videoInfoList.get(position));
    }

    @Override
    public int getItemCount() {
        return videoInfoList == null ? 0 : videoInfoList.size();
    }

//  到
    public void onViewAttachedToWindow(@NonNull VideoInfoHolder holder) {
        super.onViewAttachedToWindow(holder);
//        Log.d("holderin", holder.toString());
        holder.init();
    }
//  离开
    public void onViewDetachedFromWindow(@NonNull VideoInfoHolder holder) {
        super.onViewDetachedFromWindow(holder);
//        Log.d("holderout", holder.toString());
        holder.init();
    }

}
