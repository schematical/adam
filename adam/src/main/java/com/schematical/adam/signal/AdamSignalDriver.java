package com.schematical.adam.signal;

import com.schematical.adam.signal.bluetooth.AdamBluetoothDriver;
import com.schematical.adam.signal.gps.AdamGPSDriver;
import com.schematical.adam.signal.wifi.AdamWifiDriver;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by user1a on 10/16/13.
 */
public class AdamSignalDriver {
    protected static Hashtable<String, AdamScanResultBase> aScanResults;
    protected static AdamBluetoothDriver adamBluetoothDriver;
    protected static AdamWifiDriver adamWifiDriver;
    protected static AdamGPSDriver adamGPSDriver;
    public AdamSignalDriver(){
        aScanResults = new Hashtable<String, AdamScanResultBase>();
        adamGPSDriver = new AdamGPSDriver();
        adamWifiDriver = new AdamWifiDriver();
        adamBluetoothDriver = new AdamBluetoothDriver();
    }
    public static void AddScanResult(AdamScanResultBase scanResult){
        String key = scanResult.getType() + ":" + scanResult.getId();
        aScanResults.put(key, scanResult);
    }
    public static void Connect(){
        adamWifiDriver.Connect();
        adamBluetoothDriver.Connect();
    }
    public static void Disconnect(){
        adamWifiDriver.Disconnect();
        adamBluetoothDriver.Disconnect();
        adamGPSDriver.Disconnect();
    }
    public static ArrayList<JSONObject> GetJSON(){
        ArrayList<JSONObject> aReturn = new ArrayList<JSONObject>();
        Enumeration<String> keys = aScanResults.keys();
        while(keys.hasMoreElements()){
            String key = keys.nextElement();
            AdamScanResultBase sr = aScanResults.get(key);
            aReturn.add(sr.toJSONObject());
        }
        return aReturn;

    }
}
