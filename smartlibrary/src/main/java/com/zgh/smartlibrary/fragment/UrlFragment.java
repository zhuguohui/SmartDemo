package com.zgh.smartlibrary.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.zgh.smartlibrary.retry.LoadingAndRetryManager;
import com.zgh.smartlibrary.retry.OnLoadingAndRetryListener;
import com.zgh.smartlibrary.util.TimeUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuelin on 2016/2/25.
 */
public class UrlFragment extends Fragment {
    public static final long MIN_LOADING_TIME=0;//最少的加载

    public static final String KEY_URL = "key_url";
    public static final String KEY_TITLE = "key_title";
    protected static final int STATE_EMPTY = 1;
    protected static final int STATE_RETRY = 2;
    protected static final int STATE_LOADING = 3;
    protected static final int STATE_CONTENT = 4;
    private static final int MSG_SHOW_CONTENT = 1;
    protected int mState = STATE_CONTENT;
    protected LoadingAndRetryManager mLoadingAndRetryManager;
    protected static Map<Integer, Method> methodMap = new HashMap<>();
    protected static String[] methodNames = new String[]{"showContent", "showEmpty", "showLoading", "showRetry"};
    protected static int[] states = new int[]{STATE_CONTENT, STATE_EMPTY, STATE_LOADING, STATE_RETRY};

    static {
        for (int i = 0; i < methodNames.length; i++) {
            String name = methodNames[i];
            int state = states[i];
            try {
                Method method = LoadingAndRetryManager.class.getMethod(name);
                methodMap.put(state, method);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_SHOW_CONTENT:
                    showContent();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String data = getArguments().getString(KEY_URL, "");
            String title = getArguments().getString(KEY_TITLE, "");
            url = data;
            this.title = title;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected OnLoadingAndRetryListener createLoadingAndRetryListener() {
        return null;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url = "";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title = "";

    protected long mLoadingStartTime=0;

    protected void showLoading() {
        mLoadingStartTime= TimeUtil.getNowTime();
        mState = STATE_LOADING;
        showByState();

    }

    protected void showRetry() {
        mState = STATE_RETRY;
        showByState();
    }

    protected void showContent() {
        showContent(false);
    }

    /**
     * 显示内容
     * @param force 立即显示
     */
    protected void showContent(boolean force){
        long delayTime=MIN_LOADING_TIME-(TimeUtil.getNowTime()-mLoadingStartTime);
        if(!force&&delayTime>0&&mLoadingStartTime!=-1){
            handler.sendEmptyMessageDelayed(MSG_SHOW_CONTENT,delayTime);
            mLoadingStartTime=-1;
        }else {
            mState = STATE_CONTENT;
            showByState();
        }
    }

    protected void showEmpty() {
        mState = STATE_EMPTY;
        showByState();
    }


    protected void showByState() {
        if (mLoadingAndRetryManager == null) {
            return;
        }
        Method method = methodMap.get(mState);
        if (method != null) {
            try {
                method.invoke(mLoadingAndRetryManager);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
