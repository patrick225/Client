package com.patricklutz.ba.client;

import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
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
public class TCPClient extends Channel {



    private String serverMessage;
    public static final String SERVERIP = "192.168.178.37";
    public static final int SERVERPORT = 4443;
    private boolean running = false;


    private Socket socket;
    private DataOutputStream out;
    private BufferedReader in;

    public TCPClient(Handler handler) {
        super(handler, TYPE_WLAN);

        state = STATE_DISCONNECTED;
    }

    @Override
    public Channel getInstance(Handler handler) {
        return new TCPClient(handler);
    }


    @Override
    public void run() {
        running = true;

        try {
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);

            socket = new Socket(serverAddr, SERVERPORT);
            notifyHandler(STATE_CONNECTED);
            state = STATE_CONNECTED;

            try {

                out = new DataOutputStream(socket.getOutputStream());
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while(running) {
                    serverMessage = in.readLine();

                    if (serverMessage != null) {
                        //@TODO incoming messages?
                    }
                }

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

    @Override
    public boolean send(byte[] data) {

        if (out != null && state == STATE_CONNECTED) {
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
    public boolean open() {
        start();
        return true;
    }

    @Override
    public void close() {
        running = false;
        try {
            socket.close();
            out.close();
            in.close();
            state = STATE_DISCONNECTED;
        }catch (IOException e) {
            Log.e("TcpClient", e.getMessage());
        }

    }
}
