package com.schematical.adam.signal.telephony;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;


import com.schematical.adam.AdamWorldActivity;

import java.util.List;

/**
 * Created by user1a on 10/12/13.
 */
public class AdamTelephoneDriver {

    protected static TelephonyManager telephonyManager;
    private static List<CellInfo> cellInfo;

    @SuppressLint("NewApi")
    public AdamTelephoneDriver(){

        telephonyManager = (TelephonyManager) AdamWorldActivity.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        cellInfo = telephonyManager.getAllCellInfo();

    }
}
