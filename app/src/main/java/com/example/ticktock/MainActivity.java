package com.example.ticktock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.ticktock.network.ApiService;
import com.example.ticktock.network.NetworkUtils;
import com.example.ticktock.network.VideoResponse;
import com.example.ticktock.view.VideoInfoListAdapter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private LottieAnimationView animationView;
    private ViewPager2 viewPager2;
    private VideoInfoListAdapter videoInfoListAdapter;
    private boolean data_not_loading = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.viewpager);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        videoInfoListAdapter = new VideoInfoListAdapter();
        viewPager2.setAdapter(videoInfoListAdapter);
        animationView = findViewById(R.id.animation_view);
        animationView.bringToFront();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(data_not_loading){
            if(!NetworkUtils.isNetWorkAvailable(this)){
                Log.d("onStart", "start working");
                Toast.makeText(MainActivity.this, "无网络连接，请检查设置", Toast.LENGTH_SHORT).show();
                onStop();
            }
            else{
                getData();
            }
        }
    }

    public void getData() {
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .addInterceptor(new Retry(3))
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://beiyou.bytedance.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
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
                    animationView.pauseAnimation();
                    animationView.setVisibility(View.INVISIBLE);
                    data_not_loading = false;
                }
            }

            @Override
            public void onFailure(Call<List<VideoResponse>> call, Throwable t) {
                Log.d("retrofit", t.getMessage());
                Toast.makeText(MainActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static class Retry implements Interceptor {
        public int maxRetry;//最大重试次数
        private int retryNum = 0;//假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）
        public Retry(int maxRetry) {
            this.maxRetry = maxRetry;
        }
        @Override
        public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();
            okhttp3.Response response = chain.proceed(request);
            Log.i("Retry","num:"+retryNum);
            while (!response.isSuccessful() && retryNum < maxRetry) {
                retryNum++;
                Log.i("Retry","num:"+retryNum);
                response = chain.proceed(request);
            }
            return response;
        }
    }

}
