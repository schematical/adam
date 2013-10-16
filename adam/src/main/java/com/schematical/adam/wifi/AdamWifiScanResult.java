package com.schematical.adam.wifi;

import android.bluetooth.BluetoothDevice;
import android.net.wifi.ScanResult;

import com.schematical.adam.model.AdamScanResultBase;

/**
 * Created by user1a on 10/3/13.
 */
public class AdamWifiScanResult extends AdamScanResultBase {
    public ScanResult device;
    protected String type = "wifi";

    AdamWifiScanResult(ScanResult nDevice){
        device = nDevice;
        alias = device.SSID;
        id = device.BSSID;
        extra = device.capabilities;
        frequency = device.frequency;
        rssi = device.level;

    }
    //TODO: Add to JSON method


}
