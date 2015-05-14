package com.patricklutz.ba.client;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by privat-patrick on 14.05.2015.
 */
public class CommandManager {


    private Timer timer = new Timer("commandTimer");

    // period to send commands to server (milliseconds)
    private static final long PERIOD = 100;

    // minimum time, between two shots are sent
    private static final long SHOTDELAY = 500;

    private long lastShot;

    private Command command;
    private TCPClient tcpClient;
    private Context mainContext;


    public CommandManager(Context context) {
        mainContext = context;

        command = new Command(0,0,false);
        lastShot = 0;

        tcpClient = new TCPClient(msgReceived, handler);
        SensorEventListener shotDetector = new ShotDetector(context, this);

    }

    public void start() {
        timer.schedule(task, 0, PERIOD);
    }
    public void setShot(boolean shot) {
        command.setShot(shot);
    }

    public void setVeloLeft(int velocity) {
        command.setVeloLeft(velocity);
    }

    public void setVeloRight(int velocity) {
        command.setVeloRight(velocity);
    }

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            String message = "";
            switch (msg.arg1) {
                case TCPClient.STATE_CONNECTED:
                    message = "Connected to Server!";
                    break;
                case TCPClient.STATE_DISCONNECTED:
                    message = "Connection refused!";
            }

            Toast.makeText(mainContext, message, Toast.LENGTH_LONG).show();
        }
    };

    final OnMessageReceived msgReceived = new OnMessageReceived() {
        @Override
        public void messageReceived(String message) {
            Log.i("tcpIn", message);
        }
    };

    final TimerTask task = new TimerTask() {
        @Override
        public void run() {

            // want to shoot but last shot was recently
            if (command.isShot() &&
                    (System.currentTimeMillis() - lastShot) < SHOTDELAY)
                command.setShot(false);

            if (command.isShot())
                lastShot = System.currentTimeMillis();

            tcpClient.send(command);
        }
    };


}
