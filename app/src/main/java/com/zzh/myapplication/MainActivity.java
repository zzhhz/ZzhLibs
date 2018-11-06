package com.zzh.myapplication;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.zzh.zlibs.camera.Camera21Activity;
import com.zzh.zlibs.utils.PermissionManager;
import com.zzh.zlibs.utils.thread.ZThreadManager;

import java.util.Arrays;

public class MainActivity extends BaseGitActivity {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    public void onClickView(View v) {
        checkPermissions();
    }

    public void onClickCheckPermission(View v) {
        /*PermissionManager.checkAndRequestMorePermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA}, 1000,
                new PermissionManager.PermissionRequestSuccessCallBack() {
                    @Override
                    public void onHasPermission() {
                        Log.d(TAG, "onHasPermission: 已经授予了权限");
                    }
                });*/
        PermissionManager.checkAndRequestMorePermissions(this, new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE

                }, 1000,
                new PermissionManager.PermissionRequestSuccessCallBack() {
                    @Override
                    public void onHasPermission() {
                        Log.d(TAG, "onHasPermission: 已经授予了权限");
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
        Intent in = new Intent(this, Camera21Activity.class);
        startActivity(in);
    }
}
