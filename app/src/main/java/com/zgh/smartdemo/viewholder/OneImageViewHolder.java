package com.zgh.smartdemo.viewholder;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zgh.smartdemo.R;
import com.zgh.smartdemo.bean.NewsItem;


/**
 * Created by zhuguohui on 2016/8/17.
 */
public class OneImageViewHolder extends BaseNewsViewHolder {


    @Override
    public boolean isMyType(Object item) {
        if (item instanceof NewsItem) {
            NewsItem newsItem = (NewsItem) item;
            return newsItem.getDocType() == 1;
        }
        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.viewholder_one_image;
    }

    TextView tv_title,tv_content,tv_date;
    ImageView iv_news;

    @Override
    public void updateView(Context context, Object item) {
        super.updateView(context,item);
        NewsItem newsItem= (NewsItem) item;
        tv_title.setText(newsItem.getTitle());
        tv_content.setText(newsItem.getContent());
        tv_date.setText(newsItem.getDate());
        String imageUrl=newsItem.getImages()!=null&&newsItem.getImages().size()>0?newsItem.getImages().get(0):"";
        Glide.with(context).load(imageUrl).placeholder(R.drawable.ic_default_adimage).into(iv_news);
    }


}
