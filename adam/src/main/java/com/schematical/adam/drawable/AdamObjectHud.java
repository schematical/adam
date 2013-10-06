package com.schematical.adam.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Location;

import com.schematical.adam.AdamActivityMain;
import com.schematical.adam.AdamObject;
import com.schematical.adam.AdamView;
import com.schematical.adam.R;

/**
 * Created by user1a on 10/5/13.
 */
public class AdamObjectHud extends AdamDrawable {
    protected int goalX = 0;
    protected int goalY = 0;
    protected int currX = 0;
    protected int currY = 0;

    AdamObject ao;

    public AdamObjectHud(AdamView nAv, AdamObject nAo) {
        super(nAv);
        this.ao = nAo;
    }

    public void Draw(Canvas canvas){
        Location newLocation = new Location("gps");
        Location oLocation = ((AdamActivityMain)av.getContext()).GetLocation();
        newLocation.setLatitude(ao.GetLat());
        newLocation.setLongitude(ao.GetLng());
        newLocation.setAltitude(oLocation.getAltitude());

        newLocation.setAccuracy(3.0f);
        float cDist = oLocation.distanceTo(newLocation);

        currX = Math.round(currX + goalX)/2;
        currY = Math.round(currY + goalY)/2;


        canvas.drawBitmap(
                av.icon,
                currX,
                currY,
                bg_paint
        );

        //paint.setTextSize(Math.round(40/cDist));
        Typeface tf= Typeface.createFromAsset(av.getContext().getAssets(),"font/fontawesome-webfont.ttf");
        paint.setTypeface(tf);
        paint.setColor(0xffffffff);
        paint.setTextSize(20);
        StringBuilder sb = new StringBuilder();

        canvas.drawText(
                String.valueOf('\uf09e'),
                currX,
                currY,
                paint
        );
        /*
        canvas.drawText(
                ao.GetAlias(),
                currX,
                currY,
                paint
        );

        canvas.drawText(
                "Dist:" + Float.toString(cDist),
                currX,
                currY + 45,
                paint
        );*/

    }
    public void SetGoalXY(int nGoalX, int nGoalY){
        goalX = nGoalX;
        goalY = nGoalY;
    }
}