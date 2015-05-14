package com.patricklutz.ba.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MenuActivity extends Activity {

    public final static String EXTRA_CHANNEL = "com.patricklutz.ba.client.EXTRA_CHANNEL";
    public final static int EXTRA_WLAN = 1;
    public final static int EXTRA_BLUETOOTH = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }


    public void connectViaWlan(View view) {

        Channel tcpChannel = new TCPClient(new Handler() {
            public void handleMessage(Message msg) {

                String message = "";
                switch (msg.arg1) {
                    case Channel.STATE_CONNECTED:
                        message = "Connected to Server!";
                        break;
                    case Channel.STATE_DISCONNECTED:
                        message = "Connection refused!";
                }

                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_CHANNEL, EXTRA_WLAN);
        startActivity(intent);
    }

    public void connectViaBluetooth(View view) {

    }

}
