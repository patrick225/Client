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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }


    public void connectViaWlan(View view) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_CHANNEL, Channel.TYPE_WLAN);
        startActivity(intent);
    }

    public void connectViaBluetooth(View view) {

    }

}
