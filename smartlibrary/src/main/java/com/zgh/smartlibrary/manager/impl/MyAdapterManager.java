package com.zgh.smartlibrary.manager.impl;

import android.content.Context;
import android.text.TextUtils;
import android.widget.BaseAdapter;


import com.zgh.smartlibrary.adapter.SmartAdapter;
import com.zgh.smartlibrary.config.AbsListFragmentConfig;
import com.zgh.smartlibrary.json.JsonAnalysisEngine;
import com.zgh.smartlibrary.manager.AdapterManager;
import com.zgh.smartlibrary.util.FileUtil;
import com.zgh.smartlibrary.util.GsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuguohui on 2016/9/6.
 */
public class MyAdapterManager implements AdapterManager {
    BaseAdapter mAdapter;
    List mData = new ArrayList();
    Context mContext;
    private List<SmartAdapter.SmartViewHolder> holders = new ArrayList<>();
    protected JsonAnalysisEngine jsonAnalysisEngine;
    private List<DataModifiers> dataModifiersList;
    private String requestURL = "";

    public MyAdapterManager(Context context, String configPath) {
        mContext = context;
        analysisConfig(configPath);
        mAdapter = new SmartAdapter<Object>(context, mData, holders) {
        };
    }

    private void analysisConfig(String config) {
        AbsListFragmentConfig mConfig = GsonUtil.jsonToBean(FileUtil.readRaw(mContext, config), AbsListFragmentConfig.class);
        if (mConfig == null) {
            throw new RuntimeException("解析配置失败！");
        }
        jsonAnalysisEngine = new JsonAnalysisEngine(mConfig.getJsonConfigs());
        for (String holderName : mConfig.getHolders()) {
            try {
                Class clazz = Class.forName(holderName);
                SmartAdapter.SmartViewHolder holder = (SmartAdapter.SmartViewHolder) clazz.newInstance();
                holders.add(holder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(mConfig.getUrl())) {
            requestURL = mConfig.getUrl();
        }
    }

    @Override
    public BaseAdapter getAdapter() {
        return mAdapter;
    }

    int dataSize;

    @Override
    public int update(String jsonData, boolean update) {
        List<Object> data = jsonAnalysisEngine.getData(jsonData);
        if (update) {
            mData.clear();
            dataSize = 0;
        }
        data = modifyListData(data, update);
        mData.addAll(data);
        mAdapter.notifyDataSetChanged();
        int size = jsonAnalysisEngine.getListDataSize();
        dataSize += size;
        return size;
    }

    @Override
    public void addDataModifiers(DataModifiers... dataModifiers) {
        if (dataModifiers==null||dataModifiers.length == 0) {
            return;
        }
        if (dataModifiersList == null) {
            dataModifiersList = new ArrayList<>();
        }
        for (DataModifiers modifiers : dataModifiers) {
            dataModifiersList.add(modifiers);
        }

    }

    @Override
    public void removeDataModifiers(DataModifiers dataModifiers) {
        if (dataModifiersList != null) {
            dataModifiersList.remove(dataModifiers);
            if (dataModifiersList.size() == 0) {
                dataModifiersList = null;
            }
        }
    }

    @Override
    public String getRequestURL() {
        return requestURL;
    }

    @Override
    public int getDataSize() {
        return dataSize;
    }

    private List<Object> modifyListData(List<Object> data, boolean update) {
        List<Object> result = data;
        if (dataModifiersList != null) {
            for (DataModifiers dataModifiers : dataModifiersList) {
                result = dataModifiers.modifyData(result, update);
            }
        }
        return result;
    }


}
