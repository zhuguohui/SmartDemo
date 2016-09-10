package com.zgh.smartlibrary.config;

/**
 * Created by zhuguohui on 2016/8/17.
 */
public class JsonAnalysisConfig {
    //用于表示json所在位置
    private String jsonLocation;
    //解析成什么类
    private String type;
    //是否是列表数据
    private boolean isListData = false;

    public String getJsonLocation() {
        return jsonLocation;
    }

    public void setJsonLocation(String location) {
        this.jsonLocation = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public boolean isListData() {
        return isListData;
    }

    public void setListData(boolean listData) {
        isListData = listData;
    }
}

