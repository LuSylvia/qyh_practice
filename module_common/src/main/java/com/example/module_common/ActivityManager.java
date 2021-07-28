package com.example.module_common;

import android.app.Activity;

import com.example.module_common.adapter.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ActivityManager {
    private Stack<Activity> activityStack;

    private ActivityManager() {
    }

    //静态内部类实现单例
    private static class Singleton {
        public static ActivityManager ourInstance = new ActivityManager();
    }


    public static ActivityManager getInstance() {
        return Singleton.ourInstance;
    }

    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    public void removeActivity(Class<?> cls) {
        if (activityStack != null) {
            for (int i = (activityStack.size() - 1); i >= 0; i--) {
                if (isActivityClassOf(activityStack.get(i), cls)) {
                    activityStack.get(i).finish();
                    activityStack.remove(i);
                }
            }
        }
    }

    public void removeActivity(Activity activity) {
        if (activityStack == null) {
            return;
        }
        activityStack.remove(activity);
        if (!activity.isFinishing()) {
            activity.finish();
        }
    }

    public Activity getCurrentActivity() {
        if (activityStack == null || activityStack.isEmpty()) {
            return null;
        }
        return activityStack.lastElement();
    }

    public Activity getPreActivity() {
        if (activityStack == null || activityStack.size() < 2) {
            return null;
        }
        return activityStack.elementAt(activityStack.size() - 2);
    }

    public void closeAllActivity() {
        if (activityStack == null) {
            return;
        }
        while (!activityStack.isEmpty()) {
            Activity activity = activityStack.pop();
            if (activity != null) {
                activity.finish();
            }
        }
    }

    public void closeOtherActivity(String... activityNames) {
        if (activityNames == null || activityNames.length == 0 || activityStack == null) {
            return;
        }
        List<Activity> list = new ArrayList<>();
        while (!activityStack.isEmpty()) {
            Activity activity = activityStack.pop();
            if (activity == null) {
                continue;
            }
            if (isSafeActivity(activityNames, activity)) {
                list.add(activity);
            } else {
                activity.finish();
            }
        }
        if (list.size() > 0) {
            activityStack.addAll(list);
        }
    }

    private boolean isSafeActivity(String[] activityNames, Activity activity) {
        for (String name : activityNames) {
            if (name == null) {
                continue;
            }
            String s = activity.getClass().getName();
            if (activity.getClass().getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public int getCount() {
        return activityStack == null ? 0 : activityStack.size();
    }


    public boolean contains(Class<?> cls) {
        if (activityStack != null) {
            for (int i = 0, j = activityStack.size(); i < j; i++) {
                if (isActivityClassOf(activityStack.get(i), cls)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void currentActivityLimitCount(Class<?> cls, int count) {
        if (activityStack != null) {
            int tempCount = 0;
            for (int i = (activityStack.size() - 1); i >= 0; i--) {
                if (isActivityClassOf(activityStack.get(i), cls)) {
                    tempCount++;
                    if (tempCount > count) {
                        activityStack.get(i).finish();
                        activityStack.remove(i);
                    }
                }
            }
        }
    }

    public boolean isCurrentActivityClassOf(Class<?> cls) {
        Activity baseActivity = getCurrentActivity();
        return isActivityClassOf(baseActivity, cls);
    }

    public <T extends Activity> T getCurrentActivityClassOf(Class<T> cls) {
        Activity baseActivity = getCurrentActivity();
        if (isActivityClassOf(baseActivity, cls)) {
            return (T) baseActivity;
        } else {
            return null;
        }
    }

    public boolean isActivityClassOf(Activity activity, Class<?> cls) {
        return activity != null && cls != null && cls.getName() != null && cls.getName().equals(activity.getClass().getName());
    }

    /**
     * 获得栈里最上面的BaseActivity
     */
    public BaseActivity getTopBaseActivity() {
        if (activityStack == null || activityStack.isEmpty()) {
            return null;
        }
        Activity activity = activityStack.lastElement();
        if (activity instanceof BaseActivity) {
            return (BaseActivity) activity;
        }
        int size = activityStack.size();
        for (int i = size - 1; i >= 0; i--) {
            Activity temp = activityStack.get(i);
            if (temp instanceof BaseActivity) {
                return (BaseActivity) temp;
            }
        }
        return null;
    }

    public List<Activity> getActivities(Class<?> cls) {
        List<Activity> result = null;
        if (activityStack != null) {
            result = new ArrayList<>();
            for (Activity activity : activityStack) {
                if (isActivityClassOf(activity, cls)) {
                    result.add(activity);
                }
            }
        }
        return result;
    }

    public <T extends Activity> T getSpecialActivity(Class<T> cls) {
        T activity = null;
        if (activityStack != null) {
            for (int i = 0, j = activityStack.size(); i < j; i++) {
                if (isActivityClassOf(activityStack.get(i), cls)) {
                    activity = (T) activityStack.get(i);
                    break;
                }
            }
        }
        return activity;
    }
}
