package com.zgh.smartdemo.fragment;

import com.zgh.smartdemo.modifiters.NewsDataModifiers;
import com.zgh.smartlibrary.manager.AdapterManager;
import com.zgh.smartlibrary.net.NetRequest;
import com.zgh.smartlibrary.net.NetRequestModifiers;

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

    @Override
    protected void initNetRequestModifiers() {
        addNetRequestModifiers(new NetRequestModifiers() {
            @Override
            public NetRequest modifyNetRequest(NetRequest request) {
                return request;
            }
        });
    }
}
