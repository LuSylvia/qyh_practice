package com.example.module_common.activity;

import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.trello.rxlifecycle3.RxLifecycle;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.android.RxLifecycleAndroid;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public abstract class MyRxAppCompatActivity extends AppCompatActivity implements LifecycleProvider<ActivityEvent> {
    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

    @NotNull
    @Override
    public Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.hide();
    }

    @NotNull
    @Override
    public <T> LifecycleTransformer<T> bindUntilEvent(@NotNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @NotNull
    @Override
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }

    @Override
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(ActivityEvent.CREATE);
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
    }

    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    @CallSuper
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }

    @Override
    @CallSuper
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
    }


}
