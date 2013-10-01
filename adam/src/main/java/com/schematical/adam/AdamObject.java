package com.schematical.adam;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by user1a on 10/1/13.
 */
public class AdamObject {
    private final Paint paint;
    protected String id;
    protected int goalX = 0;
    protected int goalY = 0;
    protected int currX = 0;
    protected int currY = 0;
    AdamObject(){
        id = "test";
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
}
