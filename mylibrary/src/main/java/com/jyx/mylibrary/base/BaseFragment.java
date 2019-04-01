package com.jyx.mylibrary.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.jyx.mylibrary.R;
import com.jyx.mylibrary.annotation.ActivityFragmentInject;
import com.jyx.mylibrary.utils.StatusBarUtil;
import com.jyx.mylibrary.utils.StringUtils;
import com.jyx.mylibrary.utils.ToastUtils;
import com.jyx.mylibrary.utils.ValuesGainUtil;
import com.jyx.mylibrary.widget.dialog.BottomIconDialogView;
import com.jyx.mylibrary.widget.dialog.MyDialog;


/**
 * Created by PC-20160514 on 2016/5/18.
 */
public abstract class BaseFragment extends Fragment {
    /**
     * The Tag.
     */
    public final String TAG = this.getClass().getName();

    /**
     * The Root view.
     */
    protected View rootView;
    /**
     * The M content view id.
     */
    protected int mContentViewId;
    /**
     * The Is visible to user.
     */
    public Boolean isVisibleToUser = false;
    /**
     * The Is first loading.
     */
    public Boolean isFirstLoading = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (null == rootView) {
            if (getClass().isAnnotationPresent(ActivityFragmentInject.class)) {
                ActivityFragmentInject annotation = getClass()
                        .getAnnotation(ActivityFragmentInject.class);
                mContentViewId = annotation.contentViewId();
            } else {
                throw new RuntimeException(
                        "Class must add annotations of ActivityFragmentInitParams.class");
            }
            rootView = inflater.inflate(mContentViewId, null);
            initView(rootView);
        }

        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //设置通知栏颜色
        StatusBarUtil.setStatusBarColor(getSelfActivity(), false);
        initData();
        initToolBar();
        initMethod();
        setToolbarBackListener();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoading) {
            isFirstLoading = false;
            firstLoading();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 设置fragment布局
     *
     * @param rootView the root view
     */
    protected abstract void initView(View rootView);

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化toolBar
     */
    protected abstract void initToolBar();

    /**
     * 初始化方法
     * 所有的自定义方法全部在此处调用
     */
    protected abstract void initMethod();

    /**
     * 第一次加载
     */
    public void firstLoading() {

    }

    /**
     * 获取当前Activity
     *
     * @return self activity
     */
    public Activity getSelfActivity() {
        return getActivity();
    }

    /**
     * 设置toolBarTitle
     *
     * @param view  the view
     * @param strId the str id
     */
    public void setToolbarTitle(View view, int strId) {
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(getResources().getString(strId));
    }

    /**
     * 设置toolBarTitle
     *
     * @param view     the view
     * @param strTitle the str title
     */
    public void setToolbarTitle(View view, String strTitle) {
        TextView title = (TextView) view.findViewById(R.id.title);
        if (StringUtils.isObjectEmpty(strTitle)) {
            title.setText("");
        } else {
            title.setText(strTitle);
        }
    }

    /**
     * 设置toolbar背景色
     *
     * @param toolbar         the toolbar
     * @param backgroundColor the background color
     */
    public void setToolBarBackgroundColor(Toolbar toolbar, int backgroundColor) {
        if (StringUtils.isObjectEmpty(toolbar)) {
            return;
        } else {
            toolbar.setBackgroundColor(ValuesGainUtil.getInstance().getValuesColor(getSelfActivity(), backgroundColor));
        }
    }

    /**
     * 搜索是否显示，默认不显示
     *
     * @param isShow the is show
     */
    public void setToolbarSearchShow(Boolean isShow) {
        LinearLayout search_ll = rootView.findViewById(R.id.search_ll);
        if (search_ll != null) {
            if (isShow) {
                search_ll.setVisibility(View.VISIBLE);
            } else {
                search_ll.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Sets toolbar right icon.
     *
     * @param isShow the is show
     */
//右边图标是否可见
    public void setToolbarRightIcon(Boolean isShow) {
        ImageView rightIcon = rootView.findViewById(R.id.right_icon_iv);
        if (rightIcon != null) {
            if (isShow) {
                rightIcon.setVisibility(View.VISIBLE);
            } else {
                rightIcon.setVisibility(View.GONE);
            }
            rightIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final BottomIconDialogView bottomIconDialogView = new BottomIconDialogView(getSelfActivity(), null, true);
                    bottomIconDialogView.setOnItemClickListener(new BottomIconDialogView.OnItemClickListener() {
                        @Override
                        public void onClick(int position, int type) {
                            bottomIconDialogView.dismiss();
                            wxShare(type);
                        }
                    });
                }
            });
        }
    }

    /**
     * Wx share.
     */
//分享
    public void wxShare(int type) {

    }

    /**
     * 返回监听
     */
    public void setToolbarBackListener() {
        if (StringUtils.isObjectEmpty(rootView)) {
            return;
        }
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSelfActivity().finish();
                    getSelfActivity().overridePendingTransition(R.anim.right_in, R.anim.right_out);
                }
            });
        }
    }

    /**
     * 初始化toolBar
     */
    public void setInitToolbar() {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.icon_white_back);
            //返回监听
            setToolbarBackListener();
        }
    }

    /**
     * Toast.
     *
     * @param msg the msg
     */
    public void toast(String msg) {
        ToastUtils.show(msg);
    }

    /**
     * Toast.
     *
     * @param id the id
     */
    public void toast(int id) {
        ToastUtils.show(id);
    }

    /**
     * 无Intent跳转到指定的activity
     *
     * @param forwordClass the forword class
     */
    public void startAcitvityForNoIntent(Class forwordClass) {
        Intent intent = new Intent(getSelfActivity(), forwordClass);
        startActivity(intent);
        getSelfActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    /**
     * 有Intent跳转到指定的activity
     *
     * @param forwordClass the forword class
     * @param intent       the intent
     */
    public void startActivityForIntent(Class forwordClass, Intent intent) {
        intent.setClass(getSelfActivity(), forwordClass);
        startActivity(intent);
        getSelfActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    /**
     * 带数据跳转到指定的activity
     *
     * @param forwordClass the forword class
     * @param intent       the intent
     * @param requestCode  the request code
     */
    public void startActivityForResult(Class forwordClass, Intent intent, int requestCode) {
        intent.setClass(getSelfActivity(), forwordClass);
        startActivityForResult(intent, requestCode);
        getSelfActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    /**
     * 信息提示
     *
     * @param strTitle   the str title
     * @param strContent the str content
     */
    public void showDilog(String strTitle, String strContent) {
        MyDialog myDialog = new MyDialog(getSelfActivity());
        myDialog.setTitle(StringUtils.isObjectEmpty(strTitle) ? getString(R.string.information_tips) : strTitle);
        myDialog.setContent(strContent);
        myDialog.setOnNegativeListener(R.string.cancle, new MyDialog.OnNegativeListener() {
            @Override
            public void onClick(Dialog dialog) {
                dialog.dismiss();
            }
        }).setOnPositiveListener(R.string.affirm, new MyDialog.OnPositiveListener() {
            @Override
            public void onClick(Dialog dialog) {
                dialog.dismiss();
            }
        }).create();
        myDialog.show();
    }

    /**
     * 信息提示
     *
     * @param strContent the str content
     */
    public void showDilog(String strContent) {
        MyDialog myDialog = new MyDialog(getSelfActivity());
        myDialog.setTitle(getString(R.string.information_tips));
        myDialog.setContent(strContent);
        myDialog.setOnNegativeListener(R.string.cancle, new MyDialog.OnNegativeListener() {
            @Override
            public void onClick(Dialog dialog) {
                dialog.dismiss();
            }
        }).setOnPositiveListener(R.string.affirm, new MyDialog.OnPositiveListener() {
            @Override
            public void onClick(Dialog dialog) {
                dialog.dismiss();
            }
        }).create();
        myDialog.show();
    }
}
