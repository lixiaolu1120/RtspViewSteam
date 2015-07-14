package com.xmu.rtsp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.KeyEvent.KEYCODE_BACK;
import static com.xmu.rtsp.Configuration.getForwardBackStatus;
import static com.xmu.rtsp.Configuration.getLeftRightStatus;
import static com.xmu.rtsp.Configuration.getThrottleStatus;
import static com.xmu.rtsp.Configuration.getTowardLeftRightStatus;
import static com.xmu.rtsp.Configuration.keepForwardBackStatus;
import static com.xmu.rtsp.Configuration.keepLeftRightStatus;
import static com.xmu.rtsp.Configuration.keepTowardLeftRightStatus;
import static com.xmu.rtsp.Constants.TAG;

public class RtspActivity extends Activity {
    private Button playButton;
    private VideoView videoView;
    private MediaController controller;
    private SeekBar throttleBar;
    private Timer timer;
    private TimerTask task;
    private SeekBar leftRightBar;
    private SeekBar forwardBackBar;
    private SeekBar towardLeftRightBar;
    private TextView throttle;
    private TextView leftRight;
    private TextView forwardBack;
    private TextView towardLeftRight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        timer = new Timer();
        initTask();
        timer.schedule(task, 50, 100);

        initView();
        initListener();
    }

    private void initView() {
        playButton = (Button) this.findViewById(R.id.start_play);

        throttle = (TextView) findViewById(R.id.throttle_text);
        throttleBar = (SeekBar) findViewById(R.id.throttle_bar);
        throttleBar.setProgress(50);

        leftRight = (TextView) findViewById(R.id.left_right_text);
        leftRightBar = (SeekBar) findViewById(R.id.left_right_bar);
        leftRightBar.setProgress(50);

        forwardBack = (TextView) findViewById(R.id.forward_back_text);
        forwardBackBar = (SeekBar) findViewById(R.id.forward_back_bar);
        forwardBackBar.setProgress(50);

        towardLeftRight = (TextView) findViewById(R.id.toward_Left_right_text);
        towardLeftRightBar = (SeekBar) findViewById(R.id.toward_Left_right_bar);
        towardLeftRightBar.setProgress(50);

        videoView = (VideoView) this.findViewById(R.id.rtsp_player);
        videoView.setFocusable(false);
        controller = new MediaController(this);
        videoView.setMediaController(controller);
    }

    private void initListener() {
        playButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Log.i(TAG, "press Play BT");
                PlayRtspStream("rtsp://192.168.43.1:8086");
            }
        });

        throttleBar.setOnSeekBarChangeListener(new LocalSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int position, boolean b) {
                super.onProgressChanged(seekBar, position, b);
                Log.i(TAG, "SeekBar :" + position);
                throttle.setText("油门 ：" + (position - 50));
                Configuration.keepThrottleStatus(RtspActivity.this, position);
            }
        });

        leftRightBar.setOnSeekBarChangeListener(new LocalSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int position, boolean b) {
                super.onProgressChanged(seekBar, position, b);
                if (position == 50) {
                    leftRight.setText("水平：0");
                    return;
                }
                leftRight.setText("水平：" + ((position - 50) > 0 ? "右" : "左") + ((position - 50) > 0 ? position - 50 : 50 - position));
                Configuration.keepLeftRightStatus(RtspActivity.this, position);
            }
        });

        forwardBackBar.setOnSeekBarChangeListener(new LocalSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int position, boolean b) {
                super.onProgressChanged(seekBar, position, b);
                if (position == 50) {
                    forwardBack.setText("前后：0");
                    return;
                }

                forwardBack.setText("前后：" + ((position - 50) > 0 ? "前" : "后") + ((position - 50) > 0 ? position - 50 : 50 - position));
                Configuration.keepForwardBackStatus(RtspActivity.this, position);
            }
        });

        towardLeftRightBar.setOnSeekBarChangeListener(new LocalSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int position, boolean b) {
                super.onProgressChanged(seekBar, position, b);
                if (position == 50) {
                    towardLeftRight.setText("自转：0");
                    return;
                }
                towardLeftRight.setText("自转：" + ((position - 50) > 0 ? "右" : "左") + ((position - 50) > 0 ? position - 50 : 50 - position));
                Configuration.keepTowardLeftRightStatus(RtspActivity.this, position);
            }
        });
    }

    @Override
    protected void onDestroy() {
        videoView.pause();
        Configuration.keepThrottleStatus(RtspActivity.this, 50);
        keepForwardBackStatus(RtspActivity.this, 50);
        keepLeftRightStatus(RtspActivity.this, 50);
        keepTowardLeftRightStatus(RtspActivity.this, 50);
        timer.cancel();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KEYCODE_BACK) {
            System.out.println("stop rtsp");
            videoView.pause();
            this.finish();
        }
        return true;
    }

    private void PlayRtspStream(String rtspUrl) {
        videoView.setVideoURI(Uri.parse(rtspUrl));
        videoView.requestFocus();
        videoView.start();
    }

    private void initTask() {
        task = new TimerTask() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket("192.168.43.1", 5038);
                    String message = getLeftRightStatus(RtspActivity.this) + "," + getThrottleStatus(RtspActivity.this) + "," + getForwardBackStatus(RtspActivity.this) + "," + getTowardLeftRightStatus(RtspActivity.this);
                    Log.i("lixiaolu", message);
                    BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    PrintWriter out = new PrintWriter(wr, true);
                    out.println(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}