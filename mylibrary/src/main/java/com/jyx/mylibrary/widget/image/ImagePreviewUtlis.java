package com.jyx.mylibrary.widget.image;


import android.content.Context;

import com.jyx.mylibrary.R;
import com.jyx.mylibrary.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import cc.shinichi.library.ImagePreview;
import cc.shinichi.library.bean.ImageInfo;


/**
 * Create by zwz
 * on 2019/1/4
 */
public class ImagePreviewUtlis {

    //查看多图
    public static void setMutliImage(Context context, List<String> list, int position) {
        if (!StringUtils.isObjectEmpty(list)) {
            if (list.size() == 0) {
                return;
            }

            List<ImageInfo> infoList = new ArrayList<>();
            for (String image : list) {
                if (image.indexOf("?") >= 0) {
                    image = image.substring(0, image.indexOf("?"));
                }
                ImageInfo imageInfo = new ImageInfo();
                imageInfo.setOriginUrl(image);
                imageInfo.setThumbnailUrl(image);
                infoList.add(imageInfo);
            }
            ImagePreview
                    .getInstance()
                    .setLoadStrategy(ImagePreview.LoadStrategy.AlwaysOrigin)
                    .setContext(context)
                    .setIndex(position)
                    .setImageInfoList(infoList)
                    .setShowDownButton(true)
                    .setFolderName("wangzhen")
                    .setScaleLevel(1, 3, 5)
                    .setZoomTransitionDuration(500)
                    .setEnableClickClose(true)   // 是否启用点击图片关闭。默认启用
                    .setEnableDragClose(true) // 是否启用上拉/下拉关闭。默认不启用
                    .setShowCloseButton(false)// 是否显示关闭页面按钮，在页面左下角。默认不显示
                    .setCloseIconResId(R.drawable.ic_action_close)// 设置关闭按钮图片资源，可不填，默认为：R.drawable.ic_action_close
                    .setShowDownButton(true)// 是否显示下载按钮，在页面右下角。默认显示
                    .setDownIconResId(R.drawable.icon_download_new)// 设置下载按钮图片资源，可不填，默认为：R.drawable.icon_download_new
                    .setShowIndicator(true)// 设置是否显示顶部的指示器（1/9）。默认显示
                    .start();
        }
    }

    //查看多图
    public static void setMutliImage(Context context, List<String> list, int position, boolean isDisplydownloadBtn, boolean isDisplayIndicator) {
        if (!StringUtils.isObjectEmpty(list)) {
            if (list.size() == 0) {
                return;
            }

            List<ImageInfo> infoList = new ArrayList<>();
            for (String image : list) {
                ImageInfo imageInfo = new ImageInfo();
                imageInfo.setOriginUrl(image);
                imageInfo.setThumbnailUrl(image);
                infoList.add(imageInfo);
            }
            ImagePreview
                    .getInstance()
                    .setLoadStrategy(ImagePreview.LoadStrategy.AlwaysOrigin)
                    .setContext(context)
                    .setIndex(position)
                    .setImageInfoList(infoList)
                    .setShowDownButton(true)
                    .setFolderName("Weimi")
                    .setScaleLevel(1, 3, 5)
                    .setZoomTransitionDuration(500)
                    .setEnableClickClose(true)   // 是否启用点击图片关闭。默认启用
                    .setEnableDragClose(true) // 是否启用上拉/下拉关闭。默认不启用
                    .setShowCloseButton(false)// 是否显示关闭页面按钮，在页面左下角。默认不显示
                    .setCloseIconResId(R.drawable.ic_action_close)// 设置关闭按钮图片资源，可不填，默认为：R.drawable.ic_action_close
                    .setShowDownButton(isDisplydownloadBtn)// 是否显示下载按钮，在页面右下角。默认显示
                    .setDownIconResId(R.drawable.icon_download_new)// 设置下载按钮图片资源，可不填，默认为：R.drawable.icon_download_new
                    .setShowIndicator(isDisplayIndicator)// 设置是否显示顶部的指示器（1/9）。默认显示
                    .start();
        }
    }

    //查看多图
    public static void setSingleImage(Context context, String image, boolean isDisplydownloadBtn) {
        if (!StringUtils.isObjectEmpty(image)) {

            List<ImageInfo> infoList = new ArrayList<>();
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.setOriginUrl(image);
            imageInfo.setThumbnailUrl(image);
            infoList.add(imageInfo);
            ImagePreview
                    .getInstance()
                    .setLoadStrategy(ImagePreview.LoadStrategy.AlwaysOrigin)
                    .setContext(context)
                    .setIndex(1)
                    .setImageInfoList(infoList)
                    .setShowDownButton(true)
                    .setFolderName("Weimi")
                    .setScaleLevel(1, 3, 5)
                    .setZoomTransitionDuration(500)
                    .setEnableClickClose(true)   // 是否启用点击图片关闭。默认启用
                    .setEnableDragClose(true) // 是否启用上拉/下拉关闭。默认不启用
                    .setShowCloseButton(false)// 是否显示关闭页面按钮，在页面左下角。默认不显示
                    .setCloseIconResId(R.drawable.ic_action_close)// 设置关闭按钮图片资源，可不填，默认为：R.drawable.ic_action_close
                    .setShowDownButton(isDisplydownloadBtn)// 是否显示下载按钮，在页面右下角。默认显示
                    .setDownIconResId(R.drawable.icon_download_new)// 设置下载按钮图片资源，可不填，默认为：R.drawable.icon_download_new
                    .setShowIndicator(false)// 设置是否显示顶部的指示器（1/9）。默认显示
                    .start();
        }
    }

}
