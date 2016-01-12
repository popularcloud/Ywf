package com.qiuweixin.veface.callback;

import org.json.JSONObject;

/**
 * Created by Allen Lake on 2016/1/12 0012.
 */
public interface RequestCallBack {

    void onSuccess(String json);

    void onFailure(String errorMessage);
}
