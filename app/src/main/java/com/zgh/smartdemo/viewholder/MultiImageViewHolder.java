package com.zgh.smartdemo.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zgh.smartdemo.R;
import com.zgh.smartdemo.bean.NewsItem;
import com.zgh.smartlibrary.util.AppUtil;


/**
 * 多张图片的viewholder
 * Created by zhuguohui on 2016/5/13.
 */
public class MultiImageViewHolder extends BaseNewsViewHolder {
    public TextView title, date;
    public LinearLayout image_layout;


    @Override
    public boolean isMyType(Object item) {
        if (item instanceof NewsItem) {
            NewsItem newsItem = (NewsItem) item;
            return newsItem.getDocType() == 3;
        }
        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.viewholder_multi_image;
    }

    @Override
    public void updateView(Context context, Object object) {
        super.updateView(context, object);
        NewsItem item = (NewsItem) object;
        title.setText(item.getTitle());
        date.setText(item.getDate());
        int count = image_layout.getChildCount();
        for (int i = 0; i < count; i++) {
            image_layout.getChildAt(i).setVisibility(View.GONE);
        }
        int size = item.getImages().size();
        for (int i = 0; i < size && i < count; i++) {
            ImageView image = (ImageView) image_layout.getChildAt(i);
            image.setVisibility(View.VISIBLE);
            Glide.with(context).load(item.getImages().get(i)).placeholder(R.drawable.default_pic).into(image);
        }
        int height = AppUtil.dip2px(context, 75);
        if (2 == size) {
            height = AppUtil.dip2px(context, 150);
        }
        for (int i = 0; i < count; i++) {
            View view = image_layout.getChildAt(i);
            if (view.getVisibility() != ViewGroup.GONE) {
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.height = height;
                view.setLayoutParams(params);
            }
        }
    }
}
