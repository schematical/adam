package com.schematical.adam;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

/**
 * Created by user1a on 10/3/13.
 */
public class AdamBluetooth extends BroadcastReceiver{
    public static final String TYPE = "Bluetooth";
    AdamActivityMain am;
    BluetoothAdapter mBluetoothAdapter;
    Hashtable<String, AdamBluetoothScanResult> aScanResults;
    private IntentFilter discoveryFinished;
    private IntentFilter actionFoundFilter;

    AdamBluetooth(AdamActivityMain nAm){
        am = nAm;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        aScanResults = new Hashtable<String, AdamBluetoothScanResult>();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            return;
        }




    }
    public void StartDiscovery(){
        if (mBluetoothAdapter.isEnabled()) {
            // Register the BroadcastReceiver
            actionFoundFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            am.registerReceiver(this, actionFoundFilter); // Don't forget to unregister during onDestroy

            discoveryFinished = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            am.registerReceiver(this, discoveryFinished);
        }
        mBluetoothAdapter.startDiscovery();
    }
    public void UnregisterListener(){

       am.unregisterReceiver(this);
       mBluetoothAdapter.cancelDiscovery();

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        // When discovery finds a device
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            // Get the BluetoothDevice object from the Intent
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            // Add the name and address to an array adapter to show in a ListView
            //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            int  rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,(short)Integer.MIN_VALUE);

            String  name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
            String  sClass = intent.getStringExtra(BluetoothDevice.EXTRA_CLASS);
            String  sUUID = intent.getStringExtra(BluetoothDevice.EXTRA_UUID);
            String  sAddress = device.getAddress();
            AdamBluetoothScanResult as =  new AdamBluetoothScanResult(
                    device,
                    name,
                    rssi
            );
            aScanResults.put(
                sAddress,
                as
            );
            am.UpdateAdamObject(sAddress, as);
        }

    }
    public void MakeDiscoverable(){
        Intent discoverableIntent = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        am.startActivity(discoverableIntent);

    }
}
