package com.jyx.mylibrary.base;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.jyx.mylibrary.R;
import com.jyx.mylibrary.annotation.ActivityFragmentInject;
import com.jyx.mylibrary.utils.StatusBarUtil;
import com.jyx.mylibrary.utils.StringUtils;
import com.jyx.mylibrary.utils.ToastUtils;
import com.jyx.mylibrary.utils.ValuesGainUtil;
import com.jyx.mylibrary.widget.dialog.BottomIconDialogView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by pc on 2016/12/8.
 */
public abstract class BaseActivity extends Activity {
    /**
     * The M content view id.
     */
    protected int mContentViewId;
    /**
     * Toolbar标题
     */
    private int mToolbarTitle;
    private int mNavigationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getClass().isAnnotationPresent(ActivityFragmentInject.class)) {
            ActivityFragmentInject annotation = getClass()
                    .getAnnotation(ActivityFragmentInject.class);
            mContentViewId = annotation.contentViewId();
            mToolbarTitle = annotation.toolbarTitle();
            mNavigationId = annotation.navigationId();
        } else {
            throw new RuntimeException(
                    "Class must add annotations of ActivityFragmentInitParams.class");
        }
        // 网络改变的一个回掉类

        setContentView(mContentViewId);
        setToolbar();
        ActivityManager.getInstance().addActivity(getSelfActivity());

        if (mNavigationId != -1) {
            getActionBar().setHomeAsUpIndicator(mNavigationId);
        }

        //设置通知栏颜色
        StatusBarUtil.setStatusBarColor(getSelfActivity(), false);
        initView();
        initData();
        initToolBar();
        initMethod();
    }

    /**
     * 初始化view
     */
    protected abstract void initView();

    /**
     * 初始化data
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
     * 获取当前Activity
     *
     * @return self activity
     */
    public BaseActivity getSelfActivity() {
        return this;
    }

    /**
     * 初始化toolBar
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setActionBar(toolbar);
            if (getActionBar() != null) {
                getActionBar().setDisplayShowTitleEnabled(false);
            }
            if (mToolbarTitle != -1) {
                setToolbarTitle(mToolbarTitle);
            }

            toolbar.setNavigationIcon(R.drawable.icon_white_back);
            //返回监听
            setToolbarBackListener();
        }
    }

    /**
     * Sets toolbar right icon.
     *
     * @param isShow the is show
     */
//右边图标是否可见
    public void setToolbarRightIcon(Boolean isShow) {
        ImageView rightIcon = findViewById(R.id.right_icon_iv);
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
     *
     * @param type the type
     */
//分享
    public void wxShare(int type) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().finishActivity(getSelfActivity());
    }

    /**
     * 设置toolBarTitle
     *
     * @param strId the str id
     */
    public void setToolbarTitle(int strId) {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getResources().getString(strId));
    }

    /**
     * 设置toolBarTitle
     *
     * @param strTitle the str title
     */
    public void setToolbarTitle(String strTitle) {
        TextView title = (TextView) findViewById(R.id.title);
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
     * 返回监听
     */
    public void setToolbarBackListener() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSelfActivity().finish();
                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                }
            });
        }
    }

    /**
     * toast
     *
     * @param msg the msg
     */
    public void toast(String msg) {
        ToastUtils.show(msg);
    }

    /**
     * toast
     *
     * @param id the id
     */
    public void toast(int id) {
        ToastUtils.show(id);
    }

    /**
     * getResources()
     *
     * @return
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    /**
     * 无Intent跳转到指定的activity
     *
     * @param forwordClass the forword class
     */
    public void startActivityForNoIntent(Class forwordClass) {
        Intent intent = new Intent(getSelfActivity(), forwordClass);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
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
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
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
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            getSelfActivity().finish();
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
