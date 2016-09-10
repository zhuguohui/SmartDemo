package com.zgh.smartlibrary.manager;

import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.zgh.smartlibrary.page.IPagePolicy;


/**
 * 管理listview的上拉加载与下拉刷新
 * Created by zhuguohui on 2016/9/5.
 */
public interface ListViewUpdateManger {

    ListView getListView();

    View getView();

    void setState(IPagePolicy.PageState state);

    void setAdapter(BaseAdapter adapter);

    void setUpdateListener(UpdateListener listener);

    interface UpdateListener {
        void pullUp();
        void pullDown();
    }

    void updateComplete();

    void update(boolean showAnimation);

}
