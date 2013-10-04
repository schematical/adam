package com.schematical.adam;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.android.gms.internal.am;

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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user1a on 10/1/13.
 */
public class AdamLoadTask extends AsyncTask<URL, Integer, Long> {
    private AdamSaveDriver ad;
    public AdamLoadTask(AdamSaveDriver nAd) {
        ad = nAd;
    }

    public void Save(){

        this.execute();
    }
    @Override
    protected Long doInBackground(URL... urls) {
        if ( ad.isNetworkAvailable()){
            LoadFromServer();

        }else{

            LoadFromLocal();
        }
        return new Long(0);
    }
    public void LoadFromServer(){
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://lab.schematical.com/adam/load.php");
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
    }
    public void LoadFromLocal(){
        SharedPreferences pref = ad.getAdamActivityMain().getApplicationContext().getSharedPreferences("adam_objects", android.content.Context.MODE_PRIVATE );

        SharedPreferences.Editor editor = pref.edit();
        String json = pref.getString("last_state", "{}");
        try {
            JSONObject jObj = new JSONObject(json);
            ad.ParseJsonData(jObj);
            //editor.putString("last_state", "{}");
            editor.commit();

        } catch (JSONException e) {
            ad.getAdamActivityMain().SetStatus("Error loading last save state");
        }


    }


    // This is called each time you call publishProgress()
    protected void onProgressUpdate(Integer... progress) {

    }

    // This is called when doInBackground() is finished
    protected void onPostExecute(Long result) {
        ad.CleanUp();
    }

}
