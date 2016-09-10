package com.zgh.smartlibrary.config;

import java.util.List;

/**
 * Created by 用于配置adapter on 2016/8/19.
 */
public class AbsListFragmentConfig {
    //数据的地址
    private String url;
    //需要用到的SmartHolder的名字
    private List<String> holders;
    //json解析的配置
    private List<JsonAnalysisConfig> jsonConfigs;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getHolders() {
        return holders;
    }

    public void setHolders(List<String> holders) {
        this.holders = holders;
    }

    public List<JsonAnalysisConfig> getJsonConfigs() {
        return jsonConfigs;
    }

    public void setJsonConfigs(List<JsonAnalysisConfig> jsonConfigs) {
        this.jsonConfigs = jsonConfigs;
    }


}
