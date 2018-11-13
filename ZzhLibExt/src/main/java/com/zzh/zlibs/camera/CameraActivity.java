package com.zzh.zlibs.camera;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;

import com.zzh.zlibs.camera.preview.BaseCameraActivity;
import com.zzh.zlibs.utils.FileUtils;
import com.zzh.zlibs.utils.ZUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator.
 *
 * @date: 2018/11/13
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description: ZzhLibs
 * @since 1.0
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class CameraActivity extends BaseCameraActivity implements SurfaceHolder.Callback {

    private Camera mCamera;
    /**
     * 传入的数据;
     */
    public String mOutputFile;
    private int cameras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_OUTPUT_FILE)) {
            mOutputFile = intent.getStringExtra(EXTRA_OUTPUT_FILE);
        }
        initViews();
    }

    private void initViews() {
        mHolder = mSurfaceView.getHolder();
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mHolder.setKeepScreenOn(true);
        mHolder.addCallback(this);
    }

    private void initCamera() {

        if (mCamera == null) {
            cameras = Camera.getNumberOfCameras();
            if (cameras < 1) {
                throw new ExceptionInInitializerError("-------初始化相机失败，获取不到相机数量------");
            }
            openCamera();
            try {
                mCamera.setPreviewDisplay(mHolder);
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setPictureSize(1920, 1080);
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                parameters.setJpegQuality(90);
                parameters.setPictureFormat(PixelFormat.JPEG);
                mCamera.setParameters(parameters);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 默认打开后置摄像头
     */
    private void openCamera() {
        try {
            mCamera = Camera.open(0);
            android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
            android.hardware.Camera.getCameraInfo(0, info);
            int rotation = getWindowManager().getDefaultDisplay().getRotation();

            int degrees = 0;
            switch (rotation) {
                case Surface.ROTATION_0:
                    degrees = 0;
                    break;
                case Surface.ROTATION_90:
                    degrees = 90;
                    break;
                case Surface.ROTATION_180:
                    degrees = 180;
                    break;
                case Surface.ROTATION_270:
                    degrees = 270;
                    break;
            }
            int result;
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                result = (info.orientation + degrees) % 360;
                result = (360 - result) % 360;  // compensate the mirror
            } else {  // back-facing
                result = (info.orientation - degrees + 360) % 360;
            }
            mCamera.setDisplayOrientation(result);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        mHolder = null;
        mSurfaceView = null;
    }

    @Override
    protected void takePicture() {
        if (mCamera != null) {
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success) {
                        camera.takePicture(null, null, jpeg);
                    }
                }
            });
        }
    }

    @Override
    protected void takePictureCancel() {
        releaseImageViewResource(iv_preview_picture);
        iv_preview_picture.setVisibility(View.GONE);
        mCamera.startPreview();
    }

    Camera.PictureCallback jpeg = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] images, Camera camera) {
            savePicture(images);
        }
    };
}
