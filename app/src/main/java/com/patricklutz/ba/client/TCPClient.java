package com.patricklutz.ba.client;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Class for TCP Connection to the Server
 *
 * Created by privat-patrick on 12.05.2015.
 */
public class TCPClient extends Thread {

    public static final int STATE_CONNECTED = 1;
    public static final int STATE_DISCONNECTED = 2;

    private String serverMessage;
    public static final String SERVERIP = "192.168.178.37";
    public static final int SERVERPORT = 4443;
    private OnMessageReceived messageListener = null;
    private boolean running = false;

    private int state;

    private Handler handler;

    DataOutputStream out;
    BufferedReader in;

    public TCPClient(OnMessageReceived listener, Handler handler) {
        messageListener = listener;
        this.handler = handler;
        state = STATE_DISCONNECTED;
        start();
    }
    public boolean send(Command command) {

        if (out != null && state == STATE_CONNECTED) {
            byte[] data = command.getCommandData();
            try {
                out.write(data);
                out.flush();
                return true;
            } catch (IOException e) {
                Log.e("TcpClient", e.getMessage());
                state = STATE_DISCONNECTED;
                notifyHandler(STATE_DISCONNECTED);
                return false;
            }
        }
        return false;
    }


    @Override
    public void run() {
        running = true;

        try {
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);

            Socket socket = new Socket(serverAddr, SERVERPORT);
            notifyHandler(STATE_CONNECTED);
            state = STATE_CONNECTED;

            try {

                out = new DataOutputStream(socket.getOutputStream());
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while(running) {
                    serverMessage = in.readLine();

                    if (serverMessage != null) {
                        messageListener.messageReceived(serverMessage);
                    }
                }
                out.close();
                in.close();

            } catch(IOException e) {
                notifyHandler(STATE_DISCONNECTED);
                state = STATE_DISCONNECTED;
                Log.e("TCPClient", e.getMessage());
            }
            socket.close();
        } catch (IOException e) {
            Log.e("TCPClient", e.getMessage());
        }
    }

    public void close() {
        running = false;
    }

    private void notifyHandler(int state) {
        Message msg = Message.obtain();
        msg.arg1 = state;
        handler.sendMessage(msg);
    }

}
