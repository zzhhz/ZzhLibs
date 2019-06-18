package com.zzh.zlibs.image.runnable;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.zzh.zlibs.image.model.FileItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator.
 *
 * @date: 2019/6/17
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description: ZzhLibs 扫描文件夹
 * @since 1.0
 */
public class ScanFolderRunnable implements Runnable {
    private Context ctx;
    private ScanImageRunnable.Builder builder;
    List<FileItem> list = new ArrayList<>();

    public ScanFolderRunnable(Context ctx, ScanImageRunnable.Builder builder) {
        this.ctx = ctx;
        if (builder == null) {
            this.builder = new ScanImageRunnable.Builder();
        } else {
            this.builder = builder;
        }
    }

    @Override
    public void run() {
        ContentResolver resolver = ctx.getContentResolver();
        Uri files = MediaStore.Files.getContentUri("external");
        String selection = " mime_type = ? or mime_type = ? or mime_type = ? ";
        String[] selectionArgs = {"image/jpeg", "image/png", "image/jpg"};
        if (builder.isScanVideo()) {
            selection = " mime_type = ? or mime_type = ? or mime_type = ? or mime_type = ? or mime_type = ? or mime_type = ? or mime_type = ? or mime_type = ? ";
            selectionArgs = new String[]{"image/jpeg", "image/png", "image/jpg", "video/mp4", "video/avi", "video/rm", "video/mkv", "video/flv"};
        }
        Cursor query = resolver.query(files, new String[]{
                        "DISTINCT bucket_display_name ",},
                selection,
                selectionArgs, MediaStore.Images.Media.DATE_MODIFIED);
        list.clear();
        if (query != null) {
            int indexTitle = query.getColumnIndex("bucket_display_name");
            while (query.moveToNext()) {
                String title = query.getString(indexTitle);
                FileItem item = new FileItem();
                item.setTitle(title);
                list.add(item);
            }
        }
    }

    public List<FileItem> getImageFolder() {
        run();
        return list;
    }
}
