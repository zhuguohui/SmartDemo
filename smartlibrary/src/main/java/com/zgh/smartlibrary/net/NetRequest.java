package com.zgh.smartlibrary.net;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhuguohui on 2016/9/6.
 */
public class NetRequest {
    String url;
    METHOD method;
    CACHE cache;
    Map<String, String> params;
    NetResultListener resultListener;
    Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public NetResultListener getResultListener() {
        return resultListener;
    }

    public NetRequest setResultListener(NetResultListener resultListener) {
        this.resultListener = resultListener;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public NetRequest setUrl(String url) {
        this.url = url;
        return this;
    }

    public METHOD getMethod() {
        return method;
    }

    public NetRequest setMethod(METHOD method) {
        this.method = method;
        return this;
    }

    public CACHE getCache() {
        return cache;
    }

    public NetRequest setCache(CACHE cache) {
        this.cache = cache;
        return this;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public NetRequest addParams(String key, String value) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(key, value);
        return this;
    }

    public enum METHOD {
        GET, POST
    }

  public  enum CACHE {
        FILE, MEMORY, NO_CACHE
    }

    public interface NetResultListener {
        void onSuccess(NetRequest netRequest);
        void onError(String msg);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
