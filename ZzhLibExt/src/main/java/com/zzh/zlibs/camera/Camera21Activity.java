package com.zzh.zlibs.camera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Toast;

import com.zzh.zlibs.camera.preview.BaseCameraActivity;
import com.zzh.zlibs.utils.PermissionManager;
import com.zzh.zlibs.utils.ZUtils;

import java.nio.ByteBuffer;
import java.util.Arrays;

import androidx.annotation.NonNull;

/**
 * Created by Administrator.
 *
 * @date: 2018/11/6
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description: ZzhLibs
 * @since 1.0
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class Camera21Activity extends BaseCameraActivity {
    private Handler childHandler;
    private String mCameraID;
    private ImageReader mImageReader;
    private CameraManager mCameraManager;
    private CameraCaptureSession mCameraCaptureSession;
    /**
     * 传入的数据;
     */
    public String mOutputFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_OUTPUT_FILE)) {
            mOutputFile = intent.getStringExtra(EXTRA_OUTPUT_FILE);
        }
        mHolder = mSurfaceView.getHolder();
        mHolder.setKeepScreenOn(true);
        mHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    faceCamera = CameraCharacteristics.LENS_FACING_FRONT;
                    initCamera();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    @SuppressLint("MissingPermission")
    private void initCamera() throws CameraAccessException {
        HandlerThread handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        childHandler = new Handler(handlerThread.getLooper());
        mCameraID = String.valueOf(faceCamera);
        mImageReader = ImageReader.newInstance(ZUtils.getDisplayHeight(this), ZUtils.getDisplayWidth(this), ImageFormat.JPEG, 1);
        mCameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                Image image = reader.acquireNextImage();
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] images = new byte[buffer.remaining()];
                buffer.get(images);
                savePicture(images, true);
                image.close();
            }
        }, mHandler);

        if (PermissionManager.checkPermission(this, Manifest.permission.CAMERA)) {
            if (PermissionManager.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                mCameraManager.openCamera(mCameraID, stateCallback, mHandler);
            } else {
                throw new SecurityException("------zzh----需要使用存储权限---android.permission.WRITE_EXTERNAL_STORAGE----");
            }
        } else {
            throw new SecurityException("------zzh----需要使用相机权限---android.permission.CAMERA----");
        }


    }

    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mCameraDevice = camera;
            gonPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            showMessage("打开相机失败，错误码：" + error);

        }
    };

    @Override
    protected void changeCamera() {
        if (faceCamera == CameraCharacteristics.LENS_FACING_FRONT) {
            faceCamera = CameraCharacteristics.LENS_FACING_BACK;
        } else {
            faceCamera = CameraCharacteristics.LENS_FACING_FRONT;
        }
        closeCamera();
        openCamera();
    }

    @SuppressLint("MissingPermission")
    private void openCamera() {
        try {
            initCamera();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void closeCamera() {
        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
        if (mCameraCaptureSession != null) {
            mCameraCaptureSession.close();
            mCameraCaptureSession = null;
        }
    }


    /**
     * 打开预览
     */
    private void gonPreview() {
        try {
            // 创建预览需要的CaptureRequest.Builder
            final CaptureRequest.Builder previewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            // 将SurfaceView的surface作为CaptureRequest.Builder的目标
            previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            previewRequestBuilder.addTarget(mHolder.getSurface());
            // 创建CameraCaptureSession，该对象负责管理处理预览请求和拍照请求
            mCameraDevice.createCaptureSession(Arrays.asList(mHolder.getSurface(), mImageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    if (null == mCameraDevice) return;
                    // 当摄像头已经准备好时，开始显示预览
                    mCameraCaptureSession = cameraCaptureSession;
                    try {
                        // 自动对焦
                        previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                        // 打开闪光灯
                        previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                        // 显示预览
                        CaptureRequest previewRequest = previewRequestBuilder.build();
                        mCameraCaptureSession.setRepeatingRequest(previewRequest, null, childHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(mContext, "配置失败", Toast.LENGTH_SHORT).show();
                }
            }, childHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void takePictureCancel() {
        releaseImageViewResource(iv_preview_picture);
        iv_preview_picture.setVisibility(View.GONE);
    }

    /**
     * 拍照
     */
    @Override
    protected void takePicture() {
        if (mCameraDevice == null) return;
        // 创建拍照需要的CaptureRequest.Builder
        final CaptureRequest.Builder captureRequestBuilder;
        try {
            captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            // 将imageReader的surface作为CaptureRequest.Builder的目标
            captureRequestBuilder.addTarget(mImageReader.getSurface());
            // 自动对焦
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            // 自动曝光
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            // 获取手机方向
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            // 根据设备方向计算设置照片的方向
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
            //拍照
            CaptureRequest mCaptureRequest = captureRequestBuilder.build();
            mCameraCaptureSession.capture(mCaptureRequest, new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    captureRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_CANCEL);
                }
            }, childHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handlerMessage(Message msg) {

    }

    @Override
    protected void onDestroy() {
        releaseImageViewResource(iv_preview_picture);
        if (null != mCameraDevice) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
        super.onDestroy();
    }
}
