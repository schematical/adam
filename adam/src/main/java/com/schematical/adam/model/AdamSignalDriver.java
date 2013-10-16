package com.schematical.adam.model;

import com.schematical.adam.bluetooth.AdamBluetoothDriver;
import com.schematical.adam.bluetooth.AdamBluetoothScanResult;
import com.schematical.adam.wifi.AdamWifiDriver;

import java.util.Hashtable;

/**
 * Created by user1a on 10/16/13.
 */
public class AdamSignalDriver {
    Hashtable<String, AdamScanResultBase> aScanResults;
    AdamBluetoothDriver adamBluetoothDriver;
    AdamWifiDriver adamWifiDriver;
    AdamSignalDriver(){
        aScanResults = new Hashtable<String, AdamScanResultBase>();
    }
    public void AddScanResult(String key, AdamScanResultBase scanResult){
        aScanResults.put(key, scanResult);
    }
}
