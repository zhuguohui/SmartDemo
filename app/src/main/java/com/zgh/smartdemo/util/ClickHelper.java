package com.zgh.smartdemo.util;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.webkit.WebViewFragment;
import android.widget.Toast;

import com.zgh.smartdemo.bean.BannerItem;
import com.zgh.smartdemo.bean.ChannelItem;
import com.zgh.smartdemo.bean.NewsItem;


/**
 * Created by 朱国辉 on 2016/8/24.
 */
public class ClickHelper {
    public static void onClick(Object object) {
        if (object == null) {
            return;
        }

        int clickType = 0;
        Fragment targetFragment = null;
        boolean addToBackStack = true;
        String url = "";
        String title = "";
        if (object instanceof ChannelItem) {
            ChannelItem item = (ChannelItem) object;
            clickType = item.getClickType();
            url = item.getUrl();
            title = item.getTitle();
        } else if (object instanceof BannerItem) {
            BannerItem item = (BannerItem) object;
            clickType = item.getClickType();
            url = item.getUrl();
            title = item.getTitle();
        } else if (object instanceof NewsItem) {
            NewsItem item = (NewsItem) object;
            clickType = item.getClickType();
            url = item.getUrl();
            title = item.getTitle();
        }



    }


}
