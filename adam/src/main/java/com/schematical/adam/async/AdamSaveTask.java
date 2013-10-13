package com.schematical.adam.async;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user1a on 10/1/13.
 */
public class AdamSaveTask extends AsyncTask<URL, Integer, Long> {
    private AdamSaveDriver ad;
    public AdamSaveTask(AdamSaveDriver nAd) {
        ad = nAd;
    }

    public void Save(){

        this.execute();
    }
    @Override
    protected Long doInBackground(URL... urls) {
        if ( ad.isNetworkAvailable()){
            SaveToServer();

        }else{

            SaveToLocal();
        }
        return new Long(0);
    }

    public void SaveToLocal(){
        SharedPreferences pref = ad.getAdamActivityMain().getApplicationContext().getSharedPreferences("adam_objects", Context.MODE_PRIVATE );
        JSONObject data = ad.GetSaveBody();
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("last_state", data.toString());
        editor.commit();
        ad.getAdamActivityMain().SetStatus("Saved Locally");
    }


    public void SaveToServer(){
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://lab.schematical.com/adam/ping.php");
        //HttpPost httppost = new HttpPost("http://l-adam.schematical.com/ping.php");


        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("_meta", "12345"));
            JSONObject data = ad.GetSaveBody();
            nameValuePairs.add(new BasicNameValuePair("data", data.toString()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
       /* HttpParams httpParams = new BasicHttpParams();
        HttpClient client = new DefaultHttpClient(httpParams);
        HttpPost httpost = new HttpPost("http://lab.schematical.com/adam/ping.php");
        httpost.setHeader("Accept", "application/json");
        httpost.setHeader("Content-type", "application/json");
        httpost.setHeader("Accept-Charset", "utf-8");



        HttpResponse response = null;

        try {
            JSONObject data = GetSaveBody();
            StringEntity stringBuilder = new StringEntity(data.toString(),HTTP.UTF_8);
            stringBuilder.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            //httpost.setEntity(stringBuilder);
            String strData = data.toString();
            httpParams.setParameter("xxx", "XVXCV");
            *//*httpParams.setParameter("data", strData);
            httpParams.setParameter("data_obj", data);*//*
            httpost.setParams(httpParams);
            response = client.execute(httpost);

            System.out.println(response);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            am.SetStatus("Error:" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            am.SetStatus("Error:" + e.getMessage());
            e.printStackTrace();
        }*/
    }

    // This is called each time you call publishProgress()
    protected void onProgressUpdate(Integer... progress) {

    }

    // This is called when doInBackground() is finished
    protected void onPostExecute(Long result) {
        ad.CleanUp();
    }

}