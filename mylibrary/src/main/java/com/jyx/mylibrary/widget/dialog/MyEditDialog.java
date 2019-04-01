package com.jyx.mylibrary.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyx.mylibrary.R;
import com.jyx.mylibrary.utils.ValuesGainUtil;

/**
 * @author jyx
 * @ctime 2018/10/22:15:25
 * @explain
 */

public class MyEditDialog extends Dialog {
    private LinearLayout btn_layout;
    private EditText contentTxt;
    private TextView titleTxt;
    private TextView submitTxt;
    private TextView cancelTxt;
    private TextView aloneTxt;

    private Context mContext;
    private String content;
    private String positiveName;
    private String negativeName;
    private String aloneName;
    private String title;
    private int inputType;

    private Boolean isAlone = false; //是否是单独按钮

    public MyEditDialog(Context context) {
        super(context, R.style.dialog);
        this.getWindow().setDimAmount(0.5f);
        this.mContext = context;
    }

    public MyEditDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }

    protected MyEditDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    //设置title
    public MyEditDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    //设置正文内容
    public MyEditDialog setContent(String contentStr) {
        this.content = contentStr;
        return this;
    }

    //设置正文内容
    public MyEditDialog setContent(int contentStr) {
        this.content = ValuesGainUtil.getInstance().getValuesStr(mContext, contentStr);
        return this;
    }

    //设置输入框类型
    public MyEditDialog setContentInputType(int inputType) {
        this.inputType = inputType;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_commom_view);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        btn_layout = findViewById(R.id.btn_layout);
        contentTxt = findViewById(R.id.content);
        titleTxt = (TextView) findViewById(R.id.title);
        submitTxt = (TextView) findViewById(R.id.submit);
        aloneTxt = findViewById(R.id.alone);
        cancelTxt = findViewById(R.id.cancel);
        setViewTxt();//设置文本信息
        onClickListener();//设置按钮监听
    }

    //设置文本信息
    private void setViewTxt() {
        //正文内容
        contentTxt.setText(content);
        contentTxt.setText(content);//设置EditText控件的内容
        contentTxt.setSelection(content.length());//将光标移至文字末尾

        //确认按钮内容
        if (!TextUtils.isEmpty(positiveName)) {
            submitTxt.setText(positiveName);
        } else {
            submitTxt.setText("" + ValuesGainUtil.getInstance().getValuesStr(mContext, R.string.affirm));
        }

        //取消按钮内容
        if (!TextUtils.isEmpty(negativeName)) {
            cancelTxt.setText(negativeName);
        } else {
            cancelTxt.setText("" + ValuesGainUtil.getInstance().getValuesStr(mContext, R.string.cancle));
        }

        //title
        if (!TextUtils.isEmpty(title)) {
            titleTxt.setText(title);
        }

        //单独按钮内容
        if (!TextUtils.isEmpty(aloneName)) {
            aloneTxt.setText(aloneName);
        }

        //是否设置单独按钮
        if (isAlone) {
            aloneTxt.setVisibility(View.VISIBLE);
            btn_layout.setVisibility(View.GONE);
        } else {
            aloneTxt.setVisibility(View.GONE);
            btn_layout.setVisibility(View.VISIBLE);
        }
    }

    //按钮事件监听
    private void onClickListener() {
        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnNegativeListener != null) {
                    mOnNegativeListener.onClick(MyEditDialog.this);
                }
            }
        });

        submitTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnPositiveListener != null) {
                    mOnPositiveListener.onClick(MyEditDialog.this);
                }
            }
        });

        aloneTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnAloneListener != null) {
                    mOnAloneListener.onClick(MyEditDialog.this);
                }
            }
        });
    }

    //取消监听
    private OnNegativeListener mOnNegativeListener;

    //确认监听
    private OnPositiveListener mOnPositiveListener;

    //单独按钮监听
    private OnAloneListener mOnAloneListener;

    //取消接口
    public interface OnNegativeListener {
        void onClick(Dialog dialog);
    }

    //确认接口
    public interface OnPositiveListener {
        void onClick(Dialog dialog);
    }

    //单独按钮接口
    public interface OnAloneListener {
        void onClick(Dialog dialog);
    }

    /**
     * 取消按钮监听
     *
     * @param negativeTxt
     * @param onNegativeListener
     * @return
     */
    public MyEditDialog setOnNegativeListener(String negativeTxt, OnNegativeListener onNegativeListener) {
        if (!TextUtils.isEmpty(negativeTxt)) {
            this.negativeName = negativeTxt;
        }
        this.isAlone = false;
        mOnNegativeListener = onNegativeListener;
        return this;
    }

    /**
     * 取消按钮监听
     *
     * @param negativeTxtId
     * @param onNegativeListener
     * @return
     */
    public MyEditDialog setOnNegativeListener(int negativeTxtId, OnNegativeListener onNegativeListener) {
        this.negativeName = ValuesGainUtil.getInstance().getValuesStr(mContext, negativeTxtId);
        this.isAlone = false;
        mOnNegativeListener = onNegativeListener;
        return this;
    }

    /**
     * 确认按钮监听
     *
     * @param positiveTxt
     * @param onPositiveListener
     * @return
     */
    public MyEditDialog setOnPositiveListener(String positiveTxt, OnPositiveListener onPositiveListener) {
        if (!TextUtils.isEmpty(positiveTxt)) {
            this.positiveName = positiveTxt;
        }
        this.isAlone = false;
        mOnPositiveListener = onPositiveListener;
        return this;
    }

    /**
     * 确认按钮监听
     *
     * @param positiveTxtId
     * @param onPositiveListener
     * @return
     */
    public MyEditDialog setOnPositiveListener(int positiveTxtId, OnPositiveListener onPositiveListener) {
        this.positiveName = ValuesGainUtil.getInstance().getValuesStr(mContext, positiveTxtId);
        this.isAlone = false;
        mOnPositiveListener = onPositiveListener;
        return this;
    }

    /**
     * 单独按钮监听
     *
     * @param aloneTxt
     * @param onAloneListener
     * @return
     */
    public MyEditDialog setOnAloneListener(String aloneTxt, OnAloneListener onAloneListener) {
        if (!TextUtils.isEmpty(aloneTxt)) {
            this.aloneName = aloneTxt;
        }
        this.isAlone = true;
        mOnAloneListener = onAloneListener;
        return this;
    }

    /**
     * 单独按钮监听
     *
     * @param aloneTxtId
     * @param onAloneListener
     * @return
     */
    public MyEditDialog setOnAloneListener(int aloneTxtId, OnAloneListener onAloneListener) {
        this.aloneName = ValuesGainUtil.getInstance().getValuesStr(mContext, aloneTxtId);
        this.isAlone = true;
        mOnAloneListener = onAloneListener;
        return this;
    }

    //获取编辑内容
    public String getEditContent() {
        return contentTxt.getText().toString();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        this.getWindow().setDimAmount(1f);
    }
}
