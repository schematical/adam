package com.schematical.adam.telephony;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;

import com.schematical.adam.old.AdamActivityMain;

import java.util.List;

/**
 * Created by user1a on 10/12/13.
 */
public class AdamTelephoneDriver {
    protected static AdamActivityMain am;
    protected static TelephonyManager telephonyManager;
    private static List<CellInfo> cellInfo;

    @SuppressLint("NewApi")
    public static void Init(AdamActivityMain nAm){
        am = nAm;
        telephonyManager = (TelephonyManager) am.getSystemService(Context.TELEPHONY_SERVICE);
        cellInfo = telephonyManager.getAllCellInfo();

    }
}
