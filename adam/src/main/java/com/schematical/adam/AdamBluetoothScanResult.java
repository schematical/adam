package com.schematical.adam;

import android.bluetooth.BluetoothDevice;

/**
 * Created by user1a on 10/3/13.
 */
public class AdamBluetoothScanResult {
    public BluetoothDevice device;
    public String name;
    public int rssi;

    AdamBluetoothScanResult(BluetoothDevice nDevice, String nName, int nRssi){
        device = nDevice;
        name = nName;
        rssi = nRssi;
    }


}
