package com.zgh.smartdemo.util;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.zgh.smartdemo.R;
import com.zgh.smartdemo.bean.BannerItem;


/**
 * Created by zhuguohui on 2016/8/18.
 */
public class NewsBannerItemHolder implements Holder<BannerItem> {
    private ImageView imageView;
    private TextView title;

    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        View view = View.inflate(context, R.layout.layout_news_banner, null);
        imageView = (ImageView) view.findViewById(R.id.img);
        title = (TextView) view.findViewById(R.id.title);
        return view;
    }

    @Override
    public void UpdateUI(Context context, int position, BannerItem data) {
        title.setText(data.getTitle());
        String url = data.getImages() != null && data.getImages().size() > 0 ? data.getImages().get(0) : "";
        Glide.with(context).load(url).placeholder(R.drawable.default_pic).into(imageView);
    }


}

