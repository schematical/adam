package com.schematical.adam;


import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.wifi.WifiManager;
import android.net.wifi.ScanResult;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.FrameLayout;
import android.location.LocationManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;

import java.util.Hashtable;


public class AdamActivityMain extends Activity implements SensorEventListener,LocationListener, GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener  {


    private Hashtable mObjects = new Hashtable();


    private Camera mCamera;
    private AdamView mPreview;

    private Paint paint;

    private SensorManager mSensorManager;
    private Sensor mOrientation;

    public WifiManager wifiManager;
    private AdamWifiManager wifiReceiver;
    private Location mLocation;

    public AdamActivityMain() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new AdamView(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


        wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        wifiReceiver  = new AdamWifiManager(this);
        wifiManager.startScan();

        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
    }
    public void UpdateAdamObject(String id, Object data){
        AdamObject ao = (AdamObject) mObjects.get(id);
        if(ao == null){
            ao = new AdamObject(this, id);
            mObjects.put(id, ao);
        }
        ao.Update(data);

    }
    public Location GetLocation(){
        return mLocation;
    }
    public Hashtable<String, AdamObject>GetAdamObjects(){
        return mObjects;
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }





    public void onSensorChanged(SensorEvent event) {
        double xAngle = (event.values[0]/180) * Math.PI;// * Math.PI / 2;
        double yAngle = (event.values[1]/180)  * Math.PI;// * Math.PI / 2;
        double zAngle = (event.values[2]/180) * Math.PI;// * Math.PI / 2;

        mPreview.UpdateOrientation(xAngle, yAngle, zAngle);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_NORMAL);
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onPause();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(wifiReceiver);
        mSensorManager.unregisterListener(this);
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

    public void onLocationChanged(Location location) {
        mLocation = location;
        // Report to the UI that the location was updated
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Log.d("Adam", msg);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void onProviderEnabled(String provider) {

    }

    public void onProviderDisabled(String provider) {

    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.d("Adam", "CONNECTED!");
    }

    @Override
    public void onDisconnected() {
        Log.d("Adam", "DISCONNECTED!");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("Adam", "Connection Failed :(");
    }
}
