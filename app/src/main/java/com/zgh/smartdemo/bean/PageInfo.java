package com.zgh.smartdemo.bean;

/**
 * Created by zhuguohui on 2016/9/10 0010.
 */
public class PageInfo {
    String page_count;
    String page_index;
    int page_mode;

    public String getPage_count() {
        return page_count;
    }

    public void setPage_count(String page_count) {
        this.page_count = page_count;
    }

    public String getPage_index() {
        return page_index;
    }

    public void setPage_index(String page_index) {
        this.page_index = page_index;
    }

    public int getPage_mode() {
        return page_mode;
    }

    public void setPage_mode(int page_mode) {
        this.page_mode = page_mode;
    }
}
