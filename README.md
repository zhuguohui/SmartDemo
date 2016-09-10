# SmartDemo
一个用于快速开发复杂List布局的框架
#序言

最近快半个月没写博客，不是偷懒而是在整理一些思路，最近的项目中开始尝试使用新框架，也一直在改进中，最后觉得差不多了准备开源出来。10分钟开发网易新闻首页有点夸大其词，但是在我的Demo中修改一下样式基本就能满足大多数新闻客户端的需求了，其他的APP更不在话下。

#效果

####1.这是长截屏，主要展示的样式的复杂度，和网易新闻首页相比差不了多少。

![这里写图片描述](http://upload-images.jianshu.io/upload_images/2808681-238f7100777695c4?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

####2.这是动态效果，集成了，下拉刷新，上拉加载更多，错误重试，当然这些界面都是可以自定义的。

![这里写图片描述](https://github.com/zhuguohui/SmartDemo/blob/master/gif/1.gif?raw=true)

####3.直接使用布局文件实现自定义，最终框架将自动替换与添加相应的部分。

**布局文件如下**

![这里写图片描述](http://upload-images.jianshu.io/upload_images/2808681-519901999fe3bc08?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

**代码就只有这么多**

![这里写图片描述](http://upload-images.jianshu.io/upload_images/2808681-926bd9d51ed53058?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

**效果很爽**

![这里写图片描述](https://github.com/zhuguohui/SmartDemo/blob/master/gif/2.gif?raw=true)

#使用

##1.配置

1.使用起来很简单，先看代码

这是BaseFragment继承自SmartListFragment，而SmartListFragment就是我框架中的类了，这里面返回了当前应用的分页策略，方便统一控制。

![这里写图片描述](http://upload-images.jianshu.io/upload_images/2808681-25ae849c294abd91?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

而首页中就返回了一个配置文件的名字，这个配置文件放在res/raw下的。一切的黑科技都从配置文件开始

![这里写图片描述](http://upload-images.jianshu.io/upload_images/2808681-1d66389c61103d54?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

我们来看看配置文件长什么样。就是一个很简单的json文件，为了方便截图我收缩了一部分。

![这里写图片描述](http://upload-images.jianshu.io/upload_images/2808681-e7d54375230386be?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

上面的文件对应于这个类

![这里写图片描述](http://upload-images.jianshu.io/upload_images/2808681-8fcb393aee072ec6?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

而JsonAnalysisConfig定义如下：

![这里写图片描述](http://upload-images.jianshu.io/upload_images/2808681-4c30238ccb617aeb?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


##例子

可能大家还不是很清楚，我给大家做一个例子，大家就明白了。以解析生成这个为例：

![这里写图片描述](http://upload-images.jianshu.io/upload_images/2808681-157e5eee5b7d2e3b?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

首先整体的数据结构如下

![这里写图片描述](http://upload-images.jianshu.io/upload_images/2808681-17b1761d238233ea?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

我们关心的是这一部分

![这里写图片描述](http://upload-images.jianshu.io/upload_images/2808681-b22d41647086098e?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

使用它去生成一个javabean,名字就叫channelItem可以使用Android Studio的Gson生成插件。

![这里写图片描述](http://upload-images.jianshu.io/upload_images/2808681-889a242f30d42847?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

接着我们编写ViewHolder。

![这里写图片描述](http://upload-images.jianshu.io/upload_images/2808681-d0ae13f29554eb72?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这是源码很短，很简单实现了解耦：

```
package com.zgh.smartdemo.viewholder;

import android.content.Context;


import com.zgh.smartdemo.R;
import com.zgh.smartdemo.adapter.ChannelAdapter;
import com.zgh.smartdemo.bean.ChannelItem;
import com.zgh.smartdemo.util.ClickHelper;
import com.zgh.smartdemo.view.ChannelPageIndicator;
import com.zgh.smartdemo.view.PageGridView;
import com.zgh.smartlibrary.adapter.SmartAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuguohui on 2016/8/17.
 */
public class ChannelViewHolder extends SmartAdapter.SmartViewHolder<Object> {


    @Override
    public boolean isMyType(Object item) {
        if (item instanceof List) {
            List list = (List) item;
            for (Object o : list) {
                if (o == null) {
                    continue;
                }
                if (!(o instanceof ChannelItem)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_channel;
    }


    protected PageGridView pageGridView;
    ChannelPageIndicator channelPageIndicator;
    ChannelAdapter adapter;
    List<ChannelItem> channels = new ArrayList<>();

    @Override
    public void updateView(Context context, Object item) {
        List<ChannelItem> channels = (List<ChannelItem>) item;
        this.channels.clear();
        this.channels.addAll(channels);
        if (adapter == null) {
            adapter = new ChannelAdapter(context, this.channels, getColums());
            pageGridView.setAdapter(adapter);
            pageGridView.setPageIndicator(channelPageIndicator);
            pageGridView.setOnItemClickListener(new PageGridView.OnItemClickListener() {
                @Override
                public void onItemClick(PageGridView pageGridView, int position) {
                    ClickHelper.onClick(ChannelViewHolder.this.channels.get(position));
                }
            });
        } else {
            adapter.notifyDataSetChanged();
        }

    }

    protected int getColums() {
        return 4;
    }
}
```
最后讲我们的这些信息记录到配置文件中

![这里写图片描述](http://upload-images.jianshu.io/upload_images/2808681-37ba7f1d70c0717b?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

需要说明的是jsonLocation，它的值是

```
//{表示解析一个对象，[代表解析一个数据，后面是数据的名字
//而[name:0 代表解析一个名叫name的json数据下面的第一个元素
{response[centers
```

我们来看看解析过程

![这里写图片描述](http://upload-images.jianshu.io/upload_images/2808681-a942108f244aad0d?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

如果不想被自动填充为List则在配置文件中设置如下属性

![这里写图片描述](http://upload-images.jianshu.io/upload_images/2808681-bfc73260ba92582f?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

打开断点看看数据解析出来的样子

![这里写图片描述](http://upload-images.jianshu.io/upload_images/2808681-749115447b2f98a6?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

更多细节大家看Demo吧

#个性化配置

###1.数据解析器

覆盖SmartListFragment的这个方法可以替换自己的数据解析器

![这里写图片描述](http://upload-images.jianshu.io/upload_images/2808681-6485ebade8f47312?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

前提实现这个接口

![这里写图片描述](http://upload-images.jianshu.io/upload_images/2808681-098e933cd7111294?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

目前有一个默认的实现

```
package com.zgh.smartlibrary.net.impl;

import android.content.Context;


import com.zgh.smartlibrary.net.NetRequest;
import com.zgh.smartlibrary.net.NetRequestHandler;
import com.zgh.smartlibrary.util.FileUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.HasParamsable;
import com.zhy.http.okhttp.builder.OkHttpRequestBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhuguohui on 2016/9/6.
 */
public class SmartNetRequestHandler implements NetRequestHandler {
    private final Context mContext;
    private String HTTP_HEAD = "http";
    private String HTTPS_HEAD = "https";
    private String RAW_HEAD="raw://";

    public SmartNetRequestHandler(Context context){
        mContext=context;
    }

    @Override
    public void handleNetRequest(final NetRequest netRequest) {
        String url = netRequest.getUrl();

        boolean isHttpRequest = false;
        if (url != null && url.length() > 5) {

            if (url.toLowerCase().startsWith(HTTP_HEAD) || url.toLowerCase().startsWith(HTTPS_HEAD)) {
                isHttpRequest = true;
            }
        }
        if(netRequest.getMethod()==null){
            netRequest.setMethod(NetRequest.METHOD.GET);
        }
        if (isHttpRequest) {
            GetBuilder getBuilder = null;
            PostFormBuilder postFormBuilder = null;
            OkHttpRequestBuilder requestBuilder;
            HasParamsable hasParamsable;
            switch (netRequest.getMethod()) {
                case GET:
                    getBuilder = OkHttpUtils.get();
                    break;
                case POST:
                    postFormBuilder = OkHttpUtils.post();
                    break;
            }
            requestBuilder = getBuilder != null ? getBuilder : postFormBuilder;
            if (requestBuilder == null) {
                onError(netRequest, "不支持的协议！");
                return;
            }
            hasParamsable = getBuilder != null ? getBuilder : postFormBuilder;
            requestBuilder.url(url);
            Map<String, String> params = netRequest.getParams();
            if (params != null && params.size() > 0) {
                for (String key : params.keySet()) {
                    hasParamsable.addParams(key, params.get(key));
                }
            }
            requestBuilder.build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    SmartNetRequestHandler.this.onError(netRequest, e.getMessage());
                }

                @Override
                public void onResponse(String response, int id) {
                    onSuccess(netRequest,response);
                }
            });
        } else {
            if(url.toLowerCase().startsWith(RAW_HEAD)){
                String rawName = url.substring(RAW_HEAD.length());
                String s = FileUtil.readRaw(mContext, rawName);
                onSuccess(netRequest, s);
            }else{
                onError(netRequest,"不支持的协议！");
                return;
            }
        }

    }

    public void onError(NetRequest request, String msg) {
        if (request != null && request.getResultListener() != null) {
            request.getResultListener().onError(msg);
        }
    }

    public void onSuccess(NetRequest request, Object data) {
        if (request != null && request.getResultListener() != null) {
            request.setData(data);
            request.getResultListener().onSuccess(request);
        }
    }

    @Override
    public void cancelNetRequest(NetRequest netRequest) {

    }
}

```

###2.下拉刷控件替换

覆盖这个方法

```
 protected  ListViewUpdateManger getUpdateManager(Context context){
        return new PullToRefreshManger(context);
    }
```

实现这个接口

```
/**
 * 管理listview的上拉加载与下拉刷新
 * Created by zhuguohui on 2016/9/5.
 */
public interface ListViewUpdateManger {

    ListView getListView();

    View getView();

    void setState(IPagePolicy.PageState state);

    void setAdapter(BaseAdapter adapter);

    void setUpdateListener(UpdateListener listener);

    interface UpdateListener {
        void pullUp();
        void pullDown();
    }

    void updateComplete();

    void update(boolean showAnimation);

}
```
有一个默认的实现PullToRefreshManger大家可以看看源码

###对数据进行修改

![这里写图片描述](http://upload-images.jianshu.io/upload_images/2808681-4540438c949bebdf?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

###对网络请求修改

比如添加统一的token之类的 

![这里写图片描述](http://upload-images.jianshu.io/upload_images/2808681-cd3fc5918c12d191?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

###对页面修改

1.代码部分

![这里写图片描述](http://upload-images.jianshu.io/upload_images/2808681-ee4719c8845de199?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

2.布局部分

![这里写图片描述](http://upload-images.jianshu.io/upload_images/2808681-e3cfec2a17841b32?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#实现

东西太多看源码吧

#源码

[SmartDemo](https://github.com/zhuguohui/SmartDemo)
