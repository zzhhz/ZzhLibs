package com.zzh.zlibs.utils.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

/**
 * Created by Administrator.
 *
 * @date: 2018/11/5
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description: ZzhLibs 线程管理, 单例模式, 后进先出
 * @since 1.0
 */
public class ZThreadLIFOManager {

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
    /**
     * 单位
     */
    private TimeUnit sUnit = TimeUnit.MINUTES;
    /**
     * 线程调度模式
     */
    private boolean sFifo = false;
    /**
     * 线程池
     */
    private ThreadPoolExecutor mExecutor;
    /**
     * 线程池管理工具
     */
    private static ZThreadLIFOManager mInstance;

    public static ZThreadLIFOManager getInstance(Builder builder) {
        if (mInstance == null) {
            synchronized (ZThreadLIFOManager.class) {
                if (mInstance == null) {
                    mInstance = new ZThreadLIFOManager(builder);
                }
            }
        }
        return mInstance;
    }

    public static ZThreadLIFOManager getInstance() {
        if (mInstance == null) {
            synchronized (ZThreadLIFOManager.class) {
                if (mInstance == null) {
                    mInstance = new ZThreadLIFOManager(new Builder());
                }
            }
        }
        return mInstance;
    }


    private ZThreadLIFOManager(Builder builder) {
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

    /**
     * 构造者模式
     */
    public static class Builder {

        private int sCorePoolSize;
        private int sMaxPoolSize;
        private long sKeepAliveTime = 1;
        private TimeUnit sUnit = TimeUnit.MINUTES;
        private boolean sFifo = false;

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

        public ZThreadLIFOManager build() {

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

    /**
     * 移除执行任务
     *
     * @param runnable
     * @return
     */
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
