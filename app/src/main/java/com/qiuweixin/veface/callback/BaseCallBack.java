package com.qiuweixin.veface.callback;

/**
 * Created by Allen Lake on 2016/1/12 0012.
 * 基础回调类
 */
public interface BaseCallBack {

    void onSuccess(Object object);

    void onFailure(String errorMessage);
}
