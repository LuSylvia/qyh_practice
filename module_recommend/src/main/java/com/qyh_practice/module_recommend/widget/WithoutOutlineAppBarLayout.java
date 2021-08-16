package com.qyh_practice.module_recommend.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.google.android.material.appbar.AppBarLayout;

public class WithoutOutlineAppBarLayout extends AppBarLayout {


    public WithoutOutlineAppBarLayout(Context context) {
        this(context, null);
    }

    public WithoutOutlineAppBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        if (Build.VERSION.SDK_INT >= 21) {
            // remove the bounds view outline provider
            setOutlineProvider(null);
        }
    }


}
