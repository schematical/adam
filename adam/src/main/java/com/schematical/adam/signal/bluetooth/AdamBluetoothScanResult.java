package com.schematical.adam.signal.bluetooth;

import android.bluetooth.BluetoothDevice;

import com.schematical.adam.signal.AdamScanResultBase;

/**
 * Created by user1a on 10/3/13.
 */
public class AdamBluetoothScanResult extends AdamScanResultBase {
    public BluetoothDevice device;
    protected String type = "bluetooth";

    AdamBluetoothScanResult(BluetoothDevice nDevice, int nRssi){
        device = nDevice;
        alias = device.getName();
        id = device.getAddress();
        rssi = nRssi;
        extra = device.toString() + "  --- " +device.getBluetoothClass().toString();


    }
    //TODO: Add to JSON method


}
