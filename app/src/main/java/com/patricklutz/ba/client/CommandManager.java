package com.patricklutz.ba.client;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

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


    public CommandManager(Context context) {

        command = new Command(0,0,false);
        lastShot = 0;

        tcpClient = new TCPClient(new OnMessageReceived() {
            @Override
            public void messageReceived(String message) {
                Log.i("tcpIn", message);
            }
        });

        SensorEventListener shotDetector = new ShotDetector(context, this);

        TimerTask task = new TimerTask() {
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
}
