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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
    private Channel channel;
    private Context mainContext;


    public CommandManager(Context context, Channel channel) {
        mainContext = context;
        this.channel = channel;

        command = new Command(0,0,false);
        lastShot = 0;
        SensorEventListener shotDetector = new ShotDetector(context, this);

    }

    public void start() {
        Handler handler = channel.getHandler();
        channel = channel.getInstance(handler);
        channel.open();
        timer = new Timer("commandTimer");
        timer.schedule(new DoFrequently(), 0, PERIOD);
    }

    public void stop() {
        timer.cancel();
        channel.close();
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



    private class DoFrequently extends TimerTask {

        @Override
        public void run() {

            // want to shoot but last shot was recently
            if (command.isShot() &&
                    (System.currentTimeMillis() - lastShot) < SHOTDELAY)
                command.setShot(false);

            if (command.isShot())
                lastShot = System.currentTimeMillis();

            channel.send(command.getCommandData());
        }
    }

}
