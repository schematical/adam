package com.schematical.adam.vmap.drawable;

import android.graphics.Canvas;
import android.location.Location;
import android.nfc.Tag;
import android.util.Log;

import com.schematical.adam.AdamSensorDriver;
import com.schematical.adam.drawable.AdamStackablePercentField;
import com.schematical.adam.drawable.AdamStackableTextField;
import com.schematical.adam.location.AdamLocation;
import com.schematical.adam.drawable.AdamDrawable;
import com.schematical.adam.drawable.AdamIcon;
import com.schematical.adam.drawable.iAdamDrawable;
import com.schematical.adam.renderer.Adam2DPoint;
import com.schematical.adam.renderer.Adam3DEngine;
import com.schematical.adam.vmap.AdamVisualMapDriver;
import com.schematical.adam.vmap.AdamVisualMapPoint;

import java.lang.reflect.Method;

/**
 * Created by user1a on 10/8/13.
 */
public class AdamVisualMapDrawable extends AdamDrawable {
    public AdamIcon cursor;
    public AdamStackablePercentField txtDistance;
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
    }
    public void Draw(Canvas canvas){
        //For now lets just draw a an icon at the distance point
        AdamVisualMapPoint ap = AdamVisualMapDriver.GetLocationOfFloorPoint();

        //Log.d("adam", "x:" + ap.getX() + "  y: " + ap.getY() + " z: " + ap.getZ());
        Location rp = ap.getGeoLocation(AdamLocation.GetLocation());

        Adam2DPoint a2dPoint = Adam3DEngine.Get2DPos(canvas, rp);

        //Log.d("adam", a2dPoint.getX() + "/" + a2dPoint.getY());

        cursor.setLeft(((Long) Math.round(a2dPoint.getX())).intValue());
        cursor.setTop(((Long)Math.round(a2dPoint.getY())).intValue());
        //cursor.setHeight(((Long)Math.round(a2dPoint.getScale() * 140)).intValue());
        cursor.Draw(canvas);


    }
}
