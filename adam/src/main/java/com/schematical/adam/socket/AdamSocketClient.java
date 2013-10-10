package com.schematical.adam.socket;

/**
 * Created by user1a on 10/10/13.
 */

import java.io.InputStreamReader;
import java.net.InetAddress;



import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.nio.CharBuffer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.schematical.adam.AdamActivityMain;

public class AdamSocketClient {
    private AdamActivityMain am;
    private Socket socket;
    private PrintWriter out;
    private static final int SERVERPORT = 1337;
    private static final String SERVER_IP = "192.168.1.51";






    public AdamSocketClient(AdamActivityMain am) {
        new Thread(new ClientThread()).start();

    }
    public void InitWriter() throws IOException {
        out = new PrintWriter(
            new BufferedWriter(
                new OutputStreamWriter(
                    socket.getOutputStream()
                )
            ),
            true
        );
    }
    public void InitReader(){
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
    public void Write(String strOut){
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

    class ClientThread implements Runnable {

        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

                socket = new Socket(serverAddr, SERVERPORT);

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

    }
}
