package com.schematical.adam;


import android.graphics.Paint;
import android.hardware.Camera;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;


import com.schematical.adam.img.AdamImgRecDriver;
import com.schematical.adam.location.AdamLocation;
import com.schematical.adam.tts.AdamTTSDriver;

import java.util.Enumeration;
import java.util.Hashtable;


public class AdamActivityMain extends Activity {

    public static final double PING_DISTANCE = 10;
    private Hashtable mObjects = new Hashtable();


    private Camera mCamera;
    public AdamView mView;

    private Paint paint;

    private AdamSensorDriver sensorDriver;
    private String status;


    private AdamWifiManager wifiReceiver;
    private AdamSaveDriver saveDriver;
    public AdamBluetooth bluetoothDriver;

    public boolean allowPing = false;
    public int pingCt = 0;
    private AdamLocation aLocation;
    private AdamTTSDriver speachDriver;
    private AdamImgRecDriver imgRecDriver;



    public AdamActivityMain() {
    }

    public void Ping(){
        this.SetStatus("Sending Ping...");
        this.allowPing = true;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorDriver = new AdamSensorDriver(this);
        speachDriver = new AdamTTSDriver(this);
        status = "Uninitialized";
        // Create an instance of Camera

        saveDriver = new AdamSaveDriver(this);
        //saveDriver.Load();
        // Create our Preview view and set it as the content of our activity.
        mCamera = getCameraInstance();
        imgRecDriver = new AdamImgRecDriver(this);
        imgRecDriver.WatchCamera(mCamera);
        mView = new AdamView(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mView);

        bluetoothDriver = new AdamBluetooth(this);

        aLocation = new AdamLocation(this);
        wifiReceiver  = new AdamWifiManager(this);
        wifiReceiver.StartScan();
        if(wifiReceiver.IsWifiConnected()){
            //this.SetStatus("Cannot scan while connected to wifi");
            saveDriver.Save();
        }


    }
    public void Speak(String text){
        this.speachDriver.Speak(text);
    }
    public String GetStatus(){
        return status;
    }
    public void SetStatus(String nStatus){
        status = nStatus;
        //this.Speak(status);
    }
    public void UpdateAdamObject(String id, Object data){
        AdamObject ao = (AdamObject) mObjects.get(id);
        if(ao == null){
            ao = new AdamObject(this, id);
            mObjects.put(id, ao);
        }
        ao.Update(data);

    }
    public void SyncWithServers(){
        this.SetStatus("Syncing with servers");
        this.saveDriver.Save();
    }
    public Location GetLocation(){
        return aLocation.GetLocation();
    }
    public Hashtable<String, AdamObject>GetAdamObjects(){
        return mObjects;
    }
    public AdamObject GetAdamObject(String id){
        Enumeration names = mObjects.keys();
        String objId = null;
        while(names.hasMoreElements()) {
            objId = (String) names.nextElement();

            AdamObject mObject = (AdamObject) mObjects.get(objId);
            if(
                (mObject != null) &&
                (
                    (mObject.GetId().equals(id)) ||
                    (mObject.GetAlias().equals(id))
                )
            ){
                return mObject;
            }
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorDriver.onResume();

        wifiReceiver.StartScan();
        if(GetLocation() != null){
            bluetoothDriver.StartDiscovery();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(wifiReceiver);
        sensorDriver.onPause();
        bluetoothDriver.UnregisterListener();
    }
    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
            Log.d("Adam", "Error setting camera preview: " + e.getMessage());
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:

                return true;
            case R.id.action_ping:
                this.allowPing = true;
                return true;
            case R.id.action_save:
                this.saveDriver.Save();
                return true;
            case R.id.action_load:
                this.saveDriver.Load();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public AdamView GetView() {
        return mView;
    }
}
