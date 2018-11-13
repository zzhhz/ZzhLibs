package com.zzh.zlibs.camera.preview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraDevice;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.zzh.zlibs.R;
import com.zzh.zlibs.base.BaseDataBindingActivity;
import com.zzh.zlibs.utils.FileUtils;
import com.zzh.zlibs.utils.ZUtils;

import java.io.File;
import java.io.IOException;

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
public class BaseCameraActivity extends BaseDataBindingActivity implements View.OnClickListener {
    public static final String EXTRA_OUTPUT_FILE = "zzh_output_file";
    protected SurfaceView mSurfaceView;
    protected SurfaceHolder mHolder;
    protected CameraDevice mCameraDevice;
    protected ImageView iv_preview_picture;
    protected ImageView iv_cancel;
    protected ImageView iv_confirm;
    protected String DEFAULT_PATH = ZUtils.getSDCardDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "zzh_";
    /**
     * 传入的数据;
     */

    public String mOutputFile;

    /**
     * 是照片竖着显示
     */
    protected static final SparseIntArray ORIENTATIONS = new SparseIntArray();

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
            takePictureCancel();
        } else if (v.getId() == R.id.iv_confirm) {
            takePictureOkFinish();
        }
    }

    /**
     * 拍照成功后，按下确定按钮
     */
    protected void takePictureOkFinish() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_OUTPUT_FILE, mOutputFile);
        setResult(RESULT_OK, intent);
    }

    /**
     * 拍照成功后，按下取消按钮
     */
    protected void takePictureCancel() {

    }

    /**
     * 按下快门，进行拍照
     */
    protected void takePicture() {

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

    /**
     * 保存图片
     *
     * @param images
     */
    public void savePicture(byte[] images) {
        String outputFile = mOutputFile;
        File output = null;
        if (TextUtils.isEmpty(outputFile)) {
            outputFile = DEFAULT_PATH + System.currentTimeMillis() + ".jpg";
            output = new File(outputFile);
        } else {
            output = new File(outputFile);
            if (output.exists()) {
                outputFile = DEFAULT_PATH + System.currentTimeMillis() + ".jpg";
                output = new File(outputFile);
            }

            try {
                output.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mOutputFile = outputFile;
        Log.d(TAG, "onPictureTaken: 保存图片成功: " + outputFile);
        Bitmap bitmap = BitmapFactory.decodeByteArray(images, 0, images.length);
        iv_preview_picture.setImageBitmap(bitmap);
        bitmap.recycle();
        bitmap = null;
        iv_cancel.setVisibility(View.VISIBLE);
        iv_confirm.setVisibility(View.VISIBLE);
        iv_preview_picture.setVisibility(View.VISIBLE);
        FileUtils.saveFile(images, output);
    }
}
