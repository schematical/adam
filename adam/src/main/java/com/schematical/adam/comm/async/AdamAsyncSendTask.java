package com.schematical.adam.comm.async;

import android.os.AsyncTask;

import java.net.URL;


/**
 * Created by user1a on 10/1/13.
 */
public class AdamAsyncSendTask extends AsyncTask<URL, Integer, Long> {

    private final String data;

    public AdamAsyncSendTask(String nData) {
        data = nData;
    }


    @Override
    protected Long doInBackground(URL... urls) {
        AdamAsyncDriver.BackgroundSend(data);
        return new Long(0);
    }

    // This is called each time you call publishProgress()
    protected void onProgressUpdate(Integer... progress) {

    }

    // This is called when doInBackground() is finished
    protected void onPostExecute(Long result) {
        //AdamAsyncDriver.CleanUp();
    }
    /*public void Send(){
        //if ( AdamSaveDriver.isNetworkAvailable()){

        //Get a snap shot of all the drivers
        Hashtable<String, AdamObject> aObjects = AdamActivityMain.GetAdamObjects();
        if(aObjects.size() < 3){
            AdamLocationDriver.CleanUp();
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
            AdamLocationDriver.ParseLocationData(jObj);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

    }*/
}
