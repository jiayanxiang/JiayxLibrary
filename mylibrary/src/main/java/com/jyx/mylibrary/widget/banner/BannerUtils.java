package com.jyx.mylibrary.widget.banner;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.donkingliang.banner.CustomBanner;
import com.jyx.mylibrary.utils.StringUtils;
import com.jyx.mylibrary.widget.image.ImagePreviewUtlis;
import com.jyx.mylibrary.widget.image.MyRectangleView;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Banner utils.
 *
 * @author jyx
 * @CTime 2019 /3/1
 * @explain:
 */
public class BannerUtils {

    /**
     * Sets banner view.
     *
     * @param context      the context
     * @param customBanner the custom banner
     * @param stringList   the string list
     */
    public static void setBannerView(final Context context, CustomBanner customBanner, final List<String> stringList) {
        customBanner.setPages(new CustomBanner.ViewCreator<String>() {
            @Override
            public View createView(final Context context, final int position) {
                //这里返回的是轮播图的项的布局 支持任何的布局
                //position 轮播图的第几个项
                MyRectangleView myRectangleView = new MyRectangleView(context);
                myRectangleView.setAdjustViewBounds(true);
                myRectangleView.setScaleType(ImageView.ScaleType.FIT_XY);
                return myRectangleView;
            }

            @Override
            public void updateUI(final Context context, View view, final int position, String data) {
                //在这里更新轮播图的UI
                //position 轮播图的第几个项
                //view 轮播图当前项的布局 它是createView方法的返回值
                //data 轮播图当前项对应的数据
                Glide.with(context).load(data).into((ImageView) view);
            }
        }, stringList);
        customBanner.setIndicatorStyle(CustomBanner.IndicatorStyle.ORDINARY);
        customBanner.startTurning(5000);
        customBanner.setScrollDuration(300);
        if (stringList.size() <= 1) {
            customBanner.stopTurning();
        }
        customBanner.setOnPageClickListener(new CustomBanner.OnPageClickListener() {
            @Override
            public void onPageClick(int i, Object o) {
                ImagePreviewUtlis.setMutliImage(context, stringList, i);
            }
        });
    }

    /**
     * Sets banner view.
     *
     * @param context      the context
     * @param customBanner the custom banner
     * @param str          the str
     */
    public static void setBannerView(Context context, CustomBanner customBanner, String str) {
        if (StringUtils.isObjectEmpty(str)) {
            return;
        }
        String[] split = str.split(",");
        List<String> stringList = new ArrayList<>();
        stringList.clear();
        for (int i = 0; i < split.length; i++) {
            stringList.add(split[i]);
        }
        setBannerView(context, customBanner, stringList);
    }
}
