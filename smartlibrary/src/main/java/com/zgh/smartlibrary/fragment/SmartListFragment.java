package com.zgh.smartlibrary.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;


import com.zgh.smartlibrary.R;
import com.zgh.smartlibrary.manager.AdapterManager;
import com.zgh.smartlibrary.manager.ListViewUpdateManger;
import com.zgh.smartlibrary.manager.impl.MyAdapterManager;
import com.zgh.smartlibrary.manager.impl.PullToRefreshManger;
import com.zgh.smartlibrary.net.NetRequest;
import com.zgh.smartlibrary.net.NetRequestHandler;
import com.zgh.smartlibrary.net.NetRequestModifiers;
import com.zgh.smartlibrary.net.impl.SmartNetRequestHandler;
import com.zgh.smartlibrary.page.IPagePolicy;
import com.zgh.smartlibrary.retry.LoadingAndRetryManager;
import com.zgh.smartlibrary.retry.OnLoadingAndRetryListener;
import com.zgh.smartlibrary.util.BackPressListener;
import com.zgh.smartlibrary.util.LayoutUtil;
import com.zgh.smartlibrary.util.NetStateUtil;
import com.zgh.smartlibrary.util.SpUtil;
import com.zgh.smartlibrary.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhuguohui on 2016/2/25.
 */
public abstract class SmartListFragment<T> extends UrlFragment implements BackPressListener, NetRequest.NetResultListener {
    //默认第一页
    public static final int FIRST_PAGE_INDEX = 0;
    //自动更新时间单位毫秒,默认10分钟
    private static final long AUTO_REFRESH_TIME = 10 * 60 * 1000;
    private static final int MSG_LOAD_DATA = 1;

    //  protected ListView listView;
    protected View mBaseView;
    //上一次更新时间
    protected long mLastRefreshTime = 0L;
    //请求的页码
    protected int mCurrentPageIndex = FIRST_PAGE_INDEX;
    //开始刷新时间
    protected long mStartRefreshTime = 0;
    //第一次加载
    protected boolean isFirstLoad = true;

