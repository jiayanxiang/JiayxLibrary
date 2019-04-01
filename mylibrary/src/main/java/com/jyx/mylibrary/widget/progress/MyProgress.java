package com.jyx.mylibrary.widget.progress;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyx.mylibrary.R;
import com.jyx.mylibrary.utils.StringUtils;

/**
 * @author jyx
 * @ctime 2018/11/12:19:30
 * @explain
 */

public class MyProgress {

    public static Dialog cancelDialog;
    private static MyProgress dialogUtils;
    private Context context;

    public static MyProgress getInstance() {
        if (null == dialogUtils) {
            dialogUtils = new MyProgress();
        }
        return dialogUtils;
    }

    /**
     * Activity加载动画
     *
     * @param context
     */
    public void showActivityAnimation(Context context) {
        //显示器，先判断，当前界面上是否有正在显示的，如果有的话，直接取消，然后再创建下一个
        dismiss();
        if (context == null) {
            return;
        }

        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        this.context = context;
        if (Looper.myLooper() != Looper.getMainLooper()) {
            return;
        }
        cancelDialog = createLoadingDialog(context, "加载中...");
        if (!StringUtils.isObjectEmpty(cancelDialog)) {
            cancelDialog.show();
        }
    }

    /**
     * Activity加载动画
     *
     * @param context
     * @param msg
     */
    public void showActivityAnimation(Context context, String msg) {
        //显示器，先判断，当前界面上是否有正在显示的，如果有的话，直接取消，然后再创建下一个
        dismiss();
        if (context == null) {
            return;
        }

        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        this.context = context;
        if (Looper.myLooper() != Looper.getMainLooper()) {
            return;
        }
        cancelDialog = createLoadingDialog(context, msg + "");
        cancelDialog.show();
    }

    /**
     * 点击空白区域，是否消失
     *
     * @param isCancel
     */
    public void setCanceledOnTouchOutsid(boolean isCancel) {
        if (cancelDialog != null) {
            cancelDialog.setCanceledOnTouchOutside(isCancel);
        }
    }

    /**
     * Activity加载动画消失
     */
    public void dismiss() {
        if (null != cancelDialog) {
            if (cancelDialog.isShowing()) {
                if (context instanceof Activity) {
                    if (((Activity) context).isFinishing()) {
                        return;
                    }
                }
                cancelDialog.dismiss();
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    return;
                }
                cancelDialog.getWindow().setDimAmount(1f);
            }
        }
    }


    /**
     * 得到自定义的progressDialog
     *
     * @param context
     * @return
     */
    private Dialog createLoadingDialog(Context context, String str) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_view, null);// 得到加载view
        TextView dialog_txt = (TextView) v.findViewById(R.id.dialog_txt);
        dialog_txt.setText(str);
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_anim);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);

        Dialog loadingDialog = new Dialog(context, R.style.dialog);// 创建自定义样式dialog
        loadingDialog.getWindow().setDimAmount(0.2f);
        loadingDialog.setCancelable(true);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
        loadingDialog.setCanceledOnTouchOutside(false);
        return loadingDialog;
    }

}
