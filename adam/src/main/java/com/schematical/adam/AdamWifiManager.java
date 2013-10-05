package com.schematical.adam;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

/**
 * Created by user1a on 10/1/13.
 */
public class AdamWifiManager extends BroadcastReceiver {
    public static final String TYPE = "Wifi";
    private StringBuilder sb;
    public WifiManager wifiManager;
    private List<ScanResult> wifiList;
    private AdamActivityMain mC;
    AdamWifiManager(AdamActivityMain c){
        mC = c;
        wifiManager = (WifiManager) mC.getSystemService(Context.WIFI_SERVICE);




    }
    public void Disconnect(){
        wifiManager.disconnect();
    }

    public void StartScan(){
        mC.registerReceiver(this, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
    }
    public void onReceive(Context c, Intent intent) {
        wifiList = wifiManager.getScanResults();
        if(
            (mC.GetLocation() != null) &&
            (mC.allowPing)
        ){
            sb = new StringBuilder();

            for(int i = 0; i < wifiList.size(); i++){

                ScanResult sr = wifiList.get(i);
                mC.UpdateAdamObject(
                        sr.BSSID,
                        sr
                );
            }
            mC.pingCt += 1;
            mC.allowPing = false;
            mC.SyncWithServers();
        }


    }
    public boolean IsWifiConnected(){
        ConnectivityManager connManager = (ConnectivityManager) mC.getSystemService(mC.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            return true;
        }
        return false;
    }
}

