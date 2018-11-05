package com.zzh.zlibs.utils.thread;

import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator.
 *
 * @date: 2018/11/5
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description: ZzhLibs 线程管理, 单例模式
 * @since 1.0
 */
public class ZThreadManager {

    /**
     * 线程核心数量
     */
    private int sCorePoolSize;
    /**
     * 最大线程数量
     */
    private int sMaxPoolSize;
    /**
     * 存活时间
     */
    private long sKeepAliveTime = 1;
    private TimeUnit sUnit = TimeUnit.MINUTES;
    private ThreadPoolExecutor mExecutor;
    private static ZThreadManager mInstance;

    public static ZThreadManager getInstance() {
        if (mInstance == null) {
            synchronized (ZThreadManager.class) {
                if (mInstance == null) {
                    mInstance = new ZThreadManager();
                }
            }
        }
        return mInstance;
    }


    private ZThreadManager() {
        /**
         * 初始化数据
         */
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        this.sCorePoolSize = availableProcessors;
        sMaxPoolSize = availableProcessors * 2 + 1;
        mExecutor = initThreadPool();
    }

    @NonNull
    private ThreadPoolExecutor initThreadPool() {
        return new ThreadPoolExecutor(sCorePoolSize, sMaxPoolSize,
                sKeepAliveTime, sUnit, new LIFOLinkedBlockingQueue<Runnable>(true),
                new ZDefaultThreadFactory(Thread.NORM_PRIORITY, "zzh-pool-"),
                new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 执行任务
     *
     * @param runnable
     */
    public void execute(Runnable runnable) {
        if (mExecutor == null) {
            mExecutor = initThreadPool();
        }
        if (runnable != null) {
            mExecutor.execute(runnable);
        }
    }

    public boolean remove(Runnable runnable) {
        if (runnable == null) {
            return true;
        }
        return mExecutor.remove(runnable);
    }

    /**
     * 取消所有任务
     *
     * @return
     */
    public void cancel() {
        BlockingQueue<Runnable> queue = mExecutor.getQueue();
        queue.clear();
    }


}
