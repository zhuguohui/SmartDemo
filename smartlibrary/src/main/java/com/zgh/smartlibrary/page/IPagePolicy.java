package com.zgh.smartlibrary.page;

import com.zgh.smartlibrary.net.NetRequest;

/**
 * Created by zhuguohui on 2016/9/2.
 */
public interface IPagePolicy {

    /**
     * 获取是否需要分页等信息
     *
     * @param dataSize 当前页的item数量
     * @param data     需要解析的json数据
     */
    PageState getPageState(int dataSize, String data);


    /**
     * 更具index获取分页的url
     *
     * @param index
     * @return
     */
    NetRequest getNetRequestByPageIndex(int index);


    void setBaseURL(String baseURL);


    enum PageState {
        NO_PAGE, HAVE_MORE, NO_MORE, LOAD_ERROR
    }
}
