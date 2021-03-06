package com.schematical.adam.drawable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;

import java.util.Hashtable;

/**
 * Created by user1a on 10/4/13.
 */
abstract public class AdamDrawable implements iAdamDrawable{
    protected String id = null;
    protected final Paint paint;
    protected final Paint bg_paint;


    protected AdamAction action;
    protected Object actionParameter;

    protected int height = 200;
    protected int width = 200;
    protected Integer top = null;
    protected Integer left = null;
    protected Integer right = null;
    protected Integer bottom = null;


    protected int zIndex = 0;
    protected int padding = 20;
    protected AdamView av;



    protected AdamDrawable objParent;
    protected Hashtable<String, AdamDrawable> children = new Hashtable<String, AdamDrawable>();

    public AdamDrawable(iAdamDrawable nAv){
        if(nAv instanceof AdamView){
            av = (AdamView)nAv;
            av.AddChildControl(this);
        }else if(nAv instanceof AdamDrawable){
            av = ((AdamDrawable) nAv).getAv();
            av.AddChildControl(this);

            objParent = (AdamDrawable)nAv;
            objParent.AddChildControl(this);
        }

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

    public void setLeft(Integer left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(Integer right) {
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
       if(action != null){
           action.Exicute(this.actionParameter, this);
       }
    }
    public void DrawBitmap(Canvas canvas, Bitmap bm){
        if(width < 1 || height < 1){
            return;
        }
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


    public void DrawIcon(Canvas canvas, Character icon, long nX, long nY){

        Typeface oTf = paint.getTypeface();
        Typeface tf= Typeface.createFromAsset(av.getContext().getAssets(), "font/fontawesome-webfont.ttf");
        paint.setTypeface(tf);

        canvas.drawText(
            icon.toString(),
            nX,
            nY,
            paint
        );
        paint.setTypeface(oTf);
    }

    public void setAction(AdamAction nAction) {
        action = nAction;
    }

    public AdamView getAv() {
        return av;
    }
    public void AddChildControl(AdamDrawable child){
        this.children.put(child.getId(), child);
    }
    public AdamDrawable getObjParent() {
        return objParent;
    }

    public void InvertColor() {
        int color = paint.getColor();
        int bg_color = bg_paint.getColor();
        bg_paint.setColor(color);
        paint.setColor(bg_color);
    }
}
