package com.zzh.zlibs.utils;

import android.content.Context;
import android.os.Environment;
import android.sax.TextElementListener;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator.
 *
 * @date: 2018/11/6
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description: ZzhLibs 文件操作
 * @since 1.0
 */
public class FileUtils {
    /**
     * 文件操作
     *
     * @param data         字节
     * @param filePathName 写入的文件路径
     * @return 写入成功 true
     */
    public static boolean saveFile(byte[] data, String filePathName) {
        if (data == null || TextUtils.isEmpty(filePathName)) {
            return false;
        }
        return saveFile(data, new File(filePathName));
    }

    /**
     * 文件操作
     *
     * @param data         字节
     * @param filePathName 写入的文件
     * @return 写入成功 true
     */
    public static boolean saveFile(byte[] data, File filePathName) {
        BufferedOutputStream bos = null;
        if (ZUtils.isMountedSDCard()) {
            try {
                bos = new BufferedOutputStream(new FileOutputStream(filePathName));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 私有缓存目录。如果需要缓存多重类型的文件，建议在此目录下再次进行新建目录划分
     *
     * @return 缓存路径
     */
    public static String getPrivateCachePath(Context ctx) {
        File cacheDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            cacheDir = ctx.getExternalCacheDir();
        } else {
            cacheDir = ctx.getCacheDir();
        }
        return cacheDir.getAbsolutePath();
    }


}
