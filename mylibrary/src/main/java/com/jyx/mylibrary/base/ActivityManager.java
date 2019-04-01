package com.jyx.mylibrary.base;

import android.app.Activity;
import android.util.Log;

import com.jyx.mylibrary.utils.JsonUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * @author jyx
 * @CTime 2017/10/16:20:23
 * @explain Activity管理类
 */

public class ActivityManager {

    /**
     * Activity集合
     */
    public List<Activity> mActivityList = new LinkedList<>();

    /**
     * ActivityManager类实例
     */
    private static ActivityManager instance = null;


    private ActivityManager() {

    }

    /**
     * 获取ActivityManager实例instance
     *
     * @return
     */
    public static ActivityManager getInstance() {
        if (null == instance) {
            instance = new ActivityManager();
        }
        return instance;
    }

    /**
     * 添加一个Activity到mActivityList集合当中
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (null != mActivityList) {
            mActivityList.add(activity);
            Log.e("activityManager",activity.getClass().getName());
            for (int i = 0; i < mActivityList.size(); i++) {
                Activity activity1 = mActivityList.get(i);
                Log.e("activityManager",activity1.getClass().getName());
            }
        }
    }

    /**
     * 从mActivityList集合当中移除当前的Activity
     *
     * @return
     */
    public Activity removeActivity() {
        return mActivityList.isEmpty() ? null : mActivityList.remove(mActivityList.size() - 1);
    }

    /**
     * Activity集合当中的第一个Activity
     *
     * @return
     */
    public Activity firstActivity() {
        return mActivityList.isEmpty() ? null : mActivityList.get(0);
    }

    /**
     * Activity集合当中的最后一个Activity
     *
     * @return
     */
    public Activity lastActivity() {
        return mActivityList.isEmpty() ? null : mActivityList.get(mActivityList.size() - 1);
    }

    /**
     * 释放所有的Activity
     */
    public void finishAllActivity() {
        if (!mActivityList.isEmpty()) {
            for (Activity activity : mActivityList) {
                activity.finish();
            }
        }
        mActivityList.clear();
    }

    /**
     * 释放当前Activity
     */
    public void finishCurrentActivity() {
        if (!mActivityList.isEmpty()) {
            removeActivity().finish();
        }
    }

    /**
     * 释放指定的Activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (!mActivityList.isEmpty()) {
            Log.e("activityManager",activity.getClass().getName());
            activity.finish();
            mActivityList.remove(activity);
            for (int i = 0; i < mActivityList.size(); i++) {
                Activity activity1 = mActivityList.get(i);
                Log.e("activityManager",activity1.getClass().getName());
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : mActivityList) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public Activity getActivity(Class<?> cls) {
        for (Activity activity : mActivityList) {
            if (activity.getClass().equals(cls)) {
               return activity;
            }
        }
        return null;
    }

    /**
     * 获取当前Activity
     *
     * @return
     */
    public Activity getCurrentActivity() {
        return mActivityList.isEmpty() ? null : mActivityList.get(mActivityList.size() - 1);
    }

}

