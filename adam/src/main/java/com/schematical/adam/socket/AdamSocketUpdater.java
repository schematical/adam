package com.schematical.adam.socket;

import com.schematical.adam.location.AdamLocation;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by user1a on 10/12/13.
 */
public class AdamSocketUpdater extends TimerTask {
    protected boolean blnRunning = false;
    private Timer timer;
    private int seconds  =5;

    @Override
    public void run() {
        AdamLocation.UpdateServer();

        AdamSocketClient.SendUpdate();
        timer = new Timer();
        timer.schedule(
                new AdamSocketUpdater(),
                seconds*1000
        );

    }
}
