package com.zzh.zlibs.image;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.Objects;
import java.util.logging.Handler;

/**
 * Created by Administrator.
 *
 * @date: 2019/5/13
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description: ZzhLibs
 * @since 1.0
 */
public class ScanImageRunnable implements Runnable {

    public static final int RESUTL_SCAN_IMAGE = 5000;

    private Context ctx;
    private Handler handler;

    public ScanImageRunnable(Context ctx, Handler handler) {
        if (ctx == null) {
            throw new NullPointerException("Context 不能为空");
        }
        this.ctx = ctx;
        this.handler = handler;
    }

    @Override
    public void run() {
        ContentResolver resolver = ctx.getContentResolver();
        Cursor query = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png", "image/jpg"}, MediaStore.Images.Media.DATE_MODIFIED);
        if (query != null) {



        }

    }
}
