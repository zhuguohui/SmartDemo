package com.zgh.smartdemo.viewholder;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zgh.smartdemo.R;
import com.zgh.smartdemo.bean.NewsItem;


/**
 * 广告的viewholder
 * Created by zhuguohui on 2016/5/13.
 */
public class ADViewHolder extends BaseNewsViewHolder {
    public TextView title;
    public ImageView image;
    public TextView date;


    @Override
    public boolean isMyType(Object item) {
        if (item instanceof NewsItem) {
            NewsItem newsItem = (NewsItem) item;
            return newsItem.getDocType() == 4;
        }
        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.viewholder_ad;
    }

    @Override
    public void updateView(Context context, Object object) {
        final NewsItem item = (NewsItem) object;
        title.setText(item.getTitle());
        date.setText(item.getDate());
        Glide.with(context).
                load(item.getImages() == null || item.getImages().size() == 0 ? "" : item.getImages().get(0))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.default_pic).into(image);
        super.updateView(context, object);
    }
}
