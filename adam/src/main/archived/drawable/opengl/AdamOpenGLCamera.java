package com.schematical.adam.drawable.opengl;

import android.opengl.Matrix;
import android.os.SystemClock;

/**
 * Created by user1a on 10/7/13.
 * TODO: Extend to special world camera
 * Follow - mVMatrix = AdamSensorDriver.rotationMatrix;
 *
 * And follow lat lng
 */
public class AdamOpenGLCamera implements iAdamOpenGLDrawable {
    protected AdamOpenGLDrawable objFollow = null;

    private int rmOffset;

    private float eyeX;
    private float eyeY;
    private float eyeZ;

    private float centerX;
    private float centerY;
    private float centerZ;
    private float upX;
    private float upY;
    private float upZ;
    public AdamOpenGLCamera(){
        rmOffset = 0;
        eyeX = 0;
        eyeY = 0;
        eyeZ = 0;
        centerX = 0f;
        centerY = 0f;
        centerZ = 0f;
        upX = 0f;
        upY  = 1.0f;
        upZ = 0.0f;
    }
    public void Follow( AdamOpenGLDrawable nFollow){
        objFollow = nFollow;
    }

    public float[] Update(){
        if(objFollow != null){
            centerX = objFollow.getX();
            centerY = objFollow.getY();
            centerZ = objFollow.getZ();
            /*eyeX = objFollow.getX();
            eyeY = objFollow.getY();
            eyeZ = objFollow.getZ() - 3;*/
        }
        eyeX -= .01;
        float[] rotationalMatrix = new float[16];
        Matrix.setIdentityM(rotationalMatrix,0);
        Matrix.setLookAtM(
                rotationalMatrix,
                0,
                eyeX,
                eyeY,
                eyeZ,
                centerX,
                centerY,
                centerZ,
                upX,
                upY,
                upZ
        );


        return rotationalMatrix;
    }
    public float getEyeX() {
        return eyeX;
    }

    public void setEyeX(float eyeX) {
        this.eyeX = eyeX;
    }

    public float getEyeY() {
        return eyeY;
    }

    public void setEyeY(float eyeY) {
        this.eyeY = eyeY;
    }

    public float getEyeZ() {
        return eyeZ;
    }

    public void setEyeZ(float eyeZ) {
        this.eyeZ = eyeZ;
    }
}
