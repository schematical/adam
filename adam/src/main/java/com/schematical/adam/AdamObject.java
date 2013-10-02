package com.schematical.adam;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * Created by user1a on 10/1/13.
 */
public class AdamObject {

    protected AdamActivityMain am;
    private final Paint paint;
    protected String id;
    protected int goalX = 0;
    protected int goalY = 0;
    protected int currX = 0;
    protected int currY = 0;

    protected double lat;
    protected double lng;
    protected double altutide;

    protected List<AdamPing> pings;
    AdamObject(AdamActivityMain nAm, String nId){
        am = nAm;
        id = nId;
        pings  = new ArrayList<AdamPing>();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xff00ff00);
        paint.setTextSize(50);
    }
    public String GetId(){
        return id;
    }
    public boolean IsFocused(){
        return am.mPreview.focusObjectId.equals(this.id);
    }
    public void Draw(Canvas canvas){
        currX = Math.round(currX + goalX)/2;
        currY = Math.round(currY + goalY)/2;
        canvas.drawText(
                this.id,
                currX,
                currY,
                paint
        );
        if(IsFocused()){
            Location newLocation = new Location("gps");
            Location oLocation = am.GetLocation();
            newLocation.setLatitude(this.lat);
            newLocation.setLongitude(this.lng);
            newLocation.setAltitude(oLocation.getAltitude());

            newLocation.setAccuracy(3.0f);
            float cDist = oLocation.distanceTo(newLocation);
            canvas.drawText(
                    "Dist:" + Float.toString(cDist),
                    currX,
                    currY + 45,
                    paint
            );
        }
    }
    public void SetGoalXY(int nGoalX, int nGoalY){
        goalX = nGoalX;
        goalY = nGoalY;
    }
    public double GetLat(){
        return lat;
    }
    public double GetLng(){
        return lng;
    }
    public double GetAltitude(){
        return altutide;
    }
    public void Update(Object data){
        java.util.Date date= new java.util.Date();
        if(data instanceof JSONObject){
            try{
                JSONObject jObj = (JSONObject) data;
                this.id = (String)jObj.get("id");
                if(jObj.has("lat") && jObj.has("lng")){
                    this.lat = (Double)jObj.get("lat");
                    this.lng = (Double)jObj.get("lng");
                }
                JSONObject jPings = (JSONObject)jObj.get("pings");
                Iterator keys = jPings.keys();
                while(keys.hasNext()){
                    JSONObject jPingData = (JSONObject) jPings.get((String) keys.next());
                    double pingLat = (Double)jPingData.get("pingLat");
                    double pingLng = 0d;
                    if(jPingData.has("pintLng")){
                        pingLng = (Double)jPingData.get("pintLng");
                    }else{
                        pingLng = (Double)jPingData.get("pingLng");
                    }
                    double pingElev = (Integer) jPingData.get("pingElev");
                    double pingAccuracy =  (Integer) jPingData.get("pingAccuracy");
                    double pingStrength = (Integer)   jPingData.get("pingStrength");
                    double pingFrequency = (Integer)   jPingData.get("pingFrequency");
                    long pingTimestamp = (Long) jPingData.get("pingTimestamp");
                    AdamPing objPing = new AdamPing(
                        pingLat,
                        pingLng,
                        pingElev,
                        pingAccuracy,
                        pingStrength,
                        pingFrequency,
                        pingTimestamp
                    );
                    pings.add(objPing);
                }

            }catch(JSONException e){
                Log.d("adam", "ADAMERROR:!!!! " + e.getMessage());
                e.printStackTrace();
            }
        }

        if(data instanceof ScanResult){
            //Track in a list of pings
            ScanResult sr = (ScanResult) data;
            Location objLocation = am.GetLocation();
            if(objLocation != null){
                AdamPing ap = new AdamPing(
                        sr.level,
                        sr.frequency,
                        objLocation.getLatitude(),
                        objLocation.getLongitude(),
                        objLocation.getAltitude(),
                        objLocation.getAccuracy(),
                        date.getTime()
                );
                pings.add(ap);
                UpdateLatLng();
            }
        }
    }
    public void UpdateLatLng(){
        if(pings.size() > 1){
            AdamPing p1 = null;
            AdamPing p2 = null;
            double a = 0;//Ping of p1
            double b = 0;//Ping of p2
            double c = 0;//distance between p1 and p2
            double aAngle = 0;
            double bAngle = 0;
            double cTX = 9999;
            double cTY = 9999;
            double guessCt = 0;
            for(int i = 0; i < pings.size(); i++){
                p2 = p1;
                p1 = pings.get(i);
                if(p2 != null){

                    a = (100 - p1.pingStrength) * AdamPing.METERS_DMB;
                    b = (100 - p2.pingStrength) * AdamPing.METERS_DMB;
                    c = p1.DistanceFrom(p2);//In Meters
                    aAngle = Math.acos(
                            Math.pow(a,2) + Math.pow(b,2) + Math.pow(c,2) /
                            -2 * b * c
                    );

                    bAngle = Math.acos(
                            Math.pow(b,2) + Math.pow(a,2) + Math.pow(c,2) /
                                    -2 * a * c
                    );
                    double aAngleRelGround = p1.AngleInRelationTo(p2);
                    //Generate 2 estimates
                    double c1Y = Math.sin(aAngleRelGround + aAngle) * a;
                    double c1X = Math.cos(aAngleRelGround + aAngle) * a;
                    double c2Y = Math.sin(aAngleRelGround - aAngle) * a;
                    double c2X = Math.cos(aAngleRelGround - aAngle) * a;
                    if(cTX == 9999){
                        cTX = c1X;
                        cTY = c1Y;
                    }else{
                        cTX += c1X;
                        cTY += c1Y;
                    }
                    cTX += c2X;
                    cTY += c2Y;
                    guessCt += 2;
                }


            }
            this.lat = cTX / guessCt;
            this.lng = cTY / guessCt;
        }else{
            this.lat = pings.get(0).pingLat;
            this.lng = pings.get(0).pingLng;
        }

    }

    public JSONObject toJSONObject() {
        JSONObject jObj = new JSONObject();

        try {
            jObj.put("id", this.id);
            JSONObject jPings = new JSONObject();

            for(int i = 0; i < this.pings.size(); i++){
                jPings.put(Integer.toString(i), this.pings.get(i).toJSONObject());

            }
            jObj.put("pings", jPings);


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jObj;


    }
    public void ClearPings(){
        //this.pings = new ArrayList<AdamPing>();
    }
}
