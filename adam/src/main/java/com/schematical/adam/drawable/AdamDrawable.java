package com.schematical.adam.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.schematical.adam.AdamView;

/**
 * Created by user1a on 10/4/13.
 */
public class AdamDrawable {

    protected final Paint paint;
    protected int height = 200;
    protected int width = 200;
    protected int padding = 20;
    protected AdamView av;

    public AdamDrawable(AdamView nAv){
        av = nAv;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xff00ff00);
        paint.setTextSize(20);
        paint.setStrokeWidth(4);


    }

}
