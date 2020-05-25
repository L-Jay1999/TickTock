package com.example.ticktock.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("api/invoke/video/invoke/video")
    Call<List<VideoResponse>> getVideoInfos();
}
