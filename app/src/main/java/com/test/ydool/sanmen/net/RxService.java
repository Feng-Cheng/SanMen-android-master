package com.test.ydool.sanmen.net;


import com.test.ydool.sanmen.MyApplication;
import com.test.ydool.sanmen.TerminalInfo;
import com.test.ydool.sanmen.utils.HttpUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/4/19.
 */

public class RxService {


    private static File cacheFile = new File(MyApplication.newInstance().getExternalCacheDir(),"sanMen");

    private static Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);

    /**
     * 提供缓存功能的interceptor
     */
    private static Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!HttpUtil.isNetworkConnected(MyApplication.newInstance())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                        .build();
            }
            Response response = chain.proceed(request);
            if (HttpUtil.isNetworkConnected(MyApplication.newInstance())) {
                int maxAge = 0 * 60;
                // 有网络时 设置缓存超时时间0个小时
                response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .build();
            } else {
                // 无网络时，设置超时为2周
                int maxStale = 60 * 60 * 24 * 14;
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
            }
            return response;
        }
    };

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(interceptor)
            .build();

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(TerminalInfo.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    private RxService() {
    }

    public static <T> T createApi(Class<T> api) {
        return retrofit.create(api);
    }
}
