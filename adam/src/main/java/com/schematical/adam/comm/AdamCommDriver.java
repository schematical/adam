package com.schematical.adam.comm;

import com.schematical.adam.comm.async.AdamAsyncDriver;
import com.schematical.adam.comm.socket.AdamSocketClient;
import com.schematical.adam.signal.AdamSignalDriver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by user1a on 10/16/13.
 * This class determines if it has a socket connection or it should do an async task
 */
public class AdamCommDriver {

    public static final String SERVER_IP = "192.168.88.103";
    public static final int SERVERPORT = 1337;
    protected static AdamSocketClient adamSocketClient;
    protected static AdamAsyncDriver adamAsyncDriver;
    public AdamCommDriver(){
        adamSocketClient = new AdamSocketClient();
        adamAsyncDriver = new AdamAsyncDriver();
        //TODO: Create local
    }
    public static void Checkin(){
        JSONObject data = new JSONObject();
        try {
            data.put("body", AdamSignalDriver.GetJSON());
            Send(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void Send(JSONObject data){
        /*if(adamSocketClient.IsConnected()){
            return adamSocketClient.Send(data);
        }*/
        adamAsyncDriver.Send(data.toString());
    }
    public static void ParseJsonData(JSONObject jObj) throws JSONException {

        Iterator<String> keys = jObj.keys();
        while(keys.hasNext()){
            String key = keys.next();

            Object aoData = (JSONObject) jObj.get(key);


        }
    }


}
