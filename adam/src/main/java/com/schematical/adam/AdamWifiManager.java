package com.schematical.adam;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;

import java.util.List;

/**
 * Created by user1a on 10/1/13.
 */
public class AdamWifiManager extends BroadcastReceiver {
    public static final String TYPE = "Wifi";
    private StringBuilder sb;
    private List<ScanResult> wifiList;
    private AdamActivityMain mC;
    AdamWifiManager(AdamActivityMain c){
        mC = c;
    }
    public void onReceive(Context c, Intent intent) {
        wifiList = mC.wifiManager.getScanResults();
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
}

