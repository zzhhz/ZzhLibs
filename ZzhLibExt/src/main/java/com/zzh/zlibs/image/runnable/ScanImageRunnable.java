package com.zzh.zlibs.image.runnable;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.zzh.zlibs.BuildConfig;
import com.zzh.zlibs.image.model.FileItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Administrator.
 *
 * @date: 2019/5/13
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description: ZzhLibs 扫描整个手机的文件
 * @since 1.0
 */
public class ScanImageRunnable implements Runnable {

    public static final int RESUTL_SCAN_IMAGE = 5000;
    private static final String TAG = "ScanImageRunnable";

    private Context ctx;
    private Builder builder;

    public ScanImageRunnable(Context ctx, Builder builder) {
        if (ctx == null) {
            throw new NullPointerException("Context 不能为空");
        }
        this.ctx = ctx;
        if (builder == null) {
            this.builder = new Builder();
        } else {
            this.builder = builder;
        }
    }

    public ScanImageRunnable setBuilder(Builder builder) {
        this.builder = builder;
        return this;
    }

    List<FileItem> list = new ArrayList<>();

    @Override
    public void run() {
        ContentResolver resolver = ctx.getContentResolver();
        Uri files = MediaStore.Files.getContentUri("external");
        String selection = " mime_type = ? or mime_type = ? or mime_type = ? ";
        String[] selectionArgs = {"image/jpeg", "image/png", "image/jpg"};
        if (!TextUtils.isEmpty(builder.getBucketDisplayName())) {
            selection = " bucket_display_name = ? and mime_type = ? or mime_type = ? or mime_type = ? ";
            selectionArgs = new String[]{builder.getBucketDisplayName(), "image/jpeg", "image/png", "image/jpg"};
        }

        if (builder.isScanVideo()) {
            selection = " mime_type = ? or mime_type = ? or mime_type = ? or mime_type = ? or mime_type = ? or mime_type = ? or mime_type = ? or mime_type = ? ";
            selectionArgs = new String[]{"image/jpeg", "image/png", "image/jpg", "video/mp4", "video/avi", "video/rm", "video/mkv", "video/flv"};
            if (!TextUtils.isEmpty(builder.getBucketDisplayName())) {
                selection = " bucket_display_name = ? and mime_type = ? or mime_type = ? or mime_type = ? or mime_type = ? or mime_type = ? or mime_type = ? or mime_type = ? or mime_type = ? ";
                selectionArgs = new String[]{builder.getBucketDisplayName(), "image/jpeg", "image/png", "image/jpg", "video/mp4", "video/avi", "video/rm", "video/mkv", "video/flv"};
            }
        }
        Log.e("---------", "run: " + Arrays.toString(selectionArgs));
        Log.e("---------", "run: " + selection);
        Cursor query = resolver.query(files, new String[]{MediaStore.Files.FileColumns._ID,
                        MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.PARENT, "title", "mime_type", "width", "height", "mini_thumb_data", "_size", "date_added"},
                selection,
                selectionArgs, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
        list.clear();
        if (query != null) {
            int indexId = query.getColumnIndex("_id");
            int indexData = query.getColumnIndex("_data");
            int indexParent = query.getColumnIndex("parent");
            int indexTitle = query.getColumnIndex("title");
            int indexMimeType = query.getColumnIndex("mime_type");
            int indexWidth = query.getColumnIndex("width");
            int indexHeight = query.getColumnIndex("height");
            int indexThumb = query.getColumnIndex("mini_thumb_data");
            int indexSize = query.getColumnIndex("_size");
            int indexDate = query.getColumnIndex("date_added");

            while (query.moveToNext()) {
                int id = query.getInt(indexId);
                String data = query.getString(indexData);
                String display_name = query.getString(indexParent);
                String title = query.getString(indexTitle);
                String mime = query.getString(indexMimeType);
                String thumb = query.getString(indexThumb);
                int width = query.getInt(indexWidth);
                int height = query.getInt(indexHeight);
                long size = query.getLong(indexSize);
                long date = query.getLong(indexDate);

                FileItem item = new FileItem();
                item.setHeight(height);
                item.setWidth(width);
                item.setDate_added(date);
                item.setPath(data);
                item.setTitle(title);
                item.setThumb(thumb);
                item.setFileSize(size);
                item.setFileType(data);
                item.setMime_type(mime);
                item.setId(id);
                item.setParent(display_name);
                list.add(item);
            }
        }
    }

    public List<FileItem> getImageFile() {
        run();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "getImageFile: " + list.size());
        }
        return list;
    }

    public static class Builder {
        /**
         * 是否扫描图片
         */
        private boolean scanImage = true;
        /**
         * 是否扫描视频
         */
        private boolean scanVideo = true;
        private String bucketDisplayName;

        public String getBucketDisplayName() {
            return bucketDisplayName;
        }

        public void setBucketDisplayName(String bucketDisplayName) {
            this.bucketDisplayName = bucketDisplayName;
        }

        public boolean isScanImage() {
            return scanImage;
        }

        public Builder setScanImage(boolean scanImage) {
            this.scanImage = scanImage;
            return this;
        }

        public boolean isScanVideo() {
            return scanVideo;
        }

        public Builder setScanVideo(boolean scanVideo) {
            this.scanVideo = scanVideo;
            return this;
        }
    }
}
