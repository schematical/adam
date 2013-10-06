package com.schematical.adam;


import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager;
import android.net.wifi.ScanResult;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.location.LocationManager;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.schematical.adam.img.AdamImgRecDriver;
import com.schematical.adam.tts.AdamTTSDriver;

import java.util.Enumeration;
import java.util.Hashtable;


public class AdamActivityMain extends Activity implements SensorEventListener {

    public static final double PING_DISTANCE = 10;
    private Hashtable mObjects = new Hashtable();


    private Camera mCamera;
    public AdamView mView;

    private Paint paint;

    private SensorManager mSensorManager;
    private Sensor mOrientation;
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


        speachDriver = new AdamTTSDriver(this);
        status = "Uninitialized";
        // Create an instance of Camera
        mCamera = getCameraInstance();
        saveDriver = new AdamSaveDriver(this);
        saveDriver.Load();
        // Create our Preview view and set it as the content of our activity.
        mView = new AdamView(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mView);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        bluetoothDriver = new AdamBluetooth(this);

        aLocation = new AdamLocation(this);
        wifiReceiver  = new AdamWifiManager(this);
        wifiReceiver.StartScan();
        if(wifiReceiver.IsWifiConnected()){
            //this.SetStatus("Cannot scan while connected to wifi");
            saveDriver.Save();
        }
        imgRecDriver = new AdamImgRecDriver(this);
        imgRecDriver.WatchCamera(mCamera);

    }
    public void Speak(String text){
        this.speachDriver.Speak(text);
    }
    public String GetStatus(){
        return status;
    }
    public void SetStatus(String nStatus){
        status = nStatus;
        this.Speak(status);
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
                (mObject.GetId().equals(id)) ||
                (mObject.GetAlias().equals(id))
            ){
                return mObject;
            }
        }
        return null;
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }





    public void onSensorChanged(SensorEvent event) {
        double xAngle = (event.values[0]/180) * Math.PI;// * Math.PI / 2;
        double yAngle = (event.values[1]/180)  * Math.PI;// * Math.PI / 2;
        double zAngle = (event.values[2]/180) * Math.PI;// * Math.PI / 2;

        mView.UpdateOrientation(xAngle, yAngle, zAngle);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_NORMAL);

        wifiReceiver.StartScan();
        if(GetLocation() != null){
            bluetoothDriver.StartDiscovery();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(wifiReceiver);
        mSensorManager.unregisterListener(this);
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
