package com.jyx.mylibrary.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by fySpring
 * Date : 2017/4/6
 * To do :Toast工具类
 */

public class ToastUtils {
    private static Context sContext;
    private static Toast toast;

    public static void init(Context context) {
        sContext = context.getApplicationContext();
    }

    public static void show(int messageId) {
        if (!StringUtils.isObjectEmpty(messageId)) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(sContext, ValuesGainUtil.getInstance().getValuesStr(sContext, messageId), Toast.LENGTH_SHORT);
        } else {
            toast.cancel();
            toast = Toast.makeText(sContext, ValuesGainUtil.getInstance().getValuesStr(sContext, messageId), Toast.LENGTH_SHORT);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setText(ValuesGainUtil.getInstance().getValuesStr(sContext, messageId) + "");
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void show(String message) {
        if (StringUtils.isObjectEmpty(message)) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(sContext, message, Toast.LENGTH_SHORT);
        } else {
            toast.cancel();
            toast = Toast.makeText(sContext, message, Toast.LENGTH_SHORT);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setText(message);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}
