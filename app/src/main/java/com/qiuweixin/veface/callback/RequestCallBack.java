package com.qiuweixin.veface.callback;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by Allen Lake on 2016/1/12 0012.
 */
public interface RequestCallBack {

    void onSuccess(JSONObject json);

    void onFailure(String errorMessage);
}
