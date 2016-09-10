package com.zgh.smartdemo.viewholder;

import android.content.Context;


import com.zgh.smartdemo.R;
import com.zgh.smartdemo.adapter.ChannelAdapter;
import com.zgh.smartdemo.bean.ChannelItem;
import com.zgh.smartdemo.util.ClickHelper;
import com.zgh.smartdemo.view.ChannelPageIndicator;
import com.zgh.smartdemo.view.PageGridView;
import com.zgh.smartlibrary.adapter.SmartAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuguohui on 2016/8/17.
 */
public class ChannelViewHolder extends SmartAdapter.SmartViewHolder<Object> {


    @Override
    public boolean isMyType(Object item) {
        if (item instanceof List) {
            List list = (List) item;
            for (Object o : list) {
                if (o == null) {
                    continue;
                }
                if (!(o instanceof ChannelItem)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_channel;
    }


    protected PageGridView pageGridView;
    ChannelPageIndicator channelPageIndicator;
    ChannelAdapter adapter;
    List<ChannelItem> channels = new ArrayList<>();

    @Override
    public void updateView(Context context, Object item) {
        List<ChannelItem> channels = (List<ChannelItem>) item;
        this.channels.clear();
        this.channels.addAll(channels);
        if (adapter == null) {
            adapter = new ChannelAdapter(context, this.channels, getColums());
            pageGridView.setAdapter(adapter);
            pageGridView.setPageIndicator(channelPageIndicator);
            pageGridView.setOnItemClickListener(new PageGridView.OnItemClickListener() {
                @Override
                public void onItemClick(PageGridView pageGridView, int position) {
                    ClickHelper.onClick(ChannelViewHolder.this.channels.get(position));
                }
            });
        } else {
            adapter.notifyDataSetChanged();
        }

    }

    protected int getColums() {
        return 4;
    }


}
