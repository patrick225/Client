package com.patricklutz.ba.client;

import android.app.Activity;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;



public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        VideoView video = (VideoView) findViewById(R.id.video);
//        String path = "/storage/emulated/0/Download/AndroidCommercial.3gp";
//        path = "http://192.168.178.28:8080/asdf";
//        path = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov";
//        path = "rtsp:/192.168.178.28:8554/bunny.mov";
//        video.setVideoURI(Uri.parse(path));
//        video.start();

        LayoutInflater inflater = getLayoutInflater();
        getWindow().addContentView(inflater.inflate(R.layout.controls, null),
                                    new ViewGroup.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.MATCH_PARENT
                                    ));

    }

}
