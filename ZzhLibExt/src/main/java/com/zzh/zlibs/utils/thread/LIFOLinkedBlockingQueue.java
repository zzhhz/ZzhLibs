package com.zzh.zlibs.utils.thread;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Administrator.
 *
 * @date: 2018/11/5
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description: ZzhLibs 后进先出
 * @since 1.0
 */
public class LIFOLinkedBlockingQueue<E> extends LinkedBlockingDeque<E> {
    /**
     * 调度策略, 先进先出 true
     */
    private boolean isFiFoOrLiFo = true;

    public LIFOLinkedBlockingQueue(boolean isLiFoOrFiFo) {
        this.isFiFoOrLiFo = isLiFoOrFiFo;
    }

    @Override
    public E take() throws InterruptedException {
        if (isFiFoOrLiFo) {
            return super.take();
        } else {
            return pollLast();
        }
    }
}
