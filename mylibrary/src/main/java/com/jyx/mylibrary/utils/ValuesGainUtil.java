package com.jyx.mylibrary.utils;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;

/**
 * @author jyx
 * @ctime 2017/11/4:9:41
 * @explain Values资源获取工具类
 */

public class ValuesGainUtil {

    public static ValuesGainUtil mValuesGainUtil;

    public static ValuesGainUtil getInstance() {
        if (mValuesGainUtil == null) {
            mValuesGainUtil = new ValuesGainUtil();
        }

        return mValuesGainUtil;
    }

    /**
     * 获取Values-strings.xml 中的string
     *
     * @param context
     * @param valuesId
     * @return
     */
    public String getValuesStr(Context context, int valuesId) {

        String valuesStr = "";

        valuesStr = context.getResources().getString(valuesId);

        return valuesStr;
    }

    /**
     * 获取Values-strings.xml 中的stringArray
     *
     * @param context
     * @param valuesArrayId
     * @return
     */
    public String[] getValuesStrArray(Context context, int valuesArrayId) {

        String[] strArray = context.getResources().getStringArray(valuesArrayId);

        return strArray;
    }

    /**
     * 获取颜色
     * @param context
     * @param valuesColor
     * @return
     */
    public int getValuesColor(Context context, int valuesColor) {

        int version = Build.VERSION.SDK_INT;

        if (version >= Build.VERSION_CODES.M) {

            return ContextCompat.getColor(context, valuesColor);

        } else {

            return context.getResources().getColor(valuesColor);

        }
    }

}
