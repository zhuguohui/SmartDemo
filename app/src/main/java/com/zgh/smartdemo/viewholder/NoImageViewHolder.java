package com.zgh.smartdemo.viewholder;

import android.content.Context;
import android.widget.TextView;

import com.zgh.smartdemo.R;
import com.zgh.smartdemo.bean.NewsItem;


/**
 * 只有一张图片的viewholder
 * Created by zhuguohui on 2016/5/13.
 */
public class NoImageViewHolder extends BaseNewsViewHolder {
    public TextView title, date, content;


    @Override
    public boolean isMyType(Object item) {
        if (item instanceof NewsItem) {
            NewsItem newsItem = (NewsItem) item;
            return newsItem.getDocType() == 2;
        }
        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.viewholder_no_image;
    }

    @Override
    public void updateView(Context context, Object o) {
        super.updateView(context, o);
        NewsItem newsItem = (NewsItem) o;
        title.setText(newsItem.getTitle());
        date.setText(newsItem.getDate());
        content.setText(newsItem.getContent());

    }
}
