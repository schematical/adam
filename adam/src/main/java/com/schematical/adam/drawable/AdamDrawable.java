package com.schematical.adam.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.schematical.adam.AdamView;

/**
 * Created by user1a on 10/4/13.
 */
public class AdamDrawable {
    protected String id = null;
    protected final Paint paint;



    protected int height = 200;
    protected int width = 200;
    protected int x = 0;
    protected int y = 0;
    protected int zIndex = 0;
    protected int padding = 20;
    protected AdamView av;

    public AdamDrawable(AdamView nAv){
        av = nAv;
        av.AddChildControl(this);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xff00ff00);
        paint.setTextSize(20);
        paint.setStrokeWidth(4);



    }

    public String getId() {
        return this.id;
    }
    public void setId(String nId){
        this.id = nId;
    }
    public int getZIndex() {
        return zIndex;
    }
    public void setZIndex(int nZIndex){
        this.zIndex = nZIndex;
    }
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
