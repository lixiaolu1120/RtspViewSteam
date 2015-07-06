package com.xmu.rtsp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

public class RtspActivity extends Activity {
    private Button playButton;
    private VideoView videoView;
    private EditText number;
    private MediaController controller;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        number = (EditText) this.findViewById(R.id.url);
        playButton = (Button) this.findViewById(R.id.start_play);
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        videoView = (VideoView) this.findViewById(R.id.rtsp_player);
        controller = new MediaController(this);
        videoView.setMediaController(controller);

        playButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String rtspUrl = number.getText().toString();
                PlayRtspStream(rtspUrl);
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.out.println("stop rtsp");
            videoView.pause();
            this.finish();
        }
        return true;
    }

    private void PlayRtspStream(String rtspUrl) {
        Log.i("RTSP", "start play!");
        videoView.setVideoURI(Uri.parse(rtspUrl));
        Log.i("RTSP", "parse url");
        videoView.requestFocus();
        Log.i("RTSP", "video start!");
        videoView.start();
    }
}