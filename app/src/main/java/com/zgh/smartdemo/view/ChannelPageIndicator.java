package com.zgh.smartdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zgh.smartdemo.R;


/**
 * Created by zhuguohui on 2016/8/21 0021.
 */
public class ChannelPageIndicator extends LinearLayout implements PageGridView.PageIndicator {

    private int selectID;
    private int unSelectID;

    public ChannelPageIndicator(Context context) {
        this(context, null);
    }

    public ChannelPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ChannelPageIndicator);
        selectID = array.getResourceId(R.styleable.ChannelPageIndicator_ChannelPageIndicatorSelectRID, R.drawable.dot_seleted);
        unSelectID = array.getResourceId(R.styleable.ChannelPageIndicator_ChannelPageIndicatorUnSelectRID, R.drawable.dot_unseleted);
        array.recycle();
    }


    @Override
    public void InitIndicatorItems(int itemsNumber) {
        removeAllViews();
        if (itemsNumber == 1) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
            for (int i = 0; i < itemsNumber; i++) {
                ImageView imageView = new ImageView(getContext());
                imageView.setImageResource(unSelectID);
                imageView.setPadding(10, 0, 10, 0);
                addView(imageView);
            }
        }
    }

    @Override
    public void onPageSelected(int pageIndex) {
        ImageView imageView = (ImageView) getChildAt(pageIndex);
        if (imageView != null) {
            imageView.setImageResource(selectID);
        }
    }

    @Override
    public void onPageUnSelected(int pageIndex) {
        ImageView imageView = (ImageView) getChildAt(pageIndex);
        if (imageView != null) {
            imageView.setImageResource(unSelectID);
        }
    }
}
