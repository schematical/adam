package com.schematical.adam.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by user1a on 10/6/13.
 */
public class AdamStackablePercentField extends AdamStackable{
    protected Object obj;
    protected Method method;


    protected String units = "%";


    protected String name;
    public AdamStackablePercentField(iAdamDrawable nAv) {
        super(nAv);



    }
    public void Follow(Object nObj, Method nMethod){
        obj = nObj;
        method = nMethod;
    }
    public void Draw(Canvas canvas){
        int x = this.getX();
        int y = this.getY();
        canvas.drawRect(
            x,
            y,
            x + this.getWidth(),
            y + this.getHeight(),
            bg_paint
        );

        Double dResult = 0d;
        if(
            (method != null) &&
            (obj != null)
        ){
            try {
                dResult = (Double) method.invoke(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        Double dWidthOffset = dResult;
        if(dResult.equals(Double.POSITIVE_INFINITY)){
            dWidthOffset = 0d;
        }else if(dResult > 1){
            dWidthOffset = 1d;
        }
        Paint sPaint = new Paint();
        sPaint.setColor(0xff666666);

        canvas.drawRect(
            x,
            y,
            x + Math.round(this.getWidth() * dWidthOffset),
            y + this.getHeight(),
            sPaint
        );
        String txt = "";
        if(name != null){
            txt += name + ": ";
        }

        if(dResult.equals(Double.POSITIVE_INFINITY)){
            txt = "N/A";
        }else{
            txt += ((Long)Math.round(dResult * 100)).toString() + this.units;
        }
        canvas.drawText(
            txt,
            x + this.padding,
            y + this.padding,
            paint
        );
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

}
