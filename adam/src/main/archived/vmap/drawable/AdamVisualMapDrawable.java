package com.schematical.adam.vmap.drawable;

import android.graphics.Canvas;
import android.location.Location;

import com.schematical.adam.location.AdamLocationDriver;
import com.schematical.adam.sensors.AdamSensorDriver;
import com.schematical.adam.drawable.AdamStackablePercentField;
import com.schematical.adam.drawable.AdamStackableTextField;
import com.schematical.adam.drawable.AdamDrawable;
import com.schematical.adam.drawable.AdamIcon;
import com.schematical.adam.drawable.iAdamDrawable;
import com.schematical.adam.renderer.Adam2DPoint;
import com.schematical.adam.renderer.Adam3DEngine;
import com.schematical.adam.vmap.AdamVisualMap;
import com.schematical.adam.vmap.AdamVisualMapDriver;
import com.schematical.adam.vmap.AdamVisualMapPoint;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by user1a on 10/8/13.
 */
public class AdamVisualMapDrawable extends AdamDrawable {
    public AdamIcon cursor;
    public AdamStackablePercentField txtDistance;
    public AdamStackableTextField txtCoords;
    public AdamVisualMapPoint ap;
    public AdamVisualMapDrawable(iAdamDrawable nAv) {
        super(nAv);
        cursor = new AdamIcon(this, AdamIcon.building);
        txtDistance = new AdamStackablePercentField(cursor);
        txtDistance.setName("Distance");
        txtDistance.setUnits("m");
        txtDistance.setOutOf(1);
        try {
            Method m = AdamVisualMapDriver.class.getMethod("GetEstimatedDistanceToFloorPoint");
            txtDistance.Follow(AdamVisualMapDriver.class, m);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        txtCoords = new AdamStackableTextField(cursor);

        try {
            Method m = this.getClass().getMethod("GetCoords");
            txtCoords.Follow(this, m);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        AdamVisualMapDriver.InitMap();
    }
    public String GetCoords(){
        Double yaw = AdamSensorDriver.getCurrYaw();
        String txt = Math.round(ap.getX()) + "," + Math.round(ap.getY()) + "," + Math.round(ap.getZ()) + " : " + Math.round((AdamLocationDriver.GetBearing(0d, 0d, ap.getX(), ap.getY()) + yaw)/Math.PI*180) + " / " + Math.round(yaw/Math.PI*180);
        //AdamActivityMain.SendToServer(txt);
        return txt;
    }
    public void Draw(Canvas canvas){
        //For now lets just draw a an icon at the distance point
        ap = AdamVisualMapDriver.GetLocationOfFloorPoint();


        Location rp = ap.getGeoLocation(AdamLocationDriver.GetLocation());

        Adam2DPoint a2dPoint = Adam3DEngine.Get2DPos(canvas, rp);


        //if(a2dPoint.getTopY() < 0 ){
            cursor.setLeft(((Long) Math.round(a2dPoint.getX())).intValue());
            cursor.setTop(((Long)Math.round(a2dPoint.getY())).intValue());
            //cursor.setHeight(((Long)Math.round(a2dPoint.getScale() * 140)).intValue());
            cursor.Draw(canvas);
        //}

        AdamVisualMap map = AdamVisualMapDriver.getMap();
        ArrayList<AdamVisualMapPoint> points = map.getPoints();
        Adam2DPoint a2dPoint_2 = null;
        for(int i = 0; i < points.size(); i++){
            AdamVisualMapPoint point = points.get(i);
            rp = point.getGeoLocation(AdamLocationDriver.GetLocation());
            a2dPoint_2 = Adam3DEngine.Get2DPos(canvas, rp);

            Float x1 = a2dPoint.getX().floatValue();
            Float y1 = a2dPoint.getY().floatValue();
            Float x2 = a2dPoint_2.getX().floatValue();
            Float y2 = a2dPoint_2.getY().floatValue();
            canvas.drawLine(
                x1,
                y1,
                x2,
                y2,
                paint
            );
            a2dPoint =a2dPoint_2;
        }

    }
}
