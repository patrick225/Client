package com.patricklutz.ba.client;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.io.File;


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

        Log.i("file", dir.getAbsolutePath() + "27918-affe-zeigt-stinkefinger.jpg");
        imgView.setImageBitmap(BitmapFactory.decodeFile(dir.getAbsolutePath() + "/27918-affe-zeigt-stinkefinger.jpg"));

        cmdManager = new CommandManager(this);
        cmdManager.start();

        leftSeekbar = (SeekbarVertical) findViewById(R.id.seekBarLeft);
        rightSeekbar = (SeekbarVertical) findViewById(R.id.seekBarRight);
        leftSeekbar.setOnSeekBarChangeListener(seekBarChangeListener);
        rightSeekbar.setOnSeekBarChangeListener(seekBarChangeListener);

        leftSeekbar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar));
        rightSeekbar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar));
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

}


