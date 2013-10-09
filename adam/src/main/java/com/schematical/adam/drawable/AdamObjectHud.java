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
public class AdamObjectHud extends AdamIcon {
    protected int goalX = 0;
    protected int goalY = 0;
    protected int currX = 0;
    protected int currY = 0;

    public AdamObject getAdamObject() {
        return ao;
    }

    AdamObject ao;

    public AdamObjectHud(iAdamDrawable nAv, AdamObject nAo) {
        super(nAv, nAo.GetIcon());
        this.ao = nAo;
    }
    public void Draw(Canvas canvas){
        currX = Math.round(currX + goalX)/2;
        currY = Math.round(currY + goalY)/2;

        super.Draw(canvas);
    }

    public int getX(){
        return currX;
    }
    public int getY(){
        return currY;
    }
    public void Hide(){
        goalX = -200;
        goalY = -200;
        currX = -200;
        currY = -200;
    }

    public void SetGoalXY(int nGoalX, int nGoalY, Double nScale){
        goalX = nGoalX;
        goalY = nGoalY;
        this.height = ((Long)Math.round(140 * nScale)).intValue();
        this.width = ((Long)Math.round(140 * nScale)).intValue();

    }
}
