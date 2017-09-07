package com.zzh.httplibs;

import android.text.TextUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ZZH on 2017/9/7.
 *
 * @Date: 2017/9/7
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description: 网络方位工具
 */
public class RetrofitUtils {

    private OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Request.Builder builder = request.newBuilder();
                    initRequest(request);
                    return chain.proceed(builder.build());
                }
            }).connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    public void initRequest(Request request) {
    }

    private Retrofit retrofitService = null;

    public Retrofit getInstance(String url) {
        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException("网址不能为空");
        }
        if (retrofitService == null) {
            synchronized (RetrofitUtils.class) {
                if (retrofitService == null) {
                    retrofitService = new Retrofit.Builder()
                            .baseUrl(url)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .client(httpClient)
                            .build();
                }
            }
        }
        return retrofitService;
    }
}
