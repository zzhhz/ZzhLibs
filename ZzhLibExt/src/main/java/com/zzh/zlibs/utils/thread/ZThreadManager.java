package com.zzh.zlibs.utils.thread;

import android.support.annotation.NonNull;

import java.util.concurrent.BlockingQueue;
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
    private boolean sFifo = true;
    private ThreadPoolExecutor mExecutor;
    private static ZThreadManager mInstance;

    public static ZThreadManager getInstance(Builder builder) {
        if (mInstance == null) {
            synchronized (ZThreadManager.class) {
                if (mInstance == null) {
                    mInstance = new ZThreadManager(builder);
                }
            }
        }
        return mInstance;
    }

    public static ZThreadManager getInstance() {
        if (mInstance == null) {
            synchronized (ZThreadManager.class) {
                if (mInstance == null) {
                    mInstance = new ZThreadManager(new Builder());
                }
            }
        }
        return mInstance;
    }


    private ZThreadManager(Builder builder) {
        /**
         * 初始化数据
         */
        if (builder.sCorePoolSize < 1) {
            int availableProcessors = Runtime.getRuntime().availableProcessors();
            this.sCorePoolSize = availableProcessors;
            this.sMaxPoolSize = availableProcessors * 2 + 1;
        } else {
            this.sCorePoolSize = builder.getCorePoolSize();
            this.sMaxPoolSize = builder.getMaxPoolSize();
        }


        this.sKeepAliveTime = builder.getKeepAliveTime();
        this.sUnit = builder.getUnit();
        this.sFifo = builder.isFifo();


        mExecutor = initThreadPool();
    }

    @NonNull
    private ThreadPoolExecutor initThreadPool() {
        return new ThreadPoolExecutor(sCorePoolSize, sMaxPoolSize,
                sKeepAliveTime, sUnit, new LIFOLinkedBlockingQueue<Runnable>(sFifo),
                new ZDefaultThreadFactory(Thread.NORM_PRIORITY, "zzh-pool-"),
                new ThreadPoolExecutor.AbortPolicy());
    }

    public static class Builder {

        private int sCorePoolSize;
        private int sMaxPoolSize;
        private long sKeepAliveTime = 1;
        private TimeUnit sUnit = TimeUnit.MINUTES;
        private boolean sFifo = true;

        public Builder setCorePoolSize(int sCorePoolSize) {
            this.sCorePoolSize = sCorePoolSize;
            return this;
        }

        public Builder setMaxPoolSize(int sMaxPoolSize) {
            this.sMaxPoolSize = sMaxPoolSize;
            return this;
        }

        public Builder setKeepAliveTime(long sKeepAliveTime) {
            this.sKeepAliveTime = sKeepAliveTime;
            return this;
        }

        public Builder setUnit(TimeUnit sUnit) {
            this.sUnit = sUnit;
            return this;
        }

        public Builder setFifo(boolean sFifo) {
            this.sFifo = sFifo;
            return this;
        }

        public int getCorePoolSize() {
            return sCorePoolSize;
        }

        public int getMaxPoolSize() {
            return sMaxPoolSize;
        }

        public long getKeepAliveTime() {
            return sKeepAliveTime;
        }

        public TimeUnit getUnit() {
            return sUnit;
        }

        public boolean isFifo() {
            return sFifo;
        }

        public ZThreadManager build() {

            return getInstance(this);
        }
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
