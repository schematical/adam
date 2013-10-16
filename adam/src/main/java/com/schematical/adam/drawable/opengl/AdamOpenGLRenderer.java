package com.schematical.adam.drawable.opengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.schematical.adam.sensors.AdamSensorDriver;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by user1a on 10/6/13.
 */
public class AdamOpenGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "ADAM";
    private AdamOpenGLIcon mIcon;

    private float[] mMVPMatrix = new float[16];
    private float[] mProjMatrix = new float[16];
    private float[] mVMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];
    private AdamOpenGLCamera mCamera;

    // Declare as volatile because we are updating it from another thread
    public volatile float mAngle;
    public float mXChange = 0;
    public float mYChange = 0;

    @Override
    public void onSurfaceCreated(GL10 gl, javax.microedition.khronos.egl.EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0f, 0f, 0f, 1.0f);

        mCamera =  new AdamOpenGLCamera();//new AdamOpenGLARCamera();
        mCamera.setEyeZ(-6);

        // initialize a square
        mIcon = new AdamOpenGLIcon();
        //mCamera.Follow(mIcon);
    }

    public void onDrawFrame(GL10 gl) {


        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Set the camera position (View matrix)
       Matrix.setLookAtM(mVMatrix, 0, 0, 0, -6, -2f, 0f, 0f, 0f, 1.0f, 0.0f);
        //mVMatrix = mCamera.Update();
        Matrix.setIdentityM(mRotationMatrix, 0);

        /*set default camera, 2 units of the ground,
        with the up vector pointing positively on the y axis.*/

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, AdamSensorDriver.rotationMatrix, 0);

        mIcon.UpdateAndDraw(mMVPMatrix);
      /*  Matrix.translateM(mRotationMatrix, 0, -mXChange, -mYChange, 0);
        // Combine the rotation matrix with the projection and camera view
        Matrix.multiplyMM(mMVPMatrix, 0, mRotationMatrix, 0, mMVPMatrix, 0);
        // Draw triangle*/
    }



    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);


        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        //Matrix.frustumM(mProjMatrix, 0, -100, 100, -100, 100, 3, 100);
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
}