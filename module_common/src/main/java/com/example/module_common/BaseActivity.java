package com.example.module_common;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.module_common.broadcast.ForceOfflineReceiver;
import com.trello.rxlifecycle3.LifecycleProvider;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public abstract class BaseActivity extends MyRxAppCompatActivity implements BaseView {
    private static final String TAG = BaseActivity.class.getSimpleName();
    public TextView tv_main_title, tv_sub_title;
    private Toolbar toolbar;

    //是否显示标题栏，默认为true
    private boolean isShowTitle = true;
    //是否显示状态栏，默认为true
    private boolean isShowState = true;
    //是否输出日志信息
    private boolean isDebug;

    //强制下线的广播接收器
    private ForceOfflineReceiver receiver;


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

    public void findView() {
        toolbar = findViewById(R.id.toolbar);
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_sub_title = findViewById(R.id.tv_sub_title);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //屏蔽系统的标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setScreenRoate(true);
        //findView();
        if (toolbar != null) {
            //将toolbar显示到界面
            setSupportActionBar(toolbar);
        }
        if (tv_main_title != null) {
            //getTitle得到的值是activity:label的属性
            tv_main_title.setText(getTitle());
        }
        Interceptor interceptor=new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request=chain.request().newBuilder()
                        .header("headname","value")
                        .build();
                return null;
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        //判断是否有Toolbar，并默认显示返回按钮
        if (null != getToolbar() && isShowBacking()) {
            showBack();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.qyh_practice.broadcast.FORCE_OFFLINE");
        receiver = new ForceOfflineReceiver();
        registerReceiver(receiver, intentFilter);

    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private void showBack() {
        //setNavigationIcon必须在setSupportActionBar(toolbar);方法后面加入
        //getToolbar().setNavigationIcon(R.mipmap.icon_back);
        //getToolbar().setNavigationOnClickListener(v -> onBackPressed());
    }


    public Toolbar getToolbar() {
        return findViewById(R.id.toolbar);
    }

    /**
     * 设置主标题文字
     *
     * @param title
     */
    public void setMainTitle(CharSequence title) {
        if (toolbar != null) {
            toolbar.setTitle(title);
        } else {
            getToolbar().setTitle(title);
            setSupportActionBar(getToolbar());
        }
    }

    public TextView getTv_main_title() {
        return tv_main_title;
    }

    public TextView getTv_sub_title() {
        return tv_sub_title;
    }

    /**
     * 是否显示返回按钮，默认显示
     * 子类可重写该方法
     *
     * @return true
     */
    protected boolean isShowBacking() {
        return true;
    }

    //顺序：视图初始化->数据初始化

    /**
     * 设置layout布局，子类必须重写该方法
     *
     * @return res Layout xml id
     */
    //protected abstract int getLayoutId();


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
