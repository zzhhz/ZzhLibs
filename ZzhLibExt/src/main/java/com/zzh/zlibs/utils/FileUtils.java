package com.zzh.zlibs.utils;

import android.os.Environment;

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
        BufferedOutputStream bos = null;
        if (ZUtils.isMountedSDCard()) {
            try {
                File file = new File(filePathName);
                if (file.exists()) {
                    return true;
                } else {
                    file.createNewFile();
                }
                bos = new BufferedOutputStream(new FileOutputStream(file));
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
}
