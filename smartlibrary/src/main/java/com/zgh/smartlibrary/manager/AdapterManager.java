package com.zgh.smartlibrary.manager;

import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by yuelin on 2016/9/6.
 */
public interface AdapterManager {

    BaseAdapter getAdapter();

    int update(String jsonData, boolean update);

    void addDataModifiers(DataModifiers... dataModifiers);

    void removeDataModifiers(DataModifiers dataModifiers);

    interface DataModifiers {
        List<Object> modifyData(List<Object> data, boolean update);
    }

    String getRequestURL();

    int getDataSize();
}
