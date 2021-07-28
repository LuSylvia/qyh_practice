package com.example.module_common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;

import java.lang.ref.WeakReference;

public class BaseApplication extends Application {
    //private List<Activity> activityList;
    protected boolean isInMainProcess;
    protected int mainProcessPid;

    private boolean isBackground = false;
    protected static BaseApplication mInstance;
    private WeakReference<Activity> currentActivity;

    public int getMainProcessPid() {
        return mainProcessPid;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if (!isInMainProcess) {
            isInMainProcess = true;
            mainProcessPid = android.os.Process.myPid();

            registerActivityLifecycle();


            ARouter.init(this);//尽可能早，推荐在Application模块初始化
        }

        //activityList=new ArrayList<>();
    }
//    public void addActivity(Activity activity){
//        //判断当前集合中是否存在该Activity
//        if(!activityList.contains(activity)){
//            //不存在就加入
//            activityList.add(activity);
//        }
//    }
//    //销毁指定activity
//    public void removeActivity(Activity activity){
//        if(activityList.contains(activity)){
//            activityList.remove(activity);
//            activity.finish();
//        }
//    }
//    //销毁所有activity
//    public void removeAllActivity(){
//        for(Activity activity:activityList){
//            activity.finish();
//        }
//        activityList.clear();
//    }

    private void registerActivityLifecycle() {
        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                ActivityManager.getInstance().addActivity(activity);
                currentActivity = new WeakReference<>(activity);

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                if (isBackground) {

                }
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                currentActivity = new WeakReference<>(activity);
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                ActivityManager.getInstance().removeActivity(activity);
            }
        });
    }


    public static BaseApplication getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return mInstance.getApplicationContext();
    }


    public boolean isAppInBackground() {
        return isBackground;
    }

    public Activity getCurrentActivity() {
        Activity activity = null;
        if (currentActivity != null) {
            activity = currentActivity.get();
        }
        if (activity == null) {
            activity = ActivityManager.getInstance().getCurrentActivity();
        }

        return activity;
    }

}
