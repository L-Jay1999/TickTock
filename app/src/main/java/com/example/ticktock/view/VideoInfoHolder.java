package com.example.ticktock.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.ticktock.R;
import com.example.ticktock.network.VideoResponse;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class VideoInfoHolder extends RecyclerView.ViewHolder{
    private TextView user_id;
    private TextView nickname;
    private TextView description;
    private TextView likes;
    private ImageView avatar;
    private VideoView videoView;
    private ImageView likeImage;
    private ImageView unlikeImage;
    private Context context;
    private ImageButton playButton;

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
        playButton=itemView.findViewById(R.id.playButton);
    }
//  复原
    public void init() {

        videoView.pause();
        playButton.setImageResource(R.drawable.ic_media_play);
        avatar.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.INVISIBLE);
        setTextBlackColors();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void bind(final VideoResponse videoInfo) {
        user_id.setText(videoInfo.id);
        nickname.setText("@" + videoInfo.nickname);
        description.setText(videoInfo.description);
        likes.setText(String.valueOf(videoInfo.likecount));
        setTextBlackColors();

//        URI uri = URI.create(videoInfo.avatar);
//        Glide.with(context).clear(avatar);
        Uri uri = Uri.parse(videoInfo.avatar);
        Log.d("uri", String.valueOf(uri));
        Glide.with(context).load(uri).into(avatar);

//      设置视频链接
        videoView.setVideoURI(Uri.parse(videoInfo.feedurl));
        Runnable hide =new Runnable(){
            public void run(){
                playButton.setVisibility(View.INVISIBLE);
            }
        };
//      播放 暂停
        View.OnClickListener onclickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    playButton.setImageResource(R.drawable.ic_media_play);
                } else {
                    avatar.setVisibility(View.INVISIBLE);
                    videoView.setVisibility(View.VISIBLE);
                    playButton.setImageResource(R.drawable.ic_media_pause);
                    videoView.start();
                    setTextBlackColors();
                }
                playButton.setVisibility(View.VISIBLE);
                playButton.removeCallbacks(hide);
                playButton.postDelayed(hide, 2000);
            }
        };

        playButton.setOnClickListener(onclickListener);
//      双击
        final boolean[] DC = {false};
        Runnable doubleClick =new Runnable(){
            public void run(){
                DC[0] =false;
            }
        };

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//              隐藏 播放按钮
                playButton.setVisibility(View.VISIBLE);
                playButton.removeCallbacks(hide);
                playButton.postDelayed(hide, 2000);

                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    if (DC[0]){
                        Heart();
                        DC[0]=false;
                    }
                    else{
                        DC[0]=true;
                        videoView.postDelayed(doubleClick,200);
                    }
                }
                return true;
            }
        });
//      结束 重播
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
            }
        });

    }
//  星辰闪耀
    public void Heart(){
        unlikeImage.setVisibility(View.VISIBLE);
        likeImage.setVisibility(View.INVISIBLE);

        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(unlikeImage,
                "scaleX", 1.2f, 1f);
        scaleXAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleXAnimator.setInterpolator(new LinearInterpolator());
        scaleXAnimator.setDuration(1000);
        scaleXAnimator.setRepeatMode(ValueAnimator.REVERSE);

        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(unlikeImage,
                "scaleY", 1.2f, 1f);
        scaleYAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleYAnimator.setInterpolator(new LinearInterpolator());
        scaleYAnimator.setDuration(1000);
        scaleYAnimator.setRepeatMode(ValueAnimator.REVERSE);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator);
        animatorSet.start();
    }

    public void setTextWhiteColors() {
        user_id.setTextColor(Color.parseColor("#FFFFF0"));
        nickname.setTextColor(Color.parseColor("#FFFFF0"));
        description.setTextColor(Color.parseColor("#FFFFF0"));
        likes.setTextColor(Color.parseColor("#FFFFF0"));
    }

    public void setTextBlackColors() {
        user_id.setTextColor(Color.parseColor("#000000"));
        nickname.setTextColor(Color.parseColor("#000000"));
        description.setTextColor(Color.parseColor("#000000"));
        likes.setTextColor(Color.parseColor("#000000"));
    }
}
