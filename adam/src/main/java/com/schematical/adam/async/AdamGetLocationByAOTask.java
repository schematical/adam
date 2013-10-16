package com.schematical.adam.async;

import android.os.AsyncTask;
import android.util.Log;

import com.schematical.adam.old.AdamActivityMain;
import com.schematical.adam.model.old.AdamObject;
import com.schematical.adam.location.AdamLocation;

import org.apache.http.HttpEntity;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by user1a on 10/1/13.
 */
public class AdamGetLocationByAOTask extends AsyncTask<URL, Integer, Long> {

    public AdamGetLocationByAOTask() {

    }

    public void Load(){

        this.execute();
    }
    @Override
    protected Long doInBackground(URL... urls) {
        if ( AdamSaveDriver.isNetworkAvailable()){

            //Get a snap shot of all the drivers
            Hashtable<String, AdamObject> aObjects = AdamActivityMain.GetAdamObjects();
            if(aObjects.size() < 3){
                AdamLocation.CleanUp();
                return new Long(0);
            }
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://lab.schematical.com/adam/locate.php");
            JSONObject jObj = null;
            String json = "{}";

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("_meta", "12345"));
                JSONObject data = AdamSaveDriver.GetSaveBody();
                nameValuePairs.add(new BasicNameValuePair("data", data.toString()));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity httpEntity = response.getEntity();
                InputStream is = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                json = sb.toString();

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
            // try parse the string to a JSON object
            try {
                jObj = new JSONObject(json);
                AdamLocation.ParseLocationData(jObj);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
        }
        return new Long(0);
    }

    // This is called each time you call publishProgress()
    protected void onProgressUpdate(Integer... progress) {

    }

    // This is called when doInBackground() is finished
    protected void onPostExecute(Long result) {
        AdamLocation.CleanUp();
    }

}
