package com.example.a2monkr41.android_camera;

import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Camera camera;
    FrameLayout frameLayout;
    CameraPreview cameraPreview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = (FrameLayout)findViewById(R.id.frameLayout);

        camera = Camera.open();

        cameraPreview = new CameraPreview(this,camera);
        frameLayout.addView(cameraPreview);

    }

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile();

            if (pictureFile == null) {
                return;
            }

            else {
                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();

                    camera.startPreview();

                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private File getOutputMediaFile() {
        String state = Environment.getExternalStorageState();
        if(!state.equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }
        else {
            File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "Gallery");

            if(!folder.exists()) {
                folder.mkdirs();
            }

            File fileOutput = new File(folder,"capturedImage.jpg");
            return  fileOutput;
        }
    }

    public void captureImage(View v) {
        if(camera!=null) {
            camera.takePicture(null,null,mPictureCallback);
        }
    }
}
