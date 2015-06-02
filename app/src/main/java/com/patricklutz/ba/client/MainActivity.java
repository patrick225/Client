package com.patricklutz.ba.client;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;

/**
 * This is the first Controlltype via Seekbars for left and right motor
 *
 * @author privat-patrick
 */
public class MainActivity extends Activity {


    CommandManager cmdManager;

    SeekbarVertical leftSeekbar;
    SeekbarVertical rightSeekbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imgView = (ImageView) findViewById(R.id.imageview);

        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        imgView.setImageBitmap(BitmapFactory.decodeFile(dir.getAbsolutePath() + "/27918-affe-zeigt-stinkefinger.jpg"));

        Intent intent = getIntent();

        cmdManager = new CommandManager(this,
                getChannel(intent.getIntExtra(MenuActivity.EXTRA_CHANNEL, Channel.TYPE_WLAN)));

        leftSeekbar = (SeekbarVertical) findViewById(R.id.seekBarLeft);
        rightSeekbar = (SeekbarVertical) findViewById(R.id.seekBarRight);
        leftSeekbar.setOnSeekBarChangeListener(seekBarChangeListener);
        rightSeekbar.setOnSeekBarChangeListener(seekBarChangeListener);

        leftSeekbar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar));
        rightSeekbar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar));
    }


    /**
     * Returns a working Channel for the given Connectiontype
     *
     * @param connection type of connection
     * @return working channel
     */
    private Channel getChannel(int connection) {
        Channel channel = null;
        switch (connection) {
            case Channel.TYPE_WLAN:
                channel = new TCPClient(new Handler() {
                    public void handleMessage(Message msg) {

                        String message;
                        switch (msg.arg1) {
                            case Channel.STATE_CONNECTED:
                                message = "Connected to Server!";
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                break;
                            case Channel.STATE_DISCONNECTED:
                                message = "Connection refused!";
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(),MenuActivity.class);
                                startActivity(intent);
                                break;
                        }
                    }
                });
                break;
            case Channel.TYPE_BLUETOOTH:
                //@TODO bluetooth channel zurueckgeben
                break;

        }
        return channel;
    }


    final SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        private static final int SLIDER_ZEROPOINT = 50;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            switch (seekBar.getId()) {
                case R.id.seekBarLeft:
                    cmdManager.setVeloLeft(progress - SLIDER_ZEROPOINT);
                    break;
                case R.id.seekBarRight:
                    cmdManager.setVeloRight(progress - SLIDER_ZEROPOINT);
                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        cmdManager.start();
    }


    @Override
    protected void onStop() {
        super.onStop();
        cmdManager.stop();
    }

}


