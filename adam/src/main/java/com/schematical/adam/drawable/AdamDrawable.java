package com.schematical.adam.drawable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;

import com.schematical.adam.AdamView;

/**
 * Created by user1a on 10/4/13.
 */
abstract public class AdamDrawable {
    protected String id = null;
    protected final Paint paint;
    protected final Paint bg_paint;



    protected int height = 200;
    protected int width = 200;
    protected Integer top = null;
    protected Integer left = null;
    protected Integer right = null;
    protected Integer bottom = null;


    protected int zIndex = 0;
    protected int padding = 20;
    protected AdamView av;

    public AdamDrawable(AdamView nAv){
        av = nAv;
        av.AddChildControl(this);
        bg_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bg_paint.setColor(0x88000000);
        bg_paint.setStyle(Paint.Style.FILL);


        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xffffffff);
        paint.setTextSize(20);
        Typeface tf = Typeface.create("Massif Pro",Typeface.BOLD);
        paint.setTypeface(tf);
        paint.setStrokeWidth(4);



    }

    public void Draw(Canvas canvas){};
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

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }


    public int getX() {
        if((Integer)this.right != null){
            return av.GetCanvas().getWidth() - (this.right + this.width);
        }
        if((Integer)this.left != null){
            return this.left;
        }
        return 0;
    }
    public int getY() {
        if((Integer)this.bottom != null){
            return av.GetCanvas().getHeight() - (this.bottom + this.height);
        }
        if((Integer)this.top != null){
            return this.top;
        }
        return 0;
    }

    public void onTouch(View v, MotionEvent event) {

    }
    public void DrawBitmap(Canvas canvas, Bitmap bm){
        DrawBitmap(
            canvas,
            bm,
            this.getX(),
            this.getY(),
            width,
            height
        );
    }
    public void DrawBitmap(Canvas canvas, Bitmap bm, int x, int y){
        DrawBitmap(
            canvas,
            bm,
            x,
            y,
            bm.getWidth(),
            bm.getHeight()
        );
    }

    public void DrawBitmap(Canvas canvas, Bitmap bm, int nX, int nY, int nWidth, int nHeight){

        Bitmap nIcon = Bitmap.createScaledBitmap(av.icon,nWidth, nHeight, true);
        canvas.drawBitmap(
                nIcon,
                nX,
                nY,
                bg_paint
        );
    }


}
