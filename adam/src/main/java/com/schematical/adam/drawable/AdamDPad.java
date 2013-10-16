package com.schematical.adam.drawable;

import android.graphics.Canvas;

/**
 * Created by user1a on 10/6/13.
 */
public class AdamDPad extends AdamDrawable {

    protected AdamDPadKey dUp;
    protected AdamDPadKey dLeft;
    protected AdamDPadKey dRight;
    protected AdamDPadKey dDown;
    public AdamDPad(iAdamDrawable nAv) {
        super(nAv);
        height = 300;
        width = 300;
    }
    public void Draw(Canvas canvas){
        if(dUp != null){
            dUp.setLeft(this.getX() + this.getWidth() / 2 - dUp.getWidth() / 2);
            dUp.setTop(this.getY());
            dUp.Draw(canvas);
        }
        if(dDown != null){
            dDown.setLeft(this.getX() +  this.getWidth()/2 - dDown.getWidth()/2);
            dDown.setTop(this.getY() + this.getHeight() - dDown.getHeight());
            dDown.Draw(canvas);
        }



        if(dLeft != null){
            dLeft.setLeft(this.getX());
            dLeft.setTop(this.getY() + this.getHeight()/2 - dLeft.getWidth()/2);
            dLeft.Draw(canvas);
        }
        if(dRight != null){
            dRight.setLeft(this.getX() + this.getWidth() - dRight.getWidth());
            dRight.setTop(this.getY() + this.getHeight()/2 - dRight.getWidth()/2);
            dRight.Draw(canvas);
        }
    }
    public AdamDPadKey getDUp() {
        return dUp;
    }

    public void setDUp(AdamDPadKey dUp) {
        this.dUp = dUp;
    }

    public AdamDPadKey getDLeft() {
        return dLeft;
    }

    public void setDLeft(AdamDPadKey dLeft) {
        this.dLeft = dLeft;
    }

    public AdamDPadKey getDRight() {
        return dRight;
    }

    public void setDRight(AdamDPadKey dRight) {
        this.dRight = dRight;
    }

    public AdamDPadKey getDDown() {
        return dDown;
    }

    public void setDDown(AdamDPadKey dDown) {
        this.dDown = dDown;
    }

}
