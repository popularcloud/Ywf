package com.qiuweixin.veface.mvp.model;

import com.qiuweixin.veface.callback.BaseCallBack;

/**
 * Created by Allen Lake on 2016/1/19 0019.
 */
public interface IArticleModel {

    /**
     * 从服务器获取数据
     * @param cate_id
     * @param baseCallBack
     */
    void loadArticleData(String cate_id, BaseCallBack baseCallBack);
}
