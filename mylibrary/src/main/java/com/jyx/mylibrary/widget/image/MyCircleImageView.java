package com.jyx.mylibrary.widget.image;

import android.content.Context;
import android.util.AttributeSet;

import com.jyx.mylibrary.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author:jyx created at 2019/1/4 18:32
 * explain:圆形头像图片
 */
public class MyCircleImageView extends CircleImageView {
    public MyCircleImageView(Context context) {
        super(context);
        this.setBackgroundResource(R.drawable.image_user_default);
    }

    public MyCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setBackgroundResource(R.drawable.image_user_default);
    }

    public MyCircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setBackgroundResource(R.drawable.image_user_default);
    }
}
