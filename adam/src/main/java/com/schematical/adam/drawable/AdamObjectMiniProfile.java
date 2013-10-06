package com.schematical.adam.drawable;

import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import com.schematical.adam.AdamObject;
import com.schematical.adam.AdamView;

/**
 * Created by user1a on 10/6/13.
 */
public class AdamObjectMiniProfile  extends AdamDrawable{
    public static final int MAIN_WINDOW_WIDTH = 150;
    public static final int MAIN_WINDOW_HEIGHT = 150;

    //private final AdamObject adamObject;

    public AdamObjectMiniProfile(AdamView nAv){//, AdamObject nAdamObject) {
        super(nAv);
        //adamObject = nAdamObject;
    }
    public void Draw(Canvas canvas){
        AdamObject adamObject = this.av.GetFocus();
        if(adamObject == null){
            return;
        }
        canvas.drawRect(
                this.getX(),
                this.getY(),
                MAIN_WINDOW_WIDTH,
                MAIN_WINDOW_HEIGHT,
                bg_paint
        );
        paint.setTextSize(MAIN_WINDOW_HEIGHT/2);
        DrawIcon(
            canvas,
            adamObject.GetIcon(),
            (this.getX() + Math.round(MAIN_WINDOW_WIDTH * .33)),
            (this.getY() + Math.round(MAIN_WINDOW_HEIGHT * .33))
        );

    }
}
