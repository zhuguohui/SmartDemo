package com.zgh.smartdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.zgh.smartdemo.R;
import com.zgh.smartdemo.bean.ChannelItem;
import com.zgh.smartdemo.view.PageGridView;

import java.util.ArrayList;
import java.util.List;

public class ChannelAdapter extends PageGridView.PagingAdapter<ChannelAdapter.ChannelVH> {

    private LayoutInflater inflater;
    private List<ChannelItem> data;
    private int width;
    private Context context;

    public ChannelAdapter(Context context, List<ChannelItem> data, int colums) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = data != null ? data : new ArrayList<ChannelItem>();
        width = context.getResources().getDisplayMetrics().widthPixels / colums;
    }


    @Override
    public ChannelVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_channel_item, parent, false);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        params.height = width;
        view.setLayoutParams(params);
        return new ChannelVH(view);
    }

    @Override
    public void onBindViewHolder(ChannelVH holder, int position) {
        final ChannelItem item = data.get(position);
        if (item == null) {
            holder.tv_name.setVisibility(View.GONE);
            holder.iv_top.setVisibility(View.GONE);
            return;
        } else {
            holder.iv_top.setVisibility(View.VISIBLE);
            holder.tv_name.setVisibility(View.VISIBLE);
        }
        holder.tv_name.setText(item.getTitle());
        Glide.with(context).load(item.getIcon()).placeholder(R.drawable.ic_zxzx).into(holder.iv_top);

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public List getData() {
        return data;
    }

    @Override
    public Object getEmpty() {
        return null;
    }

    public static class ChannelVH extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public ImageView iv_top;


        public ChannelVH(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            iv_top = (ImageView) itemView.findViewById(R.id.iv_top);

        }
    }
}
