package com.schematical.adam.socket;

import com.schematical.adam.AdamActivityMain;
import com.schematical.adam.location.AdamLocation;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
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
