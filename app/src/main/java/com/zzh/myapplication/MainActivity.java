package com.zzh.myapplication;

import android.Manifest;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zzh.main.R;
import com.zzh.zlibs.image.model.FileItem;
import com.zzh.zlibs.utils.PermissionManager;
import com.zzh.zlibs.utils.ZUtils;
import com.zzh.zlibs.utils.thread.ZThreadManager;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import static com.zzh.zlibs.image.ImageGridActivity.DATA_ZZH_IMAGE;

public class MainActivity extends BaseGitActivity {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

        TextView tvw = (TextView) findViewById(R.id.tv_d_w);
        tvw.setText("屏幕宽度：" + String.valueOf(ZUtils.getDisplayWidth(this)) + "\n高度： " + String.valueOf(ZUtils.getDisplayHeight(this)));
        tvw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Main2Activity.class);
                startActivity(intent);
            }
        });


    }


    public void onClickView(View v) {
        checkPermissions();
    }

    public void onClickCheckPermission(View v) {
        PermissionManager.checkAndRequestMorePermissions(this, new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE

                }, 1000,
                new PermissionManager.PermissionRequestSuccessCallBack() {
                    @Override
                    public void onHasPermission() {
                        ZUtils.openCamera(mContext, getCacheDir()+"/"+System.currentTimeMillis()+".png");
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: " + requestCode + ", " + Arrays.toString(permissions) + ", " + Arrays.toString(grantResults) + ", " + PermissionManager.isPermissionRequestSuccess(grantResults));
    }

    public void onClickWriteView(View v) {
        requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
    }

    @Override
    protected void notifyPermission(int code, boolean flag) {
        Log.d(TAG, "notifyPermission: code: " + code + ",  flag:  " + flag);
    }

    private void checkPermissions() {

        int i = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean b = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean verifyGrantPermission = verifyGrantPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        Log.d(TAG, "checkPermissions  i: " + i);
        Log.d(TAG, "checkPermissions  b: " + b);
        Log.d(TAG, "checkPermissions  verifyGrantPermission: " + verifyGrantPermission);
    }

    /**
     * 线程测试
     *
     * @param v
     */
    public void onClickThread(View v) {

        ZThreadManager instance = new ZThreadManager.Builder().setFifo(false).build();

        for (int i = 0; i < 16; i++) {
            instance.execute(new TestRunnable(String.valueOf(i)));
        }
        Log.d(TAG, "onClickThread: -----------------------------------------------------------------");

    }

    class TestRunnable implements Runnable {
        private String name;

        public TestRunnable(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                Log.d(TAG, "Thread name run: " + Thread.currentThread().getName() + ", " + name + " , 线程执行完成");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void onClickTakePicture(View v) {
        ZUtils.openCamera(mContext, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult requestCode: " + requestCode + ", resultCode: " + resultCode + ", intent: " + parseIntent(data));
    }

    public String parseIntent(Intent intent) {
        if (intent == null) {
            return "";
        }
        if (intent.hasExtra(DATA_ZZH_IMAGE)) {
            StringBuilder builder = new StringBuilder();
            ArrayList<FileItem> extra = intent.getParcelableArrayListExtra(DATA_ZZH_IMAGE);
            if (extra != null) {
                for (FileItem item : extra) {
                    builder.append(item.getPath()).append(", ");
                }
            }
            return builder.toString();
        }
        return "";
    }
}
