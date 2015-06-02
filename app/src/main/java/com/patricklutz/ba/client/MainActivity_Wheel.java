package com.patricklutz.ba.client;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class MainActivity_Wheel extends Activity {


    CommandManager cmdManager;

    private float[] gravity = new float[3];
    private float[] magnetic = new float[3];
    private Sensor gravitySensor;
    private Sensor magneticSensor;

    private SensorManager snsMngr;
    private SeekbarVertical power;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity__wheel);

        power = (SeekbarVertical) findViewById(R.id.seekBarLeft);

        ImageView imgView = (ImageView) findViewById(R.id.imageview);

        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        imgView.setImageBitmap(BitmapFactory.decodeFile(dir.getAbsolutePath() + "/27918-affe-zeigt-stinkefinger.jpg"));

        Intent intent = getIntent();

        cmdManager = new CommandManager(this,
                getChannel(intent.getIntExtra(MenuActivity.EXTRA_CHANNEL, Channel.TYPE_WLAN)));

        snsMngr = (SensorManager) getSystemService(SENSOR_SERVICE);
        gravitySensor = snsMngr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticSensor = snsMngr.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }




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



    @Override
    protected void onResume() {
        super.onResume();
        snsMngr.registerListener(sensorListener, gravitySensor, SensorManager.SENSOR_DELAY_GAME);
        snsMngr.registerListener(sensorListener, magneticSensor, SensorManager.SENSOR_DELAY_GAME);
        cmdManager.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        snsMngr.unregisterListener(sensorListener);
        cmdManager.stop();


    }

    final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                gravity = event.values.clone();
            }
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                magnetic = event.values.clone();
            }

            setCommand();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private void setCommand() {
        float[] R1 = new float[9];
        float[] I = new float[9];
        SensorManager.getRotationMatrix(R1, I, gravity, magnetic);

        float[] values = new float[3];
        SensorManager.getOrientation(R1,values);

        for (int i = 0; i < values.length; i++) {
            Double degrees = (values[i] * 180) / Math.PI;
            values[i] = degrees.floatValue();
        }

        TextView tmp = (TextView) findViewById(R.id.tmpView);
        TextView tmp1 = (TextView) findViewById(R.id.tmpView1);
        TextView tmp2 = (TextView) findViewById(R.id.tmpView2);
        tmp.setText("" + values[0]);
        tmp1.setText("" + values[1]);
        tmp2.setText("" + values[2]);

        cmdManager.setVeloLeft(getVeloLeft(values[1]));
        cmdManager.setVeloRight(getVeloRight(values[1]));
    }

    private int getVeloLeft (double angle) {

        int veloLeft;
        double ratio = getVeloRatio(angle);
        if (angle > 0) {
                veloLeft = (int) (power.getProgress() / ratio);
        } else {
            veloLeft = power.getProgress();
        }
        return veloLeft;
    }

    private int getVeloRight (double angle) {

        int veloRight;
        double ratio = getVeloRatio(angle);

        if (angle < 0) {
            veloRight = (int) (power.getProgress() / ratio);
        } else {
            veloRight = power.getProgress();
        }
        return veloRight;
    }

    private double getVeloRatio (double angle) {

        double ratio = (angle / 60) + 1;
        return ratio;
    }

}
