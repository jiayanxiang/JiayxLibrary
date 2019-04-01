package com.jyx.mylibrary.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toolbar;

import com.jyx.mylibrary.R;
import com.jyx.mylibrary.utils.StatusBarUtil;
import com.jyx.mylibrary.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jyx
 * @CTime 2017/10/16:20:23
 * @explain 底部Tab菜单
 */

public abstract class BaseMainTabActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    private View view;

    private FragmentTabHost fragmentTabHost;

    public int currentIndex = 0;

    public List<View> childViewList = new ArrayList<>();

    private Class<?>[] fragmentArray;

    private List<String> fragmentList = new ArrayList<>();

    public String[] mTextviewArray;

    private int[] mImageViewArray;

    public View toolBarView;

    public Toolbar mainToolBar;

    public TextView title;

    public LinearLayout search_ll;

    /**
     * 设置默认位置
     *
     * @return
     */
    protected abstract int setCurrentIndex();

    /**
     * 需要加载的Fragment集合
     *
     * @return
     */
    protected abstract Class<?>[] setFragmentArray();

    /**
     * 底部iText集合
     *
     * @return
     */
    protected abstract String[] setTextviewArray();

    /**
     * 底部ImageView集合
     *
     * @return
     */
    protected abstract int[] setImageViewArray();

    /**
     * 方法
     *
     * @return
     */
    protected abstract void initMethod();

    /**
     * 获取当前Activity
     *
     * @return
     */
    public BaseMainTabActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(getSelfActivity());
        setContentView(setContentView());

        StatusBarUtil.setStatusBarColor(getSelfActivity(), false);
        initView();

        initData();

        setViewData();

        initToolbar();

        initMethod();
    }

    public View setContentView() {
        view = LayoutInflater.from(this).inflate(R.layout.activity_main_tab, null);
        return view;
    }

    /**
     * 初始化控件
     */

    public void initView() {
        fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        toolBarView = findViewById(R.id.toolbar_view);
        mainToolBar = findViewById(R.id.toolbar);
        title = findViewById(R.id.title);
        search_ll = findViewById(R.id.search_ll);
    }

    /**
     * 初始化数据
     */
    public void initData() {
        mTextviewArray = setTextviewArray();
        fragmentArray = setFragmentArray();
        mImageViewArray = setImageViewArray();

        /**
         * 默认位置必须在有效范围之内
         */
        if (setCurrentIndex() >= 0 && setCurrentIndex() < fragmentArray.length) {
            currentIndex = setCurrentIndex();
        } else {
            currentIndex = 0;
        }

        /**
         * 三个集合大小必须一致,否则清空数组
         */
        if (mTextviewArray.length != fragmentArray.length || fragmentArray.length != mImageViewArray.length) {

            mTextviewArray = null;
            fragmentArray = null;
            mImageViewArray = null;
            return;
        }

        /**
         * 创建底部ChildView
         */
        createChildView(fragmentArray.length);
    }

    /**
     * 设置数据
     */
    private void setViewData() {
        fragmentList.clear();
        fragmentTabHost.setup(this, getSupportFragmentManager(), R.id.tabhost_framelayout);
        for (int i = 0; i < fragmentArray.length; i++) {
            TabHost.TabSpec tabSpec = fragmentTabHost.newTabSpec(mTextviewArray[i]).setIndicator(childViewList.get(i));
            fragmentList.add(tabSpec.getTag());
            fragmentTabHost.addTab(tabSpec, fragmentArray[i], null);
        }
    }

    /**
     * 创建ChildView
     *
     * @param size 可自定义布局在此方法中修改控件
     */
    private void createChildView(int size) {
        childViewList.clear();
        for (int i = 0; i < size; i++) {
            View childView = LayoutInflater.from(BaseMainTabActivity.this).inflate(R.layout.base_main_tab_view_item, null);
            ImageView imageView = (ImageView) childView.findViewById(R.id.imageview);
            imageView.setImageResource(mImageViewArray[i]);
            TextView textview = (TextView) childView.findViewById(R.id.textview);
            textview.setText(mTextviewArray[i]);
            childViewList.add(childView);
        }
    }

    /**
     * 获取当前选中的底部View
     *
     * @param index
     * @return
     */
    public View getTabItemView(int index) {
        if (fragmentArray != null) {
            if (index >= 0 && index < childViewList.size()) {
                return childViewList.get(index);
            }
        }
        return null;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (position == getCurrentIndex()) {
            return;
        }
        currentIndex = position;
        fragmentTabHost.setCurrentTab(currentIndex);
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 获取当前页面位置
     *
     * @return
     */
    public int getCurrentIndex() {
        currentIndex = fragmentTabHost.getCurrentTab();

        return currentIndex;
    }

    /**
     * 获取当前加载的主布局
     *
     * @return
     */
    public View getContentView() {
        if (view != null) {
            return view;
        }

        return null;
    }

    public void finishCurrentActivity() {
        ActivityManager.getInstance().finishCurrentActivity();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    /**
     * 无Intent跳转到指定的activity
     *
     * @param forwordClass
     */
    public void startAcitvityForNoIntent(Class forwordClass) {
        Intent intent = new Intent(getSelfActivity(), forwordClass);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    /**
     * 有Intent跳转到指定的activity
     *
     * @param forwordClass
     * @param intent
     */
    public void startActivityForIntent(Class forwordClass, Intent intent) {
        intent.setClass(getSelfActivity(), forwordClass);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    /**
     * 带数据跳转到指定的activity
     *
     * @param forwordClass
     * @param intent
     * @param requestCode
     */
    public void startActivityForResult(Class forwordClass, Intent intent, int requestCode) {
        intent.setClass(getSelfActivity(), forwordClass);
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().finishActivity(getSelfActivity());
    }


    @Override
    public Resources getResources() {
        Resources res = super.getResources();

        //设置字体大小问题
//        Configuration config = new Configuration();
//        config.setToDefaults();
//        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    //是否显示toolbarView
    public void isToolBarView(Boolean isShow) {
        if (isShow) {
            toolBarView.setVisibility(View.VISIBLE);
        } else {
            toolBarView.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化toolBar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setActionBar(toolbar);
            if (getActionBar() != null) {
                getActionBar().setDisplayShowTitleEnabled(false);
            }

            toolbar.setNavigationIcon(R.drawable.icon_white_back);
            //返回监听
            setToolbarBackListener();
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
     * 搜索是否显示，默认不显示
     *
     * @param isShow
     */
    public void setToolbarSearchShow(Boolean isShow) {
        if (search_ll != null) {
            if (isShow) {
                search_ll.setVisibility(View.VISIBLE);
            } else {
                search_ll.setVisibility(View.GONE);
            }
        }
    }

    public void setSelectTab(int position) {
        onPageSelected(position);
    }
}
