package com.patricklutz.ba.client;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class MenuActivity extends Activity {

    private ArrayList<BluetoothDevice> devices = new ArrayList<>();

    public final static String EXTRA_CHANNEL = "com.patricklutz.ba.client.EXTRA_CHANNEL";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }


    public void connectViaWlan(View view) {
        Class activity = null;
        RadioButton steuerung1 = (RadioButton) findViewById(R.id.radioSteuerung1);
        RadioButton steuerung2 = (RadioButton) findViewById(R.id.radioSteuerung2);
        if (steuerung1.isChecked()) {
            activity = MainActivity.class;
        }
        if (steuerung2.isChecked()) {
            activity = MainActivity_Wheel.class;
        }

        Intent intent = new Intent(this, activity);
        intent.putExtra(EXTRA_CHANNEL, Channel.TYPE_WLAN);
        startActivity(intent);
    }

    public void connectViaBluetooth(View view) {

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_UUID);

        registerReceiver(bcreceiver, filter);

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();

        adapter.startDiscovery();

    }

    private final BroadcastReceiver bcreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                devices.add(device);
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())) {
                for(int i = 0; i<devices.size();i++) {
                    Log.i("bluetoothdevices", devices.get(i).getName());
                    devices.get(i).fetchUuidsWithSdp();
                }


            }
            if (BluetoothDevice.ACTION_UUID.equals(intent.getAction())) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Parcelable[] uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
                for (int i = 0; i< uuidExtra.length;i++) {
                    Log.i("bluetoothservice", uuidExtra[i].toString());
                }

                try {
                    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuidExtra[uuidExtra.length - 1].toString()));
                    new ConnectTask().execute(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("bluetootherror", e.getMessage());
                }
            }
        }
    };

    private class SendCmdTask extends AsyncTask<BluetoothSocket, Void, Void> {


        @Override
        protected Void doInBackground(BluetoothSocket... params) {
            try {
                byte[] cmd = "adsf".getBytes();

                BluetoothSocket socket = params[0];
                socket.connect();
                socket.getOutputStream().write(cmd);
                socket.getOutputStream().flush();
                socket.getOutputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class ConnectTask extends AsyncTask<BluetoothSocket, Void, BluetoothSocket> {

        @Override
        protected BluetoothSocket doInBackground(BluetoothSocket... params) {

            BluetoothSocket socket = params[0];

            try {
                socket.connect();
                new SendCmdTask().execute(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }



}
