package com.jyx.mylibrary.widget.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.jyx.mylibrary.R;

/**
 * @author:jyx created at 2019/1/4 18:33
 * explain:矩形图
 */
@SuppressLint("AppCompatCustomView")
public class MyRectangleView extends ImageView {

    private SelectType mSelectType;

    public MyRectangleView(Context context) {
        super(context);
        this.setBackgroundResource(R.drawable.image_default);
    }

    public MyRectangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setBackgroundResource(R.drawable.image_default);
        getAttrs(context, attrs);
    }

    public MyRectangleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setBackgroundResource(R.drawable.image_default);
        getAttrs(context, attrs);
    }

    /**
     * Label的选择类型
     */
    public enum SelectType {
        DEFAULT(1),
        DEFAULT_2x3(2),
        DEFAULT_2x4(3),
        DEFAULT_3x2(4),
        DEFAULT_4x2(5),
        DEFAULT_2x3_ROUND(6);
        int value;

        SelectType(int value) {
            this.value = value;
        }

        static SelectType get(int value) {
            switch (value) {
                case 1:
                    return DEFAULT;
                case 2:
                    return DEFAULT_2x3;
                case 3:
                    return DEFAULT_2x4;
                case 4:
                    return DEFAULT_3x2;
                case 5:
                    return DEFAULT_4x2;
                case 6:
                    return DEFAULT_2x3_ROUND;
            }
            return DEFAULT;
        }
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.rectangle_background_default);
            int type = mTypedArray.getInt(R.styleable.rectangle_background_default_selectDefaultType, 1);
            mSelectType = SelectType.get(type);
            setBackGround();
            mTypedArray.recycle();
        }
    }

    private void setBackGround() {
        switch (mSelectType.value) {
            case 1: {
                this.setBackgroundResource(R.drawable.image_default);
                break;
            }
            case 2: {
                this.setBackgroundResource(R.drawable.image_default);
                break;
            }
            case 3: {
                this.setBackgroundResource(R.drawable.image_default);
                break;
            }
            case 4: {
                this.setBackgroundResource(R.drawable.image_default);
                break;
            }
            case 5: {
                this.setBackgroundResource(R.drawable.image_default);
                break;
            }
            case 6: {
                this.setBackgroundResource(R.drawable.image_default);
                break;
            }
            default: {
                this.setBackgroundResource(R.drawable.image_default);
                break;
            }
        }
    }
}
