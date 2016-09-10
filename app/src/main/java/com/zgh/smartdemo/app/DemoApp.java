package com.zgh.smartdemo.app;

import android.app.Application;

import com.zgh.smartdemo.R;
import com.zgh.smartlibrary.retry.LoadingAndRetryManager;

/**
 * Created by zhuguohui on 2016/9/10 0010.
 */
public class DemoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LoadingAndRetryManager.BASE_RETRY_LAYOUT_ID = R.layout.base_retry;
        LoadingAndRetryManager.BASE_LOADING_LAYOUT_ID = R.layout.base_loading;
        LoadingAndRetryManager.BASE_EMPTY_LAYOUT_ID = R.layout.base_empty;
    }
}
