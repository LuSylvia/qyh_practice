package com.example.module_common;

import android.content.Context;

import com.trello.rxlifecycle3.LifecycleProvider;

public interface BaseView {
    LifecycleProvider getLifecycleProvider();

    Context getContext();

    void showNetErrorView();

}
