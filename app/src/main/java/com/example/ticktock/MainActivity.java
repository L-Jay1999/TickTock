package com.example.ticktock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.widget.VideoView;

import com.example.ticktock.network.ApiService;
import com.example.ticktock.network.VideoResponse;
import com.example.ticktock.view.VideoInfoListAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

//    private List<VideoResponse> videoInfoList;
    private ViewPager2 viewPager2;
    private VideoInfoListAdapter videoInfoListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
//        videoInfoList = new ArrayList<>();

        viewPager2 = findViewById(R.id.viewpager);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        videoInfoListAdapter = new VideoInfoListAdapter();
        viewPager2.setAdapter(videoInfoListAdapter);
        getData();
//        viewPager2.setAdapter(videoInfoListAdapter);
    }

    public void getData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://beiyou.bytedance.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        apiService.getVideoInfos().enqueue(new Callback<List<VideoResponse>>() {
            @Override
            public void onResponse(Call<List<VideoResponse>> call, Response<List<VideoResponse>> response) {
                if(response.body() != null) {
                    videoInfoListAdapter.setData(response.body());
                    videoInfoListAdapter.notifyDataSetChanged();
                    for(int i=0;i<response.body().size();i++){
                        Log.d("retrofit", response.body().get(i).toString());
                    }
//                    Log.d("retrofit", response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<VideoResponse>> call, Throwable t) {
                Log.d("retrofit", t.getMessage());
            }
        });
    }
}
