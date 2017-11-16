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

import com.zzh.zlibs.R;
import com.zzh.zlibs.swipe.activity.SwipeBackActivity;


/**
 * Created by user on 2017/11/16.
 *
 * @date: 2017/11/16
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description: 需要自己设置layout的Activity。支持右滑退出当前页面。
 */
public abstract class BaseDataSwipeActivity extends SwipeBackActivity {
    protected static String TAG;
    protected Context mContext;
    protected BaseHandler mHandler;
    protected Toast mToast;
    protected Toolbar mToolbar;
    protected TextView mTitle;
    //权限
    protected static final int REQUEST_CODE_READ_PERMISSION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getLocalClassName();
        mContext = this;
        if (mHandler == null)
            mHandler = new BaseHandler();
    }

    protected void toolbars(int toolbarId, int resId, String title, Toolbar.OnClickListener clickListener) {
        setToolbar(toolbarId);
        toolbars(title, resId, clickListener);
    }

    protected void toolbars(Toolbar toolbar, int resId, String title, Toolbar.OnClickListener clickListener) {
        setToolbar(toolbar);
        toolbars(title, resId, clickListener);
    }

    protected void setToolbarAndTitle(int toolbarId, int titleId) {
        setToolbar(toolbarId);
        setToolBarTitle(titleId);
    }

    public void setToolBarTitle(int titleId) {
        this.mTitle = mToolbar.findViewById(titleId);
    }


    //设置Toolbar
    protected void toolbars(String title) {
        toolbars(title, null);
    }

    public void setToolbar(int toolbarId) {
        this.mToolbar = (Toolbar) findViewById(toolbarId);
    }

    public void setToolbar(Toolbar mToolbar) {
        this.mToolbar = mToolbar;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    protected void toolbars(String title, Toolbar.OnClickListener clickListener) {
        toolbars(title, -1, clickListener);
    }

    protected void toolbars(String title, int ic_back, Toolbar.OnClickListener clickListener) {
        try {
            mToolbar = getToolbar();
            if (mToolbar == null) {
                this.mToolbar = (Toolbar) findViewById(R.id.toolbar);
            }
        } catch (Exception ex) {
            Log.e(TAG, "没有设置toolbar");
        }
        if (mToolbar == null)
            return;
        if (ic_back > 0)
            mToolbar.setNavigationIcon(ic_back);
        mToolbar.setTitle("");
        if (title != null && mTitle != null) {
            mTitle.setText(title);
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
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    //6.0处理申请权限
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

    /**
     * 申请读写SD卡的权限
     */
    protected void requestReadStoragePermission() {
        String[] permission = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        boolean result = true;//默认已经都授权了
        outer:
        for (String per : permission) {
            result = verifyGrantPermission(per);
            if (!result) {
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
    protected boolean verifyGrantPermission(String permission) {
        boolean result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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

    /**
     * 通知申请到得权限
     *
     * @param code 自定义的Request Code 和自己申请权限时是一致的
     * @param flag true 申请权限成功，false没有申请到权限
     */
    protected void notifyPermission(int code, boolean flag) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (verifyPermissions(grantResults)) {
            notifyPermission(requestCode, true);
        } else {
            notifyPermission(requestCode, false);
        }
    }
}
