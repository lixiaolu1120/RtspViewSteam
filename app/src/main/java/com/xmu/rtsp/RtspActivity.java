package com.xmu.rtsp;

import android.app.Activity;
import android.content.Intent;
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
import static com.xmu.rtsp.Configuration.keepThrottleStatus;
import static com.xmu.rtsp.Configuration.keepTowardLeftRightStatus;
import static com.xmu.rtsp.Constants.TAG;

public class RtspActivity extends Activity {
    private Button startPlay;
    private VideoView videoView;
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
    private Button flyStop;
    private Button flyStart;
    private Button reset;
    private Button leftRightReduce;
    private Button leftRightAdd;
    private Button throttleReduce;
    private Button throttleAdd;
    private Button forwardReduce;
    private Button forwardAdd;
    private Button towardLeftRightReduce;
    private Button towardLeftRightAdd;
    private TextView tipView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Intent intent = new Intent(RtspActivity.this, ClientReceiver.class);
        startService(intent);

        initView();

        reset();

        initListener();
    }

    private void initView() {
        startPlay = (Button) this.findViewById(R.id.start_play);

        flyStart = (Button) findViewById(R.id.fly_start);
        flyStop = (Button) findViewById(R.id.fly_stop);

        reset = (Button) findViewById(R.id.reset);

        leftRight = (TextView) findViewById(R.id.left_right_text);
        leftRightBar = (SeekBar) findViewById(R.id.left_right_bar);
        leftRightBar.setProgress(Configuration.getLeftRightStatus(RtspActivity.this));
        leftRightReduce = (Button) findViewById(R.id.left_right_subtraction);
        leftRightAdd = (Button) findViewById(R.id.left_right_add);

        tipView = (TextView) findViewById(R.id.tip_view);
        tipView.setVisibility(View.GONE);

        throttle = (TextView) findViewById(R.id.throttle_text);
        throttleBar = (SeekBar) findViewById(R.id.throttle_bar);
        throttleBar.setProgress(Configuration.getThrottleStatus(RtspActivity.this));
        throttleReduce = (Button) findViewById(R.id.throttle_subtraction);
        throttleAdd = (Button) findViewById(R.id.throttle_add);

        forwardBack = (TextView) findViewById(R.id.forward_back_text);
        forwardBackBar = (SeekBar) findViewById(R.id.forward_back_bar);
        forwardBackBar.setProgress(Configuration.getForwardBackStatus(RtspActivity.this));
        forwardReduce = (Button) findViewById(R.id.forward_back_subtraction);
        forwardAdd = (Button) findViewById(R.id.forward_back_add);

        towardLeftRight = (TextView) findViewById(R.id.toward_Left_right_text);
        towardLeftRightBar = (SeekBar) findViewById(R.id.toward_Left_right_bar);
        towardLeftRightBar.setProgress(Configuration.getTowardLeftRightStatus(RtspActivity.this));
        towardLeftRightReduce = (Button) findViewById(R.id.toward_Left_right_subtraction);
        towardLeftRightAdd = (Button) findViewById(R.id.toward_Left_right_add);

        videoView = (VideoView) this.findViewById(R.id.rtsp_player);
        videoView.setFocusable(false);
        MediaController controller = new MediaController(this);
        videoView.setMediaController(controller);
    }

    private void initListener() {
        startPlay.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Log.i(TAG, "press Play BT");
                PlayRtspStream("rtsp://192.168.43.1:8554");
            }
        });

        flyStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFly();

                if (timer != null) return;
                timer = new Timer();
                initTask();

                timer.schedule(task, 50, 30);
            }
        });

        flyStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopFly();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });

        leftRightBar.setOnSeekBarChangeListener(new LocalSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int position, boolean b) {
                super.onProgressChanged(seekBar, position, b);
                position = position + 1;
                if (position == 50) {
                    leftRight.setText("水平：0");
                    return;
                }

                leftRight.setText("水平：" + ((position - 50) > 0 ? "右" : "左") + ((position - 50) > 0 ? position - 50 : 50 - position));
                Configuration.keepLeftRightStatus(RtspActivity.this, position);
            }

        });

        leftRightReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftRightBar.setProgress(leftRightBar.getProgress() - 1);
            }
        });

        leftRightAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftRightBar.setProgress(leftRightBar.getProgress() + 1);
            }
        });

        throttleBar.setOnSeekBarChangeListener(new LocalSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int position, boolean b) {
                super.onProgressChanged(seekBar, position, b);
                position = position + 1;
                throttle.setText("油门：" + (position - 50));
                keepThrottleStatus(RtspActivity.this, position);

                tipView.setVisibility(position == 69 ? View.VISIBLE : View.GONE);
            }
        });

        throttleReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                throttleBar.setProgress(throttleBar.getProgress() - 1);
            }
        });

        throttleAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                throttleBar.setProgress(throttleBar.getProgress() + 1);
            }
        });

        forwardBackBar.setOnSeekBarChangeListener(new LocalSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int position, boolean b) {
                super.onProgressChanged(seekBar, position, b);
                position = position + 1;

                if (position == 50) {
                    forwardBack.setText("前后：0");
                    return;
                }

                forwardBack.setText("前后：" + ((position - 50) > 0 ? "前" : "后") + ((position - 50) > 0 ? position - 50 : 50 - position));
                Configuration.keepForwardBackStatus(RtspActivity.this, position);
            }

        });

        forwardReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forwardBackBar.setProgress(forwardBackBar.getProgress() - 1);
            }
        });

        forwardAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forwardBackBar.setProgress(forwardBackBar.getProgress() + 1);
            }
        });

        towardLeftRightBar.setOnSeekBarChangeListener(new LocalSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int position, boolean b) {
                super.onProgressChanged(seekBar, position, b);
                position = position + 1;

                if (position == 50) {
                    towardLeftRight.setText("自转：0");
                    return;
                }

                towardLeftRight.setText("自转：" + ((position - 50) >= 0 ? "右" : "左") + ((position - 50) > 0 ? position - 50 : 50 - position));
                Configuration.keepTowardLeftRightStatus(RtspActivity.this, position);
            }
        });

        towardLeftRightReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                towardLeftRightBar.setProgress(towardLeftRightBar.getProgress() - 1);
            }
        });

        towardLeftRightAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                towardLeftRightBar.setProgress(towardLeftRightBar.getProgress() + 1);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        videoView.pause();

        if (timer == null) return;
        timer.purge();
        timer.cancel();
        timer = null;
        super.onDestroy();
    }

    private void startFly() {
        leftRightBar.setProgress(99);
        keepLeftRightStatus(RtspActivity.this, 100);

        forwardBackBar.setProgress(0);
        keepForwardBackStatus(RtspActivity.this, 1);

        throttleBar.setProgress(0);
        keepThrottleStatus(RtspActivity.this, 1);

        towardLeftRightBar.setProgress(99);
        keepTowardLeftRightStatus(RtspActivity.this, 100);
    }

    private void stopFly() {
        leftRightBar.setProgress(0);
        keepLeftRightStatus(RtspActivity.this, 1);

        forwardBackBar.setProgress(0);
        keepForwardBackStatus(RtspActivity.this, 1);

        throttleBar.setProgress(0);
        keepThrottleStatus(RtspActivity.this, 1);

        towardLeftRightBar.setProgress(0);
        keepTowardLeftRightStatus(RtspActivity.this, 1);
    }

    private void reset() {
        leftRightBar.setProgress(72);
        keepLeftRightStatus(RtspActivity.this, 73);

        forwardBackBar.setProgress(72);
        keepForwardBackStatus(RtspActivity.this, 73);

        throttleBar.setProgress(0);
        keepThrottleStatus(RtspActivity.this, 1);

        towardLeftRightBar.setProgress(69);
        keepTowardLeftRightStatus(RtspActivity.this, 70);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KEYCODE_BACK) {
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
                    String message = getLeftRightStatus(RtspActivity.this) + "," + getForwardBackStatus(RtspActivity.this) + "," + getThrottleStatus(RtspActivity.this) + "," + getTowardLeftRightStatus(RtspActivity.this);
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