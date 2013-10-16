package com.schematical.adam;

import com.schematical.adam.comm.AdamCommDriver;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by user1a on 10/12/13.
 */
public class AdamSystemUpdater extends TimerTask {
    protected boolean blnRunning = false;
    private Timer timer;
    private int seconds  =5;

    @Override
    public void run() {

        AdamCommDriver.Checkin();
        timer = new Timer();
        timer.schedule(
                new AdamSystemUpdater(),
                seconds*1000
        );

    }
}
