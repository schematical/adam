package com.schematical.adam;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/**
 * Created by user1a on 10/1/13.
 */
public class AdamSaveDriver extends AsyncTask<URL, Integer, Long> {
    private AdamActivityMain am;
    public AdamSaveDriver(AdamActivityMain adamActivityMain) {
        am = adamActivityMain;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) am.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void Save(){
        this.execute();
    }
    @Override
    protected Long doInBackground(URL... urls) {
        if ( isNetworkAvailable()){
            SaveToServer();

        }else{

            SaveToLocal();
        }
        return new Long(0);
    }
    public void Load(){
        LoadFromLocal();
    }
    public void SaveToLocal(){
        SharedPreferences pref = am.getApplicationContext().getSharedPreferences("adam_objects", android.content.Context.MODE_PRIVATE );
        JSONObject data = GetSaveBody();
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("last_state", data.toString());
        editor.commit();
        am.SetStatus("Saved Locally");
    }
    public void ParseJsonData(JSONObject jObj) throws JSONException{

        Iterator<String> keys = jObj.keys();
        while(keys.hasNext()){
            String key = keys.next();

            Object aoData = (JSONObject) jObj.get(key);

            am.UpdateAdamObject(
                (String)((JSONObject) aoData).get("id"),
                aoData
            );
        }

    }
    public void LoadFromLocal(){
        SharedPreferences pref = am.getApplicationContext().getSharedPreferences("adam_objects", android.content.Context.MODE_PRIVATE );

        SharedPreferences.Editor editor = pref.edit();
        String json = pref.getString("last_state", "{}");
        try {
            JSONObject jObj = new JSONObject(json);
            ParseJsonData(jObj);
            //editor.putString("last_state", "{}");
            editor.commit();

        } catch (JSONException e) {
            am.SetStatus("Error loading last save state");
        }


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
            JSONObject data = GetSaveBody();
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
    public JSONObject GetSaveBody(){
        try {
            JSONObject data = new JSONObject();
            Hashtable<String, AdamObject> aObjects = am.GetAdamObjects();
            Enumeration<String> aKeys = aObjects.keys();
            while(aKeys.hasMoreElements()){
                String key = aKeys.nextElement();
                AdamObject aObj = aObjects.get(key);
                data.put(key, aObj.toJSONObject());
            }

            return data;

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            am.SetStatus("Error:" + e.getMessage());
            return null;
        }

    }
    // This is called each time you call publishProgress()
    protected void onProgressUpdate(Integer... progress) {

    }

    // This is called when doInBackground() is finished
    protected void onPostExecute(Long result) {

    }

}
