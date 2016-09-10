package com.zgh.smartlibrary.net.impl;

import android.content.Context;


import com.zgh.smartlibrary.net.NetRequest;
import com.zgh.smartlibrary.net.NetRequestHandler;
import com.zgh.smartlibrary.util.FileUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.HasParamsable;
import com.zhy.http.okhttp.builder.OkHttpRequestBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhuguohui on 2016/9/6.
 */
public class SmartNetRequestHandler implements NetRequestHandler {
    private final Context mContext;
    private String HTTP_HEAD = "http";
    private String HTTPS_HEAD = "https";
    private String RAW_HEAD="raw://";

    public SmartNetRequestHandler(Context context){
        mContext=context;
    }

    @Override
    public void handleNetRequest(final NetRequest netRequest) {
        String url = netRequest.getUrl();

        boolean isHttpRequest = false;
        if (url != null && url.length() > 5) {

            if (url.toLowerCase().startsWith(HTTP_HEAD) || url.toLowerCase().startsWith(HTTPS_HEAD)) {
                isHttpRequest = true;
            }
        }
        if(netRequest.getMethod()==null){
            netRequest.setMethod(NetRequest.METHOD.GET);
        }
        if (isHttpRequest) {
            GetBuilder getBuilder = null;
            PostFormBuilder postFormBuilder = null;
            OkHttpRequestBuilder requestBuilder;
            HasParamsable hasParamsable;
            switch (netRequest.getMethod()) {
                case GET:
                    getBuilder = OkHttpUtils.get();
                    break;
                case POST:
                    postFormBuilder = OkHttpUtils.post();
                    break;
            }
            requestBuilder = getBuilder != null ? getBuilder : postFormBuilder;
            if (requestBuilder == null) {
                onError(netRequest, "不支持的协议！");
                return;
            }
            hasParamsable = getBuilder != null ? getBuilder : postFormBuilder;
            requestBuilder.url(url);
            Map<String, String> params = netRequest.getParams();
            if (params != null && params.size() > 0) {
                for (String key : params.keySet()) {
                    hasParamsable.addParams(key, params.get(key));
                }
            }
            requestBuilder.build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    SmartNetRequestHandler.this.onError(netRequest, e.getMessage());
                }

                @Override
                public void onResponse(String response, int id) {
                    onSuccess(netRequest,response);
                }
            });
        } else {
            if(url.toLowerCase().startsWith(RAW_HEAD)){
                String rawName = url.substring(RAW_HEAD.length());
                String s = FileUtil.readRaw(mContext, rawName);
                onSuccess(netRequest, s);
            }else{
                onError(netRequest,"不支持的协议！");
                return;
            }
        }

    }

    public void onError(NetRequest request, String msg) {
        if (request != null && request.getResultListener() != null) {
            request.getResultListener().onError(msg);
        }
    }

    public void onSuccess(NetRequest request, Object data) {
        if (request != null && request.getResultListener() != null) {
            request.setData(data);
            request.getResultListener().onSuccess(request);
        }
    }

    @Override
    public void cancelNetRequest(NetRequest netRequest) {

    }
}
