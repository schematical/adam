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
    private StringBuilder sb;
    private List<ScanResult> wifiList;
    private AdamActivityMain mC;
    AdamWifiManager(AdamActivityMain c){
        mC = c;
    }
    public void onReceive(Context c, Intent intent) {
        sb = new StringBuilder();
        wifiList = mC.wifiManager.getScanResults();
        for(int i = 0; i < wifiList.size(); i++){
            /*sb.append(new Integer(i+1).toString() + ".");
            sb.append((wifiList.get(i)).toString());
            sb.append("\\n");*/
            ScanResult sr = wifiList.get(i);
            mC.UpdateAdamObject(
                    sr.SSID,
                    sr
            );
        }


    }
}

