package com.patricklutz.ba.client;

import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.zerokol.views.JoystickView;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VideoView video = (VideoView) findViewById(R.id.video);
        String path = "/storage/emulated/0/Download/AndroidCommercial.3gp";
        video.setVideoPath(path);
        video.start();

        LayoutInflater inflater = getLayoutInflater();
        getWindow().addContentView(inflater.inflate(R.layout.controls, null),
                                    new ViewGroup.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.MATCH_PARENT
                                    ));

        JoystickView joystick = (JoystickView) findViewById(R.id.joystick);

        joystick.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {
            @Override
            public void onValueChanged(int i, int i1, int i2) {

            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);
    }

}
