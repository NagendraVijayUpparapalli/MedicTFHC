package com.example.cool.patient;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;


public class tvvideomedic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvvideomedic);

        Button buttonPlayVideo2 = (Button) findViewById(R.id.button1);
        getWindow().setFormat(PixelFormat.UNKNOWN);
        VideoView mvideoview2 = (VideoView) findViewById(R.id.videoView1);
        String uriPath2 = "android.resource://com.example.cool.patient/" + R.raw.video;
        Uri uri2 = Uri.parse(uriPath2);
        mvideoview2.setVideoURI(uri2);
        mvideoview2.requestFocus();

        buttonPlayVideo2.setOnClickListener(new  Button.OnClickListener() {
            public void onClick(View v) {
                VideoView mVideoView2 = (VideoView) findViewById(R.id.videoView1);
                String uriPath = "android.resource://com.example.cool.patient/" + R.raw.video;
                Uri uri2 = Uri.parse(uriPath);
                mVideoView2.setVideoURI(uri2);
                mVideoView2.requestFocus();
                mVideoView2.start();


            }
        });


    }
}
