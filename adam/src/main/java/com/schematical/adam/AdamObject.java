package com.schematical.adam;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.wifi.ScanResult;

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
        return 0.1;
    }
    public double GetLng(){
        return 0.1;
    }
    public double GetAltitude(){
        return 0.1;
    }
    public void Update(Object data){
        if(data instanceof ScanResult){
            //Track in a list of pings
            ScanResult sr = (ScanResult) data;
            AdamPing ap = new AdamPing(
                    sr.level,
                    am.GetLocation().getLatitude(),
                    am.GetLocation().getLongitude(),
                    am.GetLocation().getAltitude(),
                    am.GetLocation().getAccuracy()


            );
            pings.add(ap);
        }
    }
}
