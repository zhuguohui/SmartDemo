package com.zgh.smartlibrary.manager.impl;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zgh.smartlibrary.R;
import com.zgh.smartlibrary.manager.ListViewUpdateManger;
import com.zgh.smartlibrary.page.IPagePolicy;
import com.zgh.smartlibrary.util.AppUtil;
import com.zgh.smartlibrary.util.TimeUtil;


/**
 * Created by zhuguohui on 2016/9/5.
 */
public class PullToRefreshManger implements ListViewUpdateManger {

    private final TextView footerView;
    protected PullToRefreshListView mPullToRefreshView;
    protected ListView listView;
    private String mStrNextPageRetry = "加载失败 点击重试";
    protected int LAYOUT_ID = R.layout.fragment_smart_list;
    private long mLastRefreshTime = 0;
    private boolean isUpdate = false;
    private View mBaseView = null;

    public PullToRefreshManger(Context context) {
        mBaseView = View.inflate(context, LAYOUT_ID, null);
        mPullToRefreshView = (PullToRefreshListView) mBaseView.findViewById(R.id.refreshView);
        mPullToRefreshView.setMode(PullToRefreshBase.Mode.BOTH);
        //配置加载更多文字
        mPullToRefreshView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
        mPullToRefreshView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载");
        mPullToRefreshView.getLoadingLayoutProxy(false, true).setReleaseLabel("释放加载更多");
        //加载更多的借口
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(final PullToRefreshBase<ListView> refreshView) {
                if (listener != null) {
                    isUpdate = true;
                    listener.pullUp();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                if (listener != null) {
                    isUpdate = false;
                    listener.pullDown();
                }
            }
        });
        mPullToRefreshView.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<ListView>() {
            @Override
            public void onPullEvent(PullToRefreshBase<ListView> refreshView,
                                    PullToRefreshBase.State state,
                                    PullToRefreshBase.Mode direction) {
                if ((state == PullToRefreshBase.State.PULL_TO_REFRESH ||
                        state == PullToRefreshBase.State.REFRESHING || state == PullToRefreshBase.State.MANUAL_REFRESHING)
                        && direction == PullToRefreshBase.Mode.PULL_FROM_START) {
                    if (mLastRefreshTime != 0L) {
                        mPullToRefreshView.getLoadingLayoutProxy(true, false)
                                .setLastUpdatedLabel(TimeUtil.format(mLastRefreshTime)
                                        + "更新");
                    }
                }
            }
        });

        //配置ListView
        listView = mPullToRefreshView.getRefreshableView();
        listView.setFooterDividersEnabled(false);
        listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        listView.setDivider(null);


        //添加footview
        footerView = (TextView) View.inflate(context, R.layout.view_bottom, null);
        footerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AppUtil.dip2px(context, 35)));
        //要隐藏footview其外部必须再包裹一层layout
        LinearLayout footerViewParent = new LinearLayout(context);
        footerViewParent.addView(footerView);
        footerView.setVisibility(View.GONE);
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (footerView.getText().equals(mStrNextPageRetry)) {
                    // footerView.setVisibility(View.GONE);
                    mPullToRefreshView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                    mPullToRefreshView.setRefreshing(true);
                }
            }
        });
        listView.addFooterView(footerViewParent);
    }

    @Override
    public ListView getListView() {
        return listView;
    }

    @Override
    public View getView() {
        return mBaseView;
    }

    @Override
    public void setState(IPagePolicy.PageState state) {
        switch (state) {
            case NO_MORE:
                footerView.setText("没有更多了");
                footerView.setVisibility(View.VISIBLE);
                mPullToRefreshView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                break;
            case HAVE_MORE:
                footerView.setVisibility(View.GONE);
                mPullToRefreshView.setMode(PullToRefreshBase.Mode.BOTH);
                break;
            case NO_PAGE:
                footerView.setVisibility(View.GONE);
                mPullToRefreshView.setMode(PullToRefreshBase.Mode.DISABLED);
                break;
            case LOAD_ERROR:
                footerView.setText(mStrNextPageRetry);
                footerView.setVisibility(View.VISIBLE);
                break;
            default:
                throw new IllegalArgumentException("Specified state is not supported state=" + state);
        }
    }


    @Override
    public void setAdapter(BaseAdapter adapter) {
        //设置adapter
        listView.setAdapter(adapter);
    }

    UpdateListener listener;

    @Override
    public void setUpdateListener(UpdateListener listener) {
        this.listener = listener;
    }

    @Override
    public void updateComplete() {
        if (isUpdate) {
            mLastRefreshTime = System.currentTimeMillis();
        }
        mPullToRefreshView.onRefreshComplete();
    }

    @Override
    public void update(boolean showAnimation) {
        mPullToRefreshView.setRefreshing(false);
    }


}
