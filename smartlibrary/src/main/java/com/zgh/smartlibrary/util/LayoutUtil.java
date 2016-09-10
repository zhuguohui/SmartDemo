package com.zgh.smartlibrary.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.zgh.smartlibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuguohui on 2016/9/6.
 */
public class LayoutUtil {
    public static View getBaseView(Context context, int layoutID, View mView, ListView listView) {
        if (layoutID != 0) {
            View warpView = View.inflate(context, layoutID, null);
            View holderView = warpView.findViewById(R.id.ListViewHolder);
            if (holderView instanceof LinearLayout) {
                LinearLayout holder = (LinearLayout) holderView;
                View contentView = holder.findViewById(R.id.ListViewContent);
                List<View> headerViews = new ArrayList<>();
                List<View> footViews = new ArrayList<>();
                List viewList = headerViews;
                for (int i = 0; i < holder.getChildCount(); i++) {
                    View childView = holder.getChildAt(i);
                    if (childView == contentView) {
                        viewList = footViews;
                        continue;
                    }
                    viewList.add(childView);
                }
                handleHeaderAndFooterView(listView, context, headerViews, footViews);
            }

            ViewGroup parent = (ViewGroup) holderView.getParent();
            if (parent != null) {
                int index = 0;
                for (int i = 0; i < parent.getChildCount(); i++) {
                    if (parent.getChildAt(i) == holderView) {
                        index = i;
                        break;
                    }
                }
                parent.removeView(holderView);
                ViewGroup.LayoutParams params = holderView.getLayoutParams();
                mView.setLayoutParams(params);
                parent.addView(mView, index);
                mView = parent;
            }
        }

        return mView;
    }

    private static void handleHeaderAndFooterView(ListView listView, Context context, List<View> headerViews, List<View> footViews) {
        for (View view : headerViews) {
            LinearLayout ViewParent = new LinearLayout(context);
            if (view.getParent() != null) {
                ViewGroup group = (ViewGroup) view.getParent();
                group.removeView(view);
            }
            ViewParent.addView(view);
            listView.addHeaderView(ViewParent);
        }

        for (View view : footViews) {
            LinearLayout ViewParent = new LinearLayout(context);
            if (view.getParent() != null) {
                ViewGroup group = (ViewGroup) view.getParent();
                group.removeView(view);
            }
            ViewParent.addView(view);
            listView.addFooterView(ViewParent);
        }
    }
}
