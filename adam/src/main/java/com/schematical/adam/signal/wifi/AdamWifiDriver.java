package com.schematical.adam.signal.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.schematical.adam.AdamWorldActivity;
import com.schematical.adam.signal.AdamSignalDriver;
import com.schematical.adam.signal.iAdamSignalDriver;


import java.util.List;

/**
 * Created by user1a on 10/1/13.
 */
public class AdamWifiDriver extends BroadcastReceiver implements iAdamSignalDriver {
    public static final String TYPE = "Wifi";
    private StringBuilder sb;
    public WifiManager wifiManager;
    private List<ScanResult> wifiList;

    public AdamWifiDriver(){
        wifiManager = (WifiManager) AdamWorldActivity.getInstance().getSystemService(Context.WIFI_SERVICE);
    }
    public void Disconnect(){
        AdamWorldActivity.getInstance().unregisterReceiver(this);
        wifiManager.disconnect();

    }

    public void Connect(){
        AdamWorldActivity.getInstance().registerReceiver(this, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
    }
    public void onReceive(Context c, Intent intent) {

        wifiList = wifiManager.getScanResults();

            sb = new StringBuilder();

            for(int i = 0; i < wifiList.size(); i++){

                AdamWifiScanResult sr = new AdamWifiScanResult(wifiList.get(i));
                AdamSignalDriver.AddScanResult(sr);
            }
    }
    public boolean IsWifiConnected(){
        ConnectivityManager connManager = (ConnectivityManager) AdamWorldActivity.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            return true;
        }
        return false;
    }
}

