package com.zzh.zlibs.base;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zzh.zlibs.swipe.activity.SwipeBackActivity;

/**
 * Created by zzh on 2016/1/29.
 * 对BaseActivity的一些封装：<br />
 * 全局Context<br />
 * Handler<br />
 * Toast<br />
 * 初始化View控件<br />
 * 初始化数据<br />
 * 给控件设置监听事件<br />
 * 滑动退出当前页面<br />
 * 广播没有封装，因为可以用第三方库,例如EventBus.
 */
public abstract class BaseActivity extends SwipeBackActivity implements View.OnClickListener {
    protected static String TAG;
    protected Context mContext;
    protected BaseHandler mHandler;
    protected Toast mToast;
    protected Toolbar mToolbar;
    protected TextView toolBarTitle;
    //权限
    protected static final int REQUEST_CODE_READ_PERMISSION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getLocalClassName();
        mContext = this;
        if (mHandler == null)
            mHandler = new BaseHandler();
        int layoutResId = setLayoutId();
        setContentView(layoutResId);
        init();
    }

    protected abstract int setLayoutId();

    protected void toolbars(int toolbarId, int resId, String title, Toolbar.OnClickListener clickListener){
        setToolbar(toolbarId);
        toolbars(title,resId,clickListener);
    }
    protected void toolbars(Toolbar toolbar, int resId, String title, Toolbar.OnClickListener clickListener){
        setToolbar(toolbar);
        toolbars(title,resId,clickListener);
    }

    //设置Toolbar
    protected void toolbars(String title){
        toolbars(title, null);
    }

    public void setToolbar(int toolbarId) {
        this.mToolbar = (Toolbar) findViewById(toolbarId);
    }

    public void setToolbar(Toolbar mToolbar) {
        this.mToolbar = mToolbar;
    }

    public Toolbar getToolbar(){
        return mToolbar;
    }

    protected void toolbars(String title, Toolbar.OnClickListener clickListener){
        toolbars(title, -1, clickListener);
    }

    protected void toolbars(String title, int ic_back,Toolbar.OnClickListener clickListener) {
        try {
            mToolbar = getToolbar();
        } catch (Exception ex) {
            Log.e(TAG, "-----没有设置toolbar");
        }
        if (mToolbar == null)
            return;
        if (ic_back > 0)
            mToolbar.setNavigationIcon(ic_back);
        if (title != null) {
            mToolbar.setTitle(title);
        }
        setSupportActionBar(mToolbar);
        if (clickListener == null) {
            mToolbar.setNavigationOnClickListener(new Toolbar.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            mToolbar.setNavigationOnClickListener(clickListener);
        }
    }


    private void initBroadCast() {
        /*if (mReceiver == null)
            mReceiver = new BaseReceiver();*/
        /*if (mFilter == null)
            mFilter = new IntentFilter();
        initBroadCast();
        mContext.registerReceiver(mReceiver, mFilter);*/
        //mFilter.addAction();
    }

    protected void init() {
        initView();
        initData();
        initSetListener();
    }

    /**
     * 初始化控件使用
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 设置监听事件
     */
    protected abstract void initSetListener();

    /**
     * 在子Activity中处理handler
     *
     * @param msg 发送过来的msg
     */
    protected abstract void handlerMessage(Message msg);

    /**
     * 处理广播
     *
     * @param context 上下文
     * @param intent  数据
     */
    protected void onBroadCastReceiver(Context context, Intent intent) {
    }

    public class BaseHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            handlerMessage(msg);
        }
    }

    /**
     * 广播
     */
    /*private class BaseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            onBroadCastReceiver(context, intent);
        }
    }*/

    /**
     * 显示的文字提示信息
     *
     * @param str 显示文字
     */
    protected void showMessage(String str) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, str, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(str);
        }
        mToast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if (mReceiver != null)
            mContext.unregisterReceiver(mReceiver);*/
        if (mHandler != null){
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * 6.0处理申请权限，大于Build.VERSION_CODES.M，则申请权限，否则可以直接使用权限
     * @param permission 申请的权限
     * @param code
     */
    protected void requestPermission(String[] permission, int code) {
        if (permission == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, permission, code);
        } else {
            notifyPermission(code, true);
        }
    }

    protected void requestReadStoragePermission() {
        String[] permission = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        boolean result = true;//默认已经都授权了
        outer :
        for (String per:permission){
            result = verifyGrantPermission(per);
            if (!result){
                break outer;
            }
        }

        if (!result) {
            requestPermission(permission, REQUEST_CODE_READ_PERMISSION);
        } else {
            notifyPermission(REQUEST_CODE_READ_PERMISSION, true);
        }
    }

    //判断是否授予了权限
    protected boolean verifyGrantPermission(String permission){
        boolean result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            result = (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        } else {
            result = PermissionChecker.checkSelfPermission(this, permission)
                    == PermissionChecker.PERMISSION_GRANTED;
        }
        return result;
    }

    //验证是否申请了权限
    public static boolean verifyPermissions(int[] grantResults) {
        if (grantResults.length < 1) {
            return false;
        }
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    //申请到权限
    protected void notifyPermission(int code, boolean flag){

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (verifyPermissions(grantResults)){
            notifyPermission(requestCode, true);
        } else {
            notifyPermission(requestCode, false);
        }
        /*switch (requestCode){
            case REQUEST_CODE_READ_PERMISSION:
                boolean verifyPermissions = verifyPermissions(grantResults);
                loge("-----------"+requestCode+"---"+verifyPermissions);
                if (verifyPermissions){
                    notifyPermission(requestCode);
                }
                break;
        }*/
    }
}
