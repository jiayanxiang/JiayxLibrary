package com.jyx.mylibrary.utils;

import android.app.Activity;
import android.os.Build;
import android.text.TextUtils;

import com.githang.statusbar.StatusBarCompat;
import com.jyx.mylibrary.R;

/**
 * @author jyx
 * @ctime 2018/9/28:16:31
 * @explain
 */

public class StatusBarUtil {

    /**
     *
     * @param activity
     * @param isFullScreen 是否进入全屏 完全全屏
     */
    public static void setStatusBarColor(Activity activity, Boolean isFullScreen) {
        if (isFullScreen) {
            ImmersionUtils.setSystemUiVisibility(activity,isFullScreen,true);
        } else {
            String model = android.os.Build.MODEL;                  //手机型号
            String carrier = android.os.Build.MANUFACTURER;         //手机厂家
            if (!TextUtils.isEmpty(carrier)) {
                //三星手机未适配，暂时禁用
                if (carrier.equals("samsung") || carrier.equals("OPPO") || carrier.equals("vivo")) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        return;
                    }
                }
            }
            StatusBarCompat.setStatusBarColor(activity, ValuesGainUtil.getInstance().getValuesColor(activity, R.color.status_bar_color), false);//沉浸式
        }
    }

    /**
     * 是否全屏
     * @param activity
     * @param isFullScreen
     * @param isNavigation 是否取消底部导航栏
     */
    public static void setStatusBarColor(Activity activity, Boolean isFullScreen,Boolean isNavigation) {
        if (isFullScreen) {
            ImmersionUtils.setSystemUiVisibility(activity,isFullScreen,isNavigation);
        } else {
            String model = android.os.Build.MODEL;                  //手机型号
            String carrier = android.os.Build.MANUFACTURER;         //手机厂家
            if (!TextUtils.isEmpty(carrier)) {
                //三星手机未适配，暂时禁用
                if (carrier.equals("samsung") || carrier.equals("OPPO") || carrier.equals("vivo")) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        return;
                    }
                }
            }
            StatusBarCompat.setStatusBarColor(activity, ValuesGainUtil.getInstance().getValuesColor(activity, R.color.status_bar_color), false);//沉浸式
        }
    }
}
