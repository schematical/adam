package com.schematical.adam.comm.socket;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by user1a on 10/16/13.
 */
public class AdamSocketUpdateThread implements Runnable {

    @Override
    public void run() {
        try {
            AdamSocketClient.InitSocket();
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}


