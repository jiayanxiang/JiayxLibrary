package com.jyx.mylibrary.base;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.jyx.mylibrary.utils.JsonUtil;
import com.jyx.mylibrary.utils.ScreenUtils;
import com.jyx.mylibrary.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
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
        initUMConfigure();
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
                MobclickAgent.reportError(getInstance(), "custom error :" + throwable.toString());
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

    /**
     * 初始化友盟统计
     */
    public void initUMConfigure() {

        /**
         * 设置组件化的Log开关
         * 参数: boolean 默认为false，如需查看LOG设置为true
         */
        UMConfigure.setLogEnabled(true);
        MobclickAgent.setCatchUncaughtExceptions(true);
        MobclickAgent.setScenarioType(getApplicationContext()
                , MobclickAgent.EScenarioType.E_UM_NORMAL);


        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:【友盟+】 AppKey
         * 参数3:【友盟+】 Channel
         * 参数4:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数5:Push推送业务的secret
         */
        UMConfigure.init(this, "5c9c3ae20cafb2f2c6000d41", "weina", UMConfigure.DEVICE_TYPE_PHONE, "");

        Log.e("um", UMConfigure.isDebugLog() + "/" + JsonUtil.objectToJson(UMConfigure.umDebugLog));
    }

}
