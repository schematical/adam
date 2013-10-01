package com.schematical.adam;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.wifi.ScanResult;

import java.sql.Timestamp;
import java.util.ArrayList;
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
    AdamObject(AdamActivityMain nAm, String id){
        am = nAm;
        id = id;
        pings  = new ArrayList<AdamPing>();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xff00ff00);
        paint.setTextSize(100);
    }
    public String GetId(){
        return id;
    }
    public void Draw(Canvas canvas){
        currX = Math.round(currX + goalX)/2;
        currY = Math.round(currY + goalY)/2;
        canvas.drawText(
                "Test",
                currX,
                currY,
                paint
        );
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
        if(data instanceof ScanResult){
            //Track in a list of pings
            ScanResult sr = (ScanResult) data;

            AdamPing ap = new AdamPing(
                    sr.level,
                    am.GetLocation().getLatitude(),
                    am.GetLocation().getLongitude(),
                    am.GetLocation().getAltitude(),
                    am.GetLocation().getAccuracy(),
                    new Timestamp(date.getTime())
            );
            pings.add(ap);
        }
    }
    public void UpdateLatLng(){
        AdamPing p1 = null;
        AdamPing p2 = null;
        double a = 0;//Ping of p1
        double b = 0;//Ping of p2
        double c = 0;//distance between p1 and p2
        double aAngle = 0;
        double bAngle = 0;
        
        for(int i = 0; i < pings.size(); i++){
            p2 = p1;
            p1 = pings.get(i);
            if(p1 != null){
                a = p1.pingStrength * AdamPing.METERS_DMB;
                b = p2.pingStrength * AdamPing.METERS_DMB;
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
            }

        }

    }

}
