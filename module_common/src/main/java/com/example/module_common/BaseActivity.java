package com.example.module_common;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.trello.rxlifecycle3.LifecycleProvider;

public abstract class BaseActivity extends MyRxAppCompatActivity implements BaseView {
    //是否显示标题栏，默认为true
    private boolean isShowTitle = true;
    //是否显示状态栏，默认为true
    private boolean isShowState = true;
    //是否输出日志信息
    private boolean isDebug;


    /**
     * 设置屏幕横竖屏切换
     *
     * @param screenRoate true  竖屏     false  横屏
     */
    private void setScreenRoate(Boolean screenRoate) {
        if (screenRoate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置竖屏模式
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setScreenRoate(true);
        if (!isShowTitle) {
            //不显示标题栏
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }


        initView();
        initData();

    }


    //顺序：视图初始化->数据初始化

    //设置布局
    public abstract int initLayout();

    //视图初始化
    public abstract void initView();

    //数据初始化
    public abstract void initData();


    /**
     * 设置是否显示标题栏
     */
    public void setTitle(boolean isShowTitle) {
        this.isShowTitle = isShowTitle;
    }

    /**
     * 设置是否显示状态栏
     */
    public void setState(boolean isShowState) {
        this.isShowState = isShowState;
    }

    /**
     * 展示长时间的Toast
     *
     * @param msg
     */
    public void showLongToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示短时间的Toast
     *
     * @param msg
     */
    public void showShortToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }


    //以下为BaseView中的接口实现
    @Override
    public LifecycleProvider getLifecycleProvider() {
        return this;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showNetErrorView() {

    }
}
