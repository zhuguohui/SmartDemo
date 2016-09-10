package com.zgh.smartdemo.modifiters;



import com.zgh.smartdemo.bean.NewsItem;
import com.zgh.smartlibrary.manager.AdapterManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuelin on 2016/9/6.
 */
public class NewsDataModifiers implements AdapterManager.DataModifiers {
    @Override
    public List<Object> modifyData(List<Object> data, boolean update) {
        if (!update) {
            List<Object> newsList = new ArrayList<>();
            for (Object object : data) {
                if (object instanceof NewsItem) {
                    newsList.add(object);
                }
            }
            data.clear();
            data.addAll(newsList);
        }
        return data;
    }
}
