package com.jyx.mylibrary.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

/**
 * jyx
 * 测量屏幕分辨率设置侧滑删除距离
 * Created by weisi on 2017/6/7.
 */

public class DisplayUtils {

    public static Point getDisplaySize(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics dm = new DisplayMetrics();

        wm.getDefaultDisplay().getMetrics(dm);

        Point size = new Point();

        size.x = dm.widthPixels;

        size.y = dm.heightPixels;

        return size;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void getMeasuredWidth(final View view){
        final ViewTreeObserver vto1 = view.getViewTreeObserver();
        vto1.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int measuredWidth1 = view.getMeasuredWidth();
                int measuredHeight = view.getMeasuredHeight();
                return true;
            }
        });
    }
}
