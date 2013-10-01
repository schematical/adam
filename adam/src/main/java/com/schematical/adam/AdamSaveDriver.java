package com.schematical.adam;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by user1a on 10/1/13.
 */
public class AdamSaveDriver {
    public void Save(){
        HttpParams httpParams = new BasicHttpParams();
        HttpClient client = new DefaultHttpClient(httpParams);
        HttpPost httpost = new HttpPost("http://employeestracking.appspot.com/clockin.add_clockin");
        httpost.setHeader("Accept", "application/json");
        httpost.setHeader("Content-type", "application/json");
        System.out.println("2");
        JSONObject data = new JSONObject();
        JSONObject userrequest = new JSONObject();
        HttpResponse response = null;
        try {
            userrequest.put("username","TestDemo");
            userrequest.put("status",1);
            data.put("userrequest", userrequest);
            System.out.println(data);
            httpost.getParams().setParameter("data",data);
            try {
                response = client.execute(httpost);
                System.out.println(response);
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
