package com.qiuweixin.veface.mvp.view;

import com.qiuweixin.veface.mvp.bean.ArticleInfo;

import java.util.ArrayList;

/**
 * Created by Allen Lake on 2016/1/19 0019.
 */
public interface IArticleView {

    void showArticleInDiscovery(ArrayList<ArticleInfo> articleInfos);

    void endLoadingAndRefresh();

    void showNoData();
}
