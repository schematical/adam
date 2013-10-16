package com.schematical.adam.img;

import android.graphics.Bitmap;

import android.hardware.Camera;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;

import com.schematical.adam.old.AdamActivityMain;

/**
 * Created by user1a on 10/6/13.
 */
public class AdamImgRecDriver implements Camera.FaceDetectionListener {
    AdamActivityMain am;
    FaceDetector faceDetector;
    public AdamImgRecDriver(AdamActivityMain nAm){
        am = nAm;

    }
    public Face[] AnalyzePhoto(Bitmap bm){
        faceDetector = new FaceDetector(
                bm.getWidth(),
                bm.getHeight(),
                3
        );
        Face[] faces = new Face[0];
        faceDetector.findFaces(
                bm,
                faces
        );
        return faces;
    }

    public void WatchCamera(Camera mCamera) {
        mCamera.setFaceDetectionListener(
                this
        );
    }

    @Override
    public void onFaceDetection(Camera.Face[] faces, Camera camera) {
        if(faces.length > 0){
            for(int i = 0; i < faces.length; i++){

            }
            this.am.SetStatus("Found a face");
        }
    }
}
