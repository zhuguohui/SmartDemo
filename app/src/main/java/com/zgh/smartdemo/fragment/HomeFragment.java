package com.zgh.smartdemo.fragment;

import com.zgh.smartdemo.modifiters.NewsDataModifiers;
import com.zgh.smartlibrary.manager.AdapterManager;

/**
 * Created by zhuguohui on 2016/9/10 0010.
 */
public class HomeFragment extends BaseFragment {
    @Override
    protected String getConfig() {
        return "config_home";
    }

    @Override
    protected AdapterManager.DataModifiers[] getDataModifiers() {
        //过滤数据
        return new AdapterManager.DataModifiers[]{new NewsDataModifiers()};
    }
}
