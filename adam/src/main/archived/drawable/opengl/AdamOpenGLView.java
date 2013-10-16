package com.schematical.adam.drawable.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * Created by user1a on 10/6/13.
 */
public class AdamOpenGLView extends GLSurfaceView {
    AdamOpenGLRenderer mRenderer;


    public AdamOpenGLView(Context context){
        super(context);
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new AdamOpenGLRenderer();
        setRenderer(mRenderer);
    }
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;


                mRenderer.mXChange = dx;



                mRenderer.mYChange = dy ;



                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

}
