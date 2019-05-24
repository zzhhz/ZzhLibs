package com.zzh.zlibs.image;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;


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
        Uri files = MediaStore.Files.getContentUri("external");
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Log.e("--------", "run: " + uri.toString());
        Cursor query = resolver.query(files, new String[]{"_id", "_data", "_display_name", "title", "mime_type", "width", "height", "mini_thumb_data"},
                " mime_type = ? or mime_type = ? or mime_type = ? or mime_type = ? or mime_type = ? or mime_type = ?",
                new String[]{"image/jpeg", "image/png", "image/jpg", "video/mp4", "video/avi", "video/rm"}, MediaStore.Images.Media.DATE_MODIFIED);
        if (query != null) {
            int indexId = query.getColumnIndex("_id");
            int indexData = query.getColumnIndex("_data");
            int indexDN = query.getColumnIndex("_display_name");
            int indexTitle = query.getColumnIndex("title");
            int indexMimeType = query.getColumnIndex("mime_type");
            int indexWidth = query.getColumnIndex("width");
            int indexHeight = query.getColumnIndex("height");

            while (query.moveToNext()) {
                int id = query.getInt(indexId);
                String data = query.getString(indexData);
                String display_name = query.getString(indexDN);
                String title = query.getString(indexTitle);
                String mime = query.getString(indexMimeType);
                int width = query.getInt(indexWidth);
                int height = query.getInt(indexHeight);
                Log.e("--------", "run: " + id + ", " + data + ", " + display_name + ", " + mime + ", " + title + ", " + width + ", " + height);
            }


        }

    }
}
