package com.qiuweixin.veface.mvp.presenter;

import com.qiuweixin.veface.callback.BaseCallBack;
import com.qiuweixin.veface.mvp.activity.discovery.ItemFragment;
import com.qiuweixin.veface.mvp.bean.ArticleInfo;
import com.qiuweixin.veface.mvp.model.ArticleModel;
import com.qiuweixin.veface.mvp.model.IArticleModel;
import com.qiuweixin.veface.mvp.view.IArticleView;

import java.util.ArrayList;

/**
 * Created by Allen Lake on 2016/1/19 0019.
 * 文章列表控制器类
 */
public class ArticlePresenter {

    private IArticleView mArticleView;
    private IArticleModel mArticleModel;

    public ArticlePresenter(IArticleView iArticleView){
        this.mArticleView = iArticleView;
        this.mArticleModel = new ArticleModel();
    }

    /**
     *从服务器加载数据并显示到页面上
     * @param cate_id
     */
    public void loadArticleDataFromServese(String cate_id){
        mArticleModel.loadArticleData(cate_id, new BaseCallBack() {
            @Override
            public void onSuccess(Object object) {
                    if(object == null && ItemFragment.isRefreshing){
                        mArticleView.showNoData();
                    }else{
                        mArticleView.showArticleInDiscovery((ArrayList<ArticleInfo>) object);
                    }
                mArticleView.endLoadingAndRefresh();
            }
            @Override
            public void onFailure(String errorMessage) {
                if(ItemFragment.isRefreshing){
                    mArticleView.showNoData();
                }
                mArticleView.endLoadingAndRefresh();
            }
        });
    }

}
