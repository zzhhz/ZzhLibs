package com.zzh.httplibs;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * Created by ZZH on 2017/9/7.
 *
 * @Date: 2017/9/7
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description:
 */
public class BaseSubscribes<T> implements Subscriber<T> {
    @Override
    public void onSubscribe(Subscription s) {

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onComplete() {

    }
}
