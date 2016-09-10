package com.zgh.smartdemo.page;

import com.zgh.smartdemo.bean.PageInfo;
import com.zgh.smartlibrary.config.JsonAnalysisConfig;
import com.zgh.smartlibrary.json.JsonAnalysisEngine;
import com.zgh.smartlibrary.net.NetRequest;
import com.zgh.smartlibrary.page.IPagePolicy;

import java.util.List;

/**
 * Created by zhuguohui on 2016/9/10 0010.
 */
public class DemoPagePolicy implements IPagePolicy {
    protected JsonAnalysisEngine pageEngine;
    private int mPageSize;
    private String mBaseUrl = "";

    public DemoPagePolicy() {
        mPageSize = 5;
        JsonAnalysisConfig config = new JsonAnalysisConfig();
        config.setJsonLocation("{response{page_info");
        config.setType(PageInfo.class.getName());
        pageEngine = new JsonAnalysisEngine(config);
    }


    @Override
    public PageState getPageState(int dataSize, String data) {
        PageState state;
        List<Object> data1 = pageEngine.getData(data);
        PageInfo page_info = data1 != null && data1.size() > 0 ? (PageInfo) data1.get(0) : null;
        if (page_info != null) {
            try {
                int count = Integer.valueOf(page_info.getPage_count());
                int page_index = Integer.valueOf(page_info.getPage_index());
                if (count == page_index + 1 || dataSize < mPageSize) {
                    state = PageState.NO_MORE;
                } else {
                    state = PageState.HAVE_MORE;
                }
            } catch (Exception e) {
                e.printStackTrace();
                state = PageState.NO_MORE;
            }
        } else {
            state = PageState.NO_PAGE;
        }
        return state;
    }

    @Override
    public NetRequest getNetRequestByPageIndex(int index) {
        NetRequest request=new NetRequest();
        String url = mBaseUrl;
        if (index != 0) {
            int i = url.lastIndexOf(".json");
            if (i != -1) {
                StringBuilder stringBuilder = new StringBuilder(url);
                stringBuilder.insert(i, "_" + index);
                url = stringBuilder.toString();
            } else {
                url = mBaseUrl+"_"+index;
            }
        }
        request.setUrl(url);
        return request;
    }

    @Override
    public void setBaseURL(String baseURL) {
        mBaseUrl = baseURL;
    }

}