    protected boolean isUpdate = false;
    //定时更新
    protected boolean isTimingUpdate = true;
    //预加载
    protected boolean isProLoad = true;
    private boolean mHaveCreat = false;

    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    int pageIndex = msg.arg1;
                    loadListData(pageIndex);
                    break;
            }
        }
    };

    protected IPagePolicy pagePolicy;
    protected ListViewUpdateManger updateManger;
    protected AdapterManager adapterManager;
    protected NetRequestHandler netRequestHandler;
    protected List<NetRequestModifiers> netRequestModifiersList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化网络请求修改器,需要在网络处理器初始化前完成。
        initNetRequestModifiers();
        //获取网络请求解析器
        netRequestHandler = getNetRequestHandler(getActivity());
        updateManger = getUpdateManager(getActivity());
        adapterManager = getAdapterManager(getActivity(), getConfig());
        adapterManager.addDataModifiers(getDataModifiers());
        if (!TextUtils.isEmpty(adapterManager.getRequestURL())) {
            setUrl(adapterManager.getRequestURL());
        }
        createView();
        initView();
        showLoading();
        mHaveCreat = true;
        pagePolicy = getPagePolicy();
        pagePolicy.setBaseURL(getUrl());
    }

    protected AdapterManager getAdapterManager(Context context, String config) {
        return new MyAdapterManager(context, config);
    }

    protected  ListViewUpdateManger getUpdateManager(Context context){
        return new PullToRefreshManger(context);
    }


    protected void initNetRequestModifiers() {

    }

    protected NetRequestHandler getNetRequestHandler(Context context) {
        return new SmartNetRequestHandler(context);
    }

    protected AdapterManager.DataModifiers[] getDataModifiers() {
        return null;
    }

    protected abstract IPagePolicy getPagePolicy();


    protected abstract String getConfig();


    private void createView() {

        View view = updateManger.getView();
        mLoadingAndRetryManager = LoadingAndRetryManager.generate(view, createLoadingAndRetryListener());
        View mView = mLoadingAndRetryManager.mLoadingAndRetryLayout;
        mBaseView = LayoutUtil.getBaseView(getActivity(), getLayoutID(), mView, updateManger.getListView());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mBaseView.getParent() != null) {
            ViewParent parent = mBaseView.getParent();
            if (parent instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(mBaseView);
            }
        }

        return mBaseView;
    }


    protected int getLayoutID() {
        return 0;
    }

    protected void initView() {

        updateManger.setUpdateListener(new ListViewUpdateManger.UpdateListener() {
            @Override
            public void pullUp() {
                mStartRefreshTime = TimeUtil.getNowTime();
                Message msg = new Message();
                msg.arg1 = FIRST_PAGE_INDEX;
                msg.what = MSG_LOAD_DATA;
                mHandler.sendMessage(msg);
            }

            @Override
            public void pullDown() {
                int tempIndex = mCurrentPageIndex + 1;
                mStartRefreshTime = TimeUtil.getNowTime();
                Message msg = new Message();
                msg.arg1 = tempIndex;
                msg.what = MSG_LOAD_DATA;
                mHandler.sendMessage(msg);
            }

        });
        updateManger.setAdapter(adapterManager.getAdapter());
        onViewInit();
    }

    protected void onViewInit() {

    }

    public View findViewById(int id) {
        if (mBaseView != null) {
            return mBaseView.findViewById(id);
        }
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            showContent(true);
            //判断是否需要自动更新
            if (isTimingUpdate) {
                long time = TimeUtil.getNowTime() - mLastRefreshTime;
                if (time > AUTO_REFRESH_TIME && NetStateUtil.isNetworkAvailable(getActivity())) {
                    updateManger.update(true);
                }
            }
        }
    }


    public void addNetRequestModifiers(NetRequestModifiers modifiers) {
        if (netRequestModifiersList == null) {
            netRequestModifiersList = new ArrayList<>();
        }
        netRequestModifiersList.add(modifiers);
    }

    public void removeNetRequestModifiers(NetRequestModifiers modifiers) {
        if (netRequestModifiersList != null) {
            netRequestModifiersList.remove(modifiers);
            if (netRequestModifiersList.size() == 0) {
                netRequestModifiersList = null;
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isProLoad && isVisibleToUser && isFirstLoad && mHaveCreat) {
            loadListData(FIRST_PAGE_INDEX);
            isFirstLoad = false;
        }
    }

    protected NetRequest listDataRequest;
    protected int requestIndex;

    protected void loadListData(int pageIndex) {
        requestIndex = pageIndex;
        isUpdate = pageIndex == FIRST_PAGE_INDEX;
        NetRequest request  = pagePolicy.getNetRequestByPageIndex(pageIndex);
        request.setResultListener(this);
        if (isUpdate && adapterManager.getDataSize() == 0) {
            showLoading();
        }
        if (isUpdate) {
            //缓存首页
            request.setCache(NetRequest.CACHE.FILE);
        }
        listDataRequest = request;
        loadData(request);
    }


    protected void loadData(NetRequest netRequest) {
        netRequest = modifyNetRequest(netRequest);
        netRequestHandler.handleNetRequest(netRequest);
    }

    protected NetRequest modifyNetRequest(NetRequest netRequest) {
        NetRequest result = netRequest;
        if (netRequestModifiersList != null) {
            for (NetRequestModifiers modifiers : netRequestModifiersList) {
                result = modifiers.modifyNetRequest(result);
            }
        }
        return result;
    }


    protected void onGetError(String error) {
        //只有在刷新且没有数据的时候才显示重试,否则不处理
        showContent();
        closeUpdate();
        if (isUpdate) {
            if (adapterManager.getDataSize() > 0) {

                updateManger.setState(IPagePolicy.PageState.HAVE_MORE);
            } else {
                showRetry();
            }
        } else {
            updateManger.setState(IPagePolicy.PageState.LOAD_ERROR);
        }

    }


    @Override
    protected OnLoadingAndRetryListener createLoadingAndRetryListener() {
        return new OnLoadingAndRetryListener() {
            @Override
            public void setRetryEvent(View retryView) {
                retryView.findViewById(R.id.id_btn_retry).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadListData(mCurrentPageIndex);
                    }
                });
            }
        };
    }


    private void closeUpdate() {
        updateManger.updateComplete();
    }


    /**
     * 处理json数据
     *
     * @param data
     * @param update
     */
    protected void handleJSonData(String data, boolean update) {
        int dataSize = adapterManager.update(data, update);
        if (dataSize == 0) {
            showEmpty();
        }
        IPagePolicy.PageState pageState = pagePolicy.getPageState(dataSize, data);
        updateManger.setState(pageState);
    }


    /**
     * 用于处理返回键
     *
     * @return
     */
    public boolean onBackPressed() {
        return false;
    }


    @Override
    public void onSuccess(NetRequest netRequest) {
        if (netRequest == listDataRequest) {
            showContent();
            String response = (String) netRequest.getData();
            handleJSonData(response, isUpdate);
            if (isUpdate) {
                //更新时间
                mLastRefreshTime = TimeUtil.getNowTime();
                SpUtil.getInstance(getActivity()).setValue(getUrl(), mLastRefreshTime);

            }
            //更新当前页
            mCurrentPageIndex = requestIndex;
            closeUpdate();
        }
    }

    @Override
    public void onError(String msg) {
        onGetError(msg);
    }
}
