package com.xmu.rtsp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
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
import java.util.Timer;
import java.util.TimerTask;

import static android.view.KeyEvent.KEYCODE_BACK;
import static android.widget.Toast.LENGTH_SHORT;
import static com.xmu.rtsp.Constants.TAG;

public class RtspActivity extends Activity {
    private Button playButton;
    private VideoView videoView;
    private EditText number;
    private MediaController controller;
    private SeekBar seekBar;
    private Button upCmd;
    private Button cmdDown;
    private Button cmdForward;
    private Button cmdBack;
    private Button cmdLeft;
    private Button cmdRight;
    private Button startListener;
    private Timer timer;
    private TimerTask task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket("192.168.43.1", 5038);
                    Integer[] cmds = new Integer[7];
                    cmds[0] = Configuration.getThrottleStatus(RtspActivity.this);
                    cmds[1] = Configuration.getPositionForwardStatus(RtspActivity.this) ? 1 : 0;
                    cmds[2] = Configuration.getPositionBackStatus(RtspActivity.this) ? 1 : 0;
                    cmds[3] = Configuration.getPositionLeftStatus(RtspActivity.this) ? 1 : 0;
                    cmds[4] = Configuration.getPositionRightStatus(RtspActivity.this) ? 1 : 0;
                    cmds[5] = Configuration.getPositionUpStatus(RtspActivity.this) ? 1 : 0;
                    cmds[6] = Configuration.getPositionDownStatus(RtspActivity.this) ? 1 : 0;

                    String message = cmds[0] + "," + cmds[1] + "," + cmds[2] + "," + cmds[3] + "," + cmds[4] + "," + cmds[5] + "," + cmds[6];
                    BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    PrintWriter out = new PrintWriter(wr, true);
                    Log.i("lixiaolu", "Socket Output :" + out);
                    out.println(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        timer.schedule(task, 50, 50);
        initView();
        initListener();
    }

    private void initView() {
        number = (EditText) this.findViewById(R.id.url);
        playButton = (Button) this.findViewById(R.id.start_play);
        seekBar = (SeekBar) findViewById(R.id.progressBar);

        seekBar.setMax(100);
        upCmd = (Button) findViewById(R.id.cmdUp);
        cmdDown = (Button) findViewById(R.id.cmd_down);
        cmdForward = (Button) findViewById(R.id.cmd_forward);
        cmdBack = (Button) findViewById(R.id.cmd_back);
        cmdLeft = (Button) findViewById(R.id.cmd_left);
        cmdRight = (Button) findViewById(R.id.cmd_right);
        startListener = (Button) findViewById(R.id.start_listener);

        videoView = (VideoView) this.findViewById(R.id.rtsp_player);
        videoView.setFocusable(false);
        controller = new MediaController(this);
        videoView.setMediaController(controller);
    }

    private void initListener() {
        startListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        upCmd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        Configuration.keepPositionUpCmd(RtspActivity.this, true);
                        Log.i(Constants.TAG, "Up is pressed!");
                        break;

                    case MotionEvent.ACTION_UP:
                        Configuration.keepPositionUpCmd(RtspActivity.this, false);
                        Log.i(Constants.TAG, "Up is not pressed!");
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        Configuration.keepPositionUpCmd(RtspActivity.this, false);
                        Log.i(Constants.TAG, "Up is not pressed!");
                        break;
                }
                return false;
            }
        });

        cmdDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        Configuration.keepPositionDownCmd(RtspActivity.this, true);
                        Log.i(Constants.TAG, "down is pressed!");
                        break;

                    case MotionEvent.ACTION_UP:
                        Configuration.keepPositionDownCmd(RtspActivity.this, false);
                        Log.i(Constants.TAG, "down is not pressed!");
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        Configuration.keepPositionDownCmd(RtspActivity.this, false);
                        Log.i(Constants.TAG, "down is not pressed!");
                        break;
                }
                return false;
            }
        });

        cmdForward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        Configuration.keepPositionForwardCmd(RtspActivity.this, true);
                        Log.i(Constants.TAG, "Forward is pressed!");
                        break;

                    case MotionEvent.ACTION_UP:
                        Configuration.keepPositionForwardCmd(RtspActivity.this, false);
                        Log.i(Constants.TAG, "Forward is not pressed!");
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        Configuration.keepPositionForwardCmd(RtspActivity.this, false);
                        Log.i(Constants.TAG, "Forward is not pressed!");
                        break;
                }
                return false;
            }
        });

        cmdBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        Configuration.keepPositionBackCmd(RtspActivity.this, true);
                        Log.i(Constants.TAG, "BACK is pressed!");
                        break;

                    case MotionEvent.ACTION_UP:
                        Configuration.keepPositionBackCmd(RtspActivity.this, false);
                        Log.i(Constants.TAG, "BACK is not pressed!");
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        Configuration.keepPositionBackCmd(RtspActivity.this, false);
                        Log.i(Constants.TAG, "BACK is not pressed!");
                        break;
                }
                return false;
            }
        });

        cmdLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        Configuration.keepPositionLeftCmd(RtspActivity.this, true);
                        Log.i(Constants.TAG, "LEFT is pressed!");
                        break;

                    case MotionEvent.ACTION_UP:
                        Configuration.keepPositionLeftCmd(RtspActivity.this, false);
                        Log.i(Constants.TAG, "LEFT is not pressed!");
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        Configuration.keepPositionUpCmd(RtspActivity.this, false);
                        Log.i(Constants.TAG, "LEFT is not pressed!");
                        break;
                }
                return false;
            }
        });

        cmdRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        Configuration.keepPositionRightCmd(RtspActivity.this, true);
                        Log.i(Constants.TAG, "RIGHT is pressed!");
                        break;

                    case MotionEvent.ACTION_UP:
                        Configuration.keepPositionRightCmd(RtspActivity.this, false);
                        Log.i(Constants.TAG, "RIGHT is not pressed!");
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        Configuration.keepPositionRightCmd(RtspActivity.this, false);
                        Log.i(Constants.TAG, "RIGHT is not pressed!");
                        break;
                }
                return false;
            }
        });

        playButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String rtspUrl = number.getText().toString();
                PlayRtspStream(rtspUrl);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.i(TAG, "SeekBar :" + seekBar.getProgress() + "  i : " + i);
                Configuration.keepThrottleStatus(RtspActivity.this, seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(), "当前油门值为：" + seekBar.getProgress(), LENGTH_SHORT).show();
                Configuration.keepThrottleStatus(RtspActivity.this, seekBar.getProgress());
            }
        });
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

    private String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

//    private class SocketTask extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            try {
//                Socket socket = new Socket("192.168.43.1", 5038);
//                String message = "test +" + Configuration.getThrottleStatus(RtspActivity.this);
//                BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//                PrintWriter out = new PrintWriter(wr, true);
//                Log.i("lixiaolu", "Socket Output :" + out);
//                out.println(message);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }
}