package com.zgh.smartlibrary.json;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.zgh.smartlibrary.config.JsonAnalysisConfig;
import com.zgh.smartlibrary.util.GsonUtil;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuguohui on 2016/8/17.
 */
public class JsonAnalysisEngine {
    List<JsonAnalysisConfig> configItems = new ArrayList<>();
    Gson gson = new Gson();

    public JsonAnalysisEngine(List<JsonAnalysisConfig> configItems) {
        this.configItems = configItems;
    }

    public JsonAnalysisEngine(JsonAnalysisConfig... configItems) {
        this.configItems.clear();
        for (JsonAnalysisConfig config : configItems) {
            this.configItems.add(config);
        }

    }

    private int listDataSize = 0;

    public List<Object> getData(String jsonStr) {
        listDataSize = 0;
        List<Object> data = new ArrayList<>();
        for (JsonAnalysisConfig item : configItems) {
            Object o = getDataFromJson(jsonStr, item);
            if (o != null) {
                if (item.isListData()) {
                    List list = (List) o;
                    data.addAll(list);
                    listDataSize+=list.size();
                } else {
                    data.add(o);
                }
            }
        }
        return data;
    }

    public int getListDataSize() {
        return listDataSize;
    }

    private Object getDataFromJson(String jsonStr, JsonAnalysisConfig item) {
        Object o = null;
        String jsonLocation = item.getJsonLocation();
        try {
            String jsonData = getJsonStringFromLocation(jsonStr, jsonLocation);
            if (!TextUtils.isEmpty(jsonData)) {
                if (jsonData.startsWith("[")) {
                    o = GsonUtil.jsonToBeanList(jsonData, Class.forName(item.getType()));
                } else {
                    o = GsonUtil.jsonToBean(jsonData, Class.forName(item.getType()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }

    private String getJsonStringFromLocation(String jsonStr, String jsonLocation) throws JSONException {

        if (TextUtils.isEmpty(jsonLocation)) {
            return jsonStr;
        }
        char a;
        char op = 0;
        boolean haveFoundOP = false;
        int nameStart = 0, nameEnd = 0;
        String name;
        for (int i = 0; i < jsonLocation.length(); i++) {
            a = jsonLocation.charAt(i);
            if ('{' == a || '[' == a) {
                if (!haveFoundOP) {
                    op = a;
                    haveFoundOP = true;
                    nameStart = i + 1;
                } else {
                    nameEnd = i - 1;
                    break;
                }
            }
        }
        if (nameStart != 0 && nameEnd != 0) {
            name = jsonLocation.substring(nameStart, nameEnd + 1);
            jsonLocation = jsonLocation.substring(nameEnd + 1);
        } else {
            name = jsonLocation.substring(nameStart);
            jsonLocation = "";
        }
        jsonStr = jsonStr.trim();
        int index = -1;
        //
        if (name.indexOf(":") != -1) {
            String[] split = name.split(":");
            name = split[0];
            try {
                index = Integer.valueOf(split[1]);
            } catch (Exception e) {
            }

        }
        if (jsonStr.startsWith("{")) {
            JSONObject jsonObject = new JSONObject(jsonStr);
            if ('{' == op && jsonObject.has(name)) {
                return getJsonStringFromLocation(jsonObject.getJSONObject(name).toString(), jsonLocation);
            } else if ('[' == op) {

                if (index == -1) {
                    return getJsonStringFromLocation(jsonObject.getJSONArray(name).toString(), jsonLocation);
                } else {
                    return getJsonStringFromLocation(jsonObject.getJSONArray(name).getJSONObject(index).toString(), jsonLocation);
                }
            }
        } else {
            try {
                if (index != -1) {
                    JSONArray array = new JSONArray(jsonStr);
                    return array.getJSONObject(index).toString();
                } else {
                    return jsonStr;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }


        }
        return "";
    }


}
