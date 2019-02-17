package com.mobilcontrol.dev.projectslave;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;

public class CameraSnapshot {
    public CameraSnapshot(){
        int index = getFrontCameraId();
        if (index == -1) {
            return;
        }

        final Camera mCamera;

        try {
            mCamera = Camera.open(index);
        }
        catch(Exception ex){
            return;
        }

        Camera.PictureCallback mCall = new Camera.PictureCallback()
        {
            @Override
            public void onPictureTaken(byte[] data, Camera camera)
            {
                mCamera.stopPreview();
                mCamera.release();

                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                onSnapshotTaken(bmp);
            }
        };

        mCamera.takePicture(null, null, mCall);
    }

    private int getFrontCameraId(){
        Camera.CameraInfo ci = new Camera.CameraInfo();
        for (int i = 0 ; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, ci);
            if (ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) return i;
        }
        return -1; // No front-facing camera found
    }

    public void onSnapshotTaken(Bitmap bmp){

    }
}
