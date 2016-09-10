package com.zgh.smartdemo.viewholder;

import android.content.Context;
import android.view.View;

import com.zgh.smartdemo.util.ClickHelper;
import com.zgh.smartlibrary.adapter.SmartAdapter;


/**
 * Created by zhuguohui on 2016/8/25.
 */
public abstract class BaseNewsViewHolder extends SmartAdapter.SmartViewHolder<Object> {


    @Override
    public void updateView(Context context, final Object item) {
        getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickHelper.onClick(item);
            }
        });
    }
}
