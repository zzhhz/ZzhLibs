package com.zzh.zlibs.camera.preview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.zzh.zlibs.R;
import com.zzh.zlibs.base.BaseDataBindingActivity;
import com.zzh.zlibs.utils.FileUtils;
import com.zzh.zlibs.utils.PermissionManager;
import com.zzh.zlibs.utils.ZUtils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by Administrator.
 *
 * @date: 2018/11/8
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description: ZzhLibs
 * @since 1.0
 */
public class BaseCameraActivity extends BaseDataBindingActivity implements View.OnClickListener{
    public static final String EXTRA_OUTPUT_FILE = "zzh_output_file";
    protected SurfaceView mSurfaceView;
    protected SurfaceHolder mHolder;
    protected CameraDevice mCameraDevice;
    protected ImageView iv_preview_picture;
    protected ImageView iv_cancel;
    protected ImageView iv_confirm;
    /**
     * 传入的数据;
     */

    public String mOutputFile;

    /**
     * 是照片竖着显示
     */
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zzh_camera);
        initView();
    }

    private void initView() {
        mSurfaceView = findViewById(R.id.sv_camera);
        iv_preview_picture = findViewById(R.id.iv_preview_picture);
        iv_cancel = findViewById(R.id.iv_cancel);
        iv_confirm = findViewById(R.id.iv_confirm);
        iv_cancel.setOnClickListener(this);
        iv_confirm.setOnClickListener(this);
        findViewById(R.id.iv_take_picture).setOnClickListener(this);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_OUTPUT_FILE)) {
            mOutputFile = intent.getStringExtra(EXTRA_OUTPUT_FILE);
        }
        mHolder = mSurfaceView.getHolder();
        mHolder.setKeepScreenOn(true);
    }
    @Override
    protected void handlerMessage(Message msg) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_take_picture) {
            takePicture();
        } else if (v.getId() == R.id.iv_cancel) {
            releaseImageViewResource(iv_preview_picture);
            iv_confirm.setVisibility(View.GONE);
            iv_cancel.setVisibility(View.GONE);
            iv_preview_picture.setVisibility(View.GONE);
        } else if (v.getId() == R.id.iv_confirm) {
            setResult(RESULT_OK);
            finish();
        }
    }

    protected void takePicture() {

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

    public void releaseImageViewResource(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }
}
