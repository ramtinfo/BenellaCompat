package com.example.ram.benellacompat;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

import com.yqritc.scalablevideoview.ScalableVideoView;

import java.io.IOException;

public class TestActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
//        mVideoView =findViewById(R.id.videoView);
//        try {
//            mVideoView.setRawData(R.raw.portrait_sample);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        mVideoView.setLooping(true);
//        mVideoView.start();
    }

}

