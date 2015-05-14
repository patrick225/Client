package com.patricklutz.ba.client;

import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by privat-patrick on 14.05.2015.
 */
public abstract class Channel extends Thread {

    public static final int STATE_CONNECTED = 1;
    public static final int STATE_DISCONNECTED = 2;

    protected Handler handler;
    protected int state;

    public Channel(Handler handler) {
        this.handler = handler;
    }

    protected void notifyHandler(int state) {
        Message msg = Message.obtain();
        msg.arg1 = state;
        handler.sendMessage(msg);
    }



    public abstract void run();
    public abstract boolean open();
    public abstract void close();
    public abstract boolean send(byte[] data);

}
