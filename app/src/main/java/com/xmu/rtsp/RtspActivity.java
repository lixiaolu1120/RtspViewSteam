package com.xmu.rtsp;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import static android.widget.Toast.LENGTH_SHORT;

public class RtspActivity extends Activity {
    private Button playButton;
    private VideoView videoView;
    private EditText number;
    private MediaController controller;
    private SeekBar seekBar;
    private Button cmdSend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        number = (EditText) this.findViewById(R.id.url);
        playButton = (Button) this.findViewById(R.id.start_play);
        seekBar = (SeekBar) findViewById(R.id.progressBar);

        seekBar.setMax(100);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(), "当前油门值为：" + seekBar.getProgress(), LENGTH_SHORT).show();

            }
        });


        cmdSend = (Button) findViewById(R.id.cmdSend);

        videoView = (VideoView) this.findViewById(R.id.rtsp_player);
        videoView.setFocusable(false);
        controller = new MediaController(this);
        videoView.setMediaController(controller);

        cmdSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SocketTask task = new SocketTask();
                task.execute();
            }
        });

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

    private String intToIp(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    private class SocketTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            try {
                Socket socket = new Socket("192.168.43.1", 5038);
                String message = "test";
                BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                PrintWriter out = new PrintWriter(wr, true);
                Log.i("lixiaolu", "Socket Output :" + out);
                out.println(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}