package com.schematical.adam.drawable;

import android.graphics.Canvas;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by user1a on 10/6/13.
 */
public class AdamStackableTextField extends AdamStackable{
    protected Object obj;
    protected Method method;
    public AdamStackableTextField(iAdamDrawable nAv) {
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

        String txt = "Data Unavailable";
        if(
            (method != null) &&
            (obj != null)
        ){
            try {
                txt = (String) method.invoke(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        canvas.drawText(
            txt,
            x + this.padding,
            y + this.padding,
            paint
        );
    }
}
