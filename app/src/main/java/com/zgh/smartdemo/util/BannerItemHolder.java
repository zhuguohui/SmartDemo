package com.zgh.smartdemo.util;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;


import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.zgh.smartdemo.R;
import com.zgh.smartdemo.bean.BannerItem;


/**
 * Created by zhuguohui on 2016/8/18.
 */
public class BannerItemHolder implements Holder<BannerItem> {
    private ImageView imageView;


    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        View view = View.inflate(context, R.layout.banner_layout, null);
        imageView = (ImageView) view.findViewById(R.id.img);

        return view;
    }

    @Override
    public void UpdateUI(Context context, int position, BannerItem data) {

        String url = data.getImages() != null && data.getImages().size() > 0 ? data.getImages().get(0) : "";

        Glide.with(context).load(url).placeholder(R.drawable.default_pic).into(imageView);
    }


}

