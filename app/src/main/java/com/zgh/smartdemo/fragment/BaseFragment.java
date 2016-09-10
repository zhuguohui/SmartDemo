package com.zgh.smartdemo.fragment;

import com.zgh.smartdemo.page.DemoPagePolicy;
import com.zgh.smartlibrary.fragment.SmartListFragment;
import com.zgh.smartlibrary.page.IPagePolicy;

/**
 * Created by zhuguohui on 2016/9/10 0010.
 */
public abstract class BaseFragment extends SmartListFragment {
    @Override
    protected IPagePolicy getPagePolicy() {
        return new DemoPagePolicy();
    }
}
