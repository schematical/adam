package com.schematical.adam.comm.socket;

/**
 * Created by user1a on 10/10/13.
 */

import java.io.InputStreamReader;
import java.net.InetAddress;



import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.schematical.adam.comm.AdamCommClientBase;
import com.schematical.adam.comm.AdamCommDriver;

import org.json.JSONObject;

public class AdamSocketClient extends AdamCommClientBase {

    private static Socket socket;
    private PrintWriter out;

    public boolean IsConnected(){
        if(socket != null){
            return true;
        }
        return false;
    }

    public void Connect(){
        new Thread(new AdamSocketUpdateThread()).start();
    }
    protected void InitWriter() throws IOException {
        out = new PrintWriter(
            new BufferedWriter(
                new OutputStreamWriter(
                    socket.getOutputStream()
                )
            ),
            true
        );
    }
    protected void InitReader(){
        try {
            InputStreamReader in = new InputStreamReader(
                socket.getInputStream()
            );
            char[] cb = new char[0];
            in.read(cb);
            StringBuilder sb = new StringBuilder();
            sb.append(cb);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void Send(JSONObject data){
        Send(data.toString());
    }
    public void Send(String strOut){
        if(socket == null){//Shitty magic debugger
            return;
        }
        try {
            if(out == null){
                InitWriter();
            }
            out.append(strOut);
            out.flush();


        } catch (UnknownHostException e) {
            e.printStackTrace();
            out = null;
        } catch (IOException e) {
            e.printStackTrace();
            out = null;
        } catch (Exception e) {
            out = null;
            e.printStackTrace();
        }
    }
    public static void InitSocket() throws IOException {
        InetAddress serverAddr = InetAddress.getByName(AdamCommDriver.SERVER_IP);
        socket = new Socket(serverAddr, AdamCommDriver.SERVERPORT);
    }
}

