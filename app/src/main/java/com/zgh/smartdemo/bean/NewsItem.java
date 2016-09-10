package com.zgh.smartdemo.bean;



import com.zgh.smartdemo.util.HtmlRegexpUtil;
import com.zgh.smartlibrary.util.TimeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by yuelin on 2016/8/17.
 */
public class NewsItem {




    private int docId;
    private String title;
    private String content;
    private int docType;
    private int clickType;
    private String date;
    private String url;
    private List<String> images;
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private boolean haveFormatDate = false;
    private boolean haveFormatContent = false;

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        if(!haveFormatContent){
            content= HtmlRegexpUtil.filterHtml(content);
            haveFormatContent=true;
        }
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDocType() {
        return docType;
    }

    public void setDocType(int docType) {
        this.docType = docType;
    }

    public int getClickType() {
        return clickType;
    }

    public void setClickType(int clickType) {
        this.clickType = clickType;
    }

    public String getDate() {
        if(date==null){
            date="";
        }
        if (!haveFormatDate) {
            try {
                date = TimeUtil.format(format.parse(date).getTime());
                haveFormatDate = true;
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        return date;

    }

    public void setDate(String date) {

        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
