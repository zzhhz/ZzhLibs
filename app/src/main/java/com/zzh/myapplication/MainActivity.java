package com.zzh.myapplication;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

public class MainActivity extends BaseGitActivity {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    public void onClickView(View v) {
        checkPermissions();
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
}
