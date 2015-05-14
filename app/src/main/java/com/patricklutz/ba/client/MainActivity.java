package com.patricklutz.ba.client;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;


public class MainActivity extends Activity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imgView = (ImageView) findViewById(R.id.imageview);

        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        Log.i("file", dir.getAbsolutePath() + "27918-affe-zeigt-stinkefinger.jpg");
        imgView.setImageBitmap(BitmapFactory.decodeFile(dir.getAbsolutePath() + "/27918-affe-zeigt-stinkefinger.jpg"));

        SensorEventListener shotListener = new ShotDetector();

        SensorManager sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorMgr.registerListener(shotListener, sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);


    }

}


