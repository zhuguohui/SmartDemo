package com.zgh.smartlibrary.net;

/**
 * Created by zhuguohui on 2016/9/6.
 */
public interface NetRequestHandler {

    void handleNetRequest(NetRequest netRequest);

    void cancelNetRequest(NetRequest netRequest);

}
