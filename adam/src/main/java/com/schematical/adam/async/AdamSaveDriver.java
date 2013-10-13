package com.schematical.adam.async;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.schematical.adam.AdamActivityMain;
import com.schematical.adam.AdamObject;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/**
 * Created by user1a on 10/1/13.
 */
public class AdamSaveDriver {
    private static AdamActivityMain am;
    private static AdamSaveTask at;
    private AdamLoadTask alt;
    public AdamSaveDriver(AdamActivityMain adamActivityMain) {
        am = adamActivityMain;
    }
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) am.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void Save(){

        at = new AdamSaveTask(this);
        at.execute();
    }
    public static void CleanUp(){
        at = null;
    }
    public void Load(){
        alt = new AdamLoadTask(this);
        alt.execute();
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
        am.SetStatus(
                "Loaded " + Integer.toString(jObj.length()) + " Objects"
        );

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

    }
    public AdamActivityMain getAdamActivityMain(){
        return  am;
    }
    public static JSONObject GetSaveBody(){
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
