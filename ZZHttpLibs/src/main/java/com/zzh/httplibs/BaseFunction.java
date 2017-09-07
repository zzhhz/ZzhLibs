package com.zzh.httplibs;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ZZH on 2017/9/7.
 *
 * @Date: 2017/9/7
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description:
 */
public class BaseFunction implements Function<Observable, ObservableSource> {
    @Override
    public ObservableSource apply(@NonNull Observable observable) throws Exception {
        return observable.retry(1)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        onAccept(disposable);
                    }
                });
    }

    public void onAccept(Disposable disposable) {

    }
}
