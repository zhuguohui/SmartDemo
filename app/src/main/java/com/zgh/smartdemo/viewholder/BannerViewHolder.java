package com.zgh.smartdemo.viewholder;

import android.content.Context;
import android.widget.ImageView;


import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.zgh.smartdemo.R;
import com.zgh.smartdemo.bean.BannerItem;
import com.zgh.smartdemo.util.BannerItemHolder;
import com.zgh.smartdemo.util.ClickHelper;
import com.zgh.smartlibrary.adapter.SmartAdapter;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by yuelin on 2016/8/17.
 */
public class BannerViewHolder extends SmartAdapter.SmartViewHolder<Object> {


    @Override
    public boolean isMyType(Object item) {
        if (item instanceof List) {
            List list = (List) item;
            for (Object o : list) {
                if (!(o instanceof BannerItem)) {
                    return false;
                }
            }
            return true;
        }
        return false;

    }


    @Override
    public int getLayoutId() {
        return R.layout.layout_banner;
    }

    ConvenientBanner banner;

    @Override
    public void updateView(Context context, final Object item) {
        banner.setPages(new CBViewHolderCreator<BannerItemHolder>() {

            @Override
            public BannerItemHolder createHolder() {
                return new BannerItemHolder();
            }
        }, (List) item);
        banner.setPointViewVisible(true);
        banner.setPageIndicator(new int[]{R.drawable.dot_unseleted, R.drawable.dot_seleted});
        banner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
        //通过反射改变指示点间距，以满足设计图要求
        try {
            Field filed = banner.getClass().getDeclaredField("mPointViews");
            filed.setAccessible(true);
            List<ImageView> imageViews = (List<ImageView>) filed.get(banner);
            for (ImageView imageView : imageViews) {
                imageView.setPadding(10, 0, 10, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        banner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ClickHelper.onClick(((List) item).get(position));
            }
        });
    }


}
