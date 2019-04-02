package com.jyx.mylibrary.base;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.jyx.mylibrary.utils.ScreenUtils;
import com.jyx.mylibrary.utils.ToastUtils;
import com.wanjian.cockroach.Cockroach;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import static com.zhy.http.okhttp.OkHttpUtils.initClient;

public class BaseApplication extends Application {

    public static BaseApplication mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        if (mContext == null) {
            mContext = this;
        }
        mContext = this;
        //初始化okhttp
        initOkHttpManager();

        initMethod();
    }

    public void initMethod() {

        ToastUtils.init(this);
        ScreenUtils.init(this);
        initCockroach();
    }

    public static BaseApplication getInstance() {
        if (mContext != null && mContext instanceof BaseApplication) {
            return mContext;
        } else {
            mContext = new BaseApplication() {
                @Override
                public void initMethod() {

                }
            };
            mContext.onCreate();
            return mContext;
        }
    }

    /**
     * 初始化okhttp
     */
    public void initOkHttpManager() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("OkHttp Log"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .followRedirects(true)//可以重定向
                //其他配置
                .build();

        initClient(okHttpClient);
    }

    /**
     * 初始化Cockroach
     */
    public void initCockroach() {
        Cockroach.install(new Cockroach.ExceptionHandler() {

            // handlerException内部建议手动try{  你的异常处理逻辑  }catch(Throwable e){ } ，以防handlerException内部再次抛出异常，导致循环调用handlerException

            @Override
            public void handlerException(final Thread thread, final Throwable throwable) {
                Log.e("Cockroach", thread + "\n" + throwable.toString());
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.e("Cockroach", thread + "\n" + throwable.toString());
                            throwable.printStackTrace();
//                        throw new RuntimeException("..."+(i++));
                        } catch (Throwable e) {
                            Log.e("Cockroach", e.getMessage().toString());

                        }
                    }
                });
            }
        });
    }

}
