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
import android.widget.ListView;
import android.widget.TextView;

import com.jyx.mylibrary.R;
import com.jyx.mylibrary.widget.dialog.adapter.BottomDialogListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jyx
 * @CTime 2019/3/6
 * @explain:
 */
public class BottomDialogView extends Dialog {
    private boolean iscancelable = true;//控制点击dialog外部是否dismiss
    private boolean isBackCancelable = true;//控制返回键是否dismiss
    private View view;
    private Context context; //这里的view其实可以替换直接传layout过来的 因为各种原因没传(lan)
    private ListView listView;
    private TextView cancleTxt;
    private List<String> stringList = new ArrayList<>();
    private BottomDialogListAdapter adapter;

    public BottomDialogView(Context context, List<String> stringList) {
        super(context, R.style.bottomDialog);
        this.context = context;
        this.stringList.clear();
        this.stringList.addAll(stringList);
        initMethod();
        this.show();
    }

    public BottomDialogView(Context context, List<String> stringList, boolean isCancelable, boolean isBackCancelable) {
        super(context, R.style.bottomDialog);
        this.context = context;
        this.stringList.clear();
        this.stringList.addAll(stringList);
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
        this.view = LayoutInflater.from(context).inflate(R.layout.bottom_dialog_view, null);
        listView = view.findViewById(R.id.bottom_list_view);
        cancleTxt = view.findViewById(R.id.bottom_cancle_txt);
    }

    //初始化数据
    private void initViewData() {
        adapter = new BottomDialogListAdapter(context, stringList);
        listView.setAdapter(adapter);
    }

    //初始化事件监听
    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(position);
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
        void onClick(int position);
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
