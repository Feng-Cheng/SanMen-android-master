package com.test.ydool.sanmen.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by Administrator on 2018/4/24.
 */

public class ScreenManager {

    private static Stack<Activity> mActivityStack;
    private static volatile ScreenManager mScreenManager;

    private ScreenManager(){}

    public static ScreenManager getInstance(){
        if(mScreenManager == null){
            synchronized (ScreenManager.class){
                if(mScreenManager == null){
                    mScreenManager = new ScreenManager();
                }
            }
        }
        return mScreenManager;
    }
    /**
     * 移除栈顶activity
     */
    public void popActivity(Activity activity){
        if(activity != null){
            activity.finish();
            mActivityStack.remove(activity);
            activity = null;
        }
    }

    public Activity currentActivity(){
        Activity mActivity = mActivityStack.lastElement();
        return mActivity;
    }

    /**
     * 将activity入栈
     */
    public void pushActivity(Activity activity){
        if(mActivityStack == null){
            mActivityStack = new Stack<Activity>();
        }
        mActivityStack.add(activity);
    }

    /**
     * 移除栈顶到cls之间的activity
     */
    public void popAllActivityExceptOne(Class cls){
        while(true){
            Activity activity = currentActivity();
            if(activity == null){
                break;
            }
            if(activity.getClass().equals(cls)){
                break;
            }
            popActivity(activity);
        }

    }
}
