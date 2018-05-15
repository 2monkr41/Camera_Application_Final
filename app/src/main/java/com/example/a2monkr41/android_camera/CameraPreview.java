package com.example.a2monkr41.android_camera;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * Created by 2monkr41 on 19/04/2018.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{

    Camera camera;
    SurfaceHolder holder;

    public CameraPreview(Context context,Camera camera) {
        super(context);
        this.camera = camera;
        holder = getHolder();
        holder.addCallback(this);

    }

    public void surfaceCreated(SurfaceHolder holder) {

        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch(Exception e) {
            Log.d("cameraApp", e.toString());
        }

    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            camera.stopPreview();
            camera.release();
        } catch(Exception e) {
            Log.d("cameraApp", e.toString());
        }
    }

    public void surfaceChanged(SurfaceHolder holder,int format, int w, int h)
    {
        if(camera!=null)
        {
            boolean isPortrait =  getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
            Camera.Parameters p= camera.getParameters();
            Camera.Size s = this.getClosestSize(p,w,h);
            if (s!=null) {
                camera.stopPreview();
                p.setPreviewSize(s.width, s.height);
                camera.setParameters(p);
                camera.startPreview();

                try {
                    camera.setPreviewDisplay(this.getHolder());
                } catch (IOException e) {
                    Log.e("camera app", "Error setting preview display: " + e);

                }

                // Have to rotate if portrait
                if(isPortrait) {
                    camera.setDisplayOrientation(90);
                }
            }
        }
    }

    private Camera.Size getClosestSize(Camera.Parameters p,int w, int h) {

        boolean isPortrait =  getResources().getConfiguration().orientation == 	Configuration.ORIENTATION_PORTRAIT;

        Camera.Size s = null;
        List<Camera.Size> sizes = p.getSupportedPreviewSizes();
        int mindw=Integer.MAX_VALUE, dw;
        double curRatio, aspectRatio = (double)w/(double)h, dratio, minDiffRatio = Double.MAX_VALUE;

        for(int i=0; i<sizes.size(); i++) {

            dw = Math.abs(sizes.get(i).width-w);
            int realWidth = isPortrait ? sizes.get(i).height: sizes.get(i).width;
            if(isPortrait)
            {
                realWidth = sizes.get(i).height;
            }
            else
            {
                realWidth = sizes.get(i).width;
            }
            int realHeight = isPortrait ? sizes.get(i).width: sizes.get(i).height;

            curRatio = ((double)realWidth) / ((double)realHeight);
            dratio = Math.abs(curRatio-aspectRatio);

            if(dratio-0.0001 <= minDiffRatio ) {
                minDiffRatio  = dratio;

                if(dw < mindw) {
                    mindw = dw;

                    s = sizes.get(i);
                }
            }
        }

        return s;
    }


}





