package com.jyx.mylibrary.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.jyx.mylibrary.R;
import com.jyx.mylibrary.bean.BottomIconBean;
import com.jyx.mylibrary.utils.StringUtils;
import com.jyx.mylibrary.widget.dialog.adapter.BottomIconDialogListAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession;
import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneTimeline;

/**
 * @author jyx
 * @CTime 2019/3/6
 * @explain:
 */
public class BottomIconDialogView extends Dialog {
    private boolean iscancelable = true;//控制点击dialog外部是否dismiss
    private boolean isBackCancelable = true;//控制返回键是否dismiss
    private View view;
    private Context context; //这里的view其实可以替换直接传layout过来的 因为各种原因没传(lan)
    private GridView listView;
    private TextView cancleTxt;
    private List<BottomIconBean> bottomIconBeanArrayList = new ArrayList<>();
    private BottomIconDialogListAdapter adapter;
    private Boolean isDefault = false;

    public BottomIconDialogView(Context context, List<BottomIconBean> stringList) {
        super(context, R.style.bottomDialog);
        this.context = context;
        this.bottomIconBeanArrayList.clear();
        if (!StringUtils.isObjectEmpty(stringList)) {
            this.bottomIconBeanArrayList.addAll(stringList);
        }
        initMethod();
        this.show();
    }

    public BottomIconDialogView(Context context, List<BottomIconBean> stringList, Boolean isDefault) {
        super(context, R.style.bottomDialog);
        this.context = context;
        this.bottomIconBeanArrayList.clear();
        if (!StringUtils.isObjectEmpty(stringList)) {
            this.bottomIconBeanArrayList.addAll(stringList);
        }
        this.isDefault = isDefault;
        initMethod();
        this.show();
    }

    public BottomIconDialogView(Context context, List<BottomIconBean> stringList, boolean isCancelable, boolean isBackCancelable) {
        super(context, R.style.bottomDialog);
        this.context = context;
        this.bottomIconBeanArrayList.clear();
        if (!StringUtils.isObjectEmpty(stringList)) {
            this.bottomIconBeanArrayList.addAll(stringList);
        }
        this.iscancelable = isCancelable;
        this.isBackCancelable = isBackCancelable;
        initMethod();
        this.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);//这行一定要写在前面
        setCancelable(iscancelable);//点击外部不可dismiss
        setCanceledOnTouchOutside(isBackCancelable);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.setWindowAnimations(R.style.myBottomStyle);  //添加动画
    }

    //初始化所有方法
    private void initMethod() {
        initView();
        initViewData();
        initListener();
    }

    //初始化控件
    private void initView() {
        this.view = LayoutInflater.from(context).inflate(R.layout.bottom_icon_dialog_view, null);
        listView = view.findViewById(R.id.bottom_list_view);
        cancleTxt = view.findViewById(R.id.bottom_cancle_txt);
    }

    //初始化数据
    private void initViewData() {
        if (bottomIconBeanArrayList.size() == 0) {
            if (isDefault) {
                BottomIconBean bottomIconBean1 = new BottomIconBean("分享给好友", R.drawable.share_wx_firend_icon, WXSceneSession);
                BottomIconBean bottomIconBean2 = new BottomIconBean("分享到朋友圈", R.drawable.share_wx_cricle_icon, WXSceneTimeline);
                bottomIconBeanArrayList.add(bottomIconBean1);
                bottomIconBeanArrayList.add(bottomIconBean2);
            }
        }
        listView.setNumColumns(2);
        adapter = new BottomIconDialogListAdapter(context, bottomIconBeanArrayList);
        listView.setAdapter(adapter);
    }

    //初始化事件监听
    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BottomIconBean bottomIconBean = bottomIconBeanArrayList.get(position);
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(position, bottomIconBean.getType());
                }
            }
        });

        cancleTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(int position, int type);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int height = listView.getTop();
        int y = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (y < height) {
                dismiss();
            }
        }
        return super.onTouchEvent(event);
    }
}
