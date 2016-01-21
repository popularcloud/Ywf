package com.qiuweixin.veface.mvp.model;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiuweixin.veface.base.AppConstants;
import com.qiuweixin.veface.callback.BaseCallBack;
import com.qiuweixin.veface.callback.RequestCallBack;
import com.qiuweixin.veface.mvp.activity.discovery.ItemFragment;
import com.qiuweixin.veface.mvp.bean.ArticleInfo;
import com.qiuweixin.veface.network.okhttp.OkHttpUtil;

import java.util.ArrayList;

/**
 * Created by Allen Lake on 2016/1/19 0019.
 * 文章列表业务逻辑类
 */
public class ArticleModel implements IArticleModel{

    private static final String TAG = "ArticleModel";

    @Override
    public void loadArticleData(String cate_id, final BaseCallBack baseCallBack) {
        ItemFragment.isLoadingMore = true;
        String url = AppConstants.API.ARTICLE_LIST + "?cate_id=" + cate_id + "&pubtime=" + ItemFragment.mNextPage;
        Log.d(TAG, "url=" + url);
        OkHttpUtil.getSimple(url, new RequestCallBack() {
            @Override
            public void onSuccess(JSONObject json) {
                try {
                    String result = json.getString("status");
                    String count = json.getString("count");
                    Log.d(TAG, "returnData:" + json);
                    if ("true".equals(result)) {
                        JSONObject ext = json.getJSONObject("ext");
                        ItemFragment.mNextPage = ext.getLongValue("nexttime");

                        Log.d(TAG, "mNextPage:" + ItemFragment.mNextPage);
                        // 解析列表数据
                        JSONArray JSONArr = json.getJSONArray("data");
                        final ArrayList<ArticleInfo> articles = new ArrayList<>(JSONArr.size());
                        if (JSONArr != null) {

                            for (Object articleArrObject : JSONArr) {
                                JSONObject j = (JSONObject) articleArrObject;
                                ArticleInfo article = JSON.toJavaObject(j, ArticleInfo.class);
                                articles.add(article);
                            }
                            Log.d(TAG, articles.size()+"");
                            baseCallBack.onSuccess(articles);
                        }
                    } else {
                        if ("2".equals(count)) {
                           // QBLToast.show(R.string.no_more_data);
                            ItemFragment.isLoadingMore = false;
                        } else {
                            //String errorData = json.getString("data");
                            //QBLToast.show(errorData);
                        }
                        String errorData = json.getString("data");
                        baseCallBack.onFailure(errorData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    baseCallBack.onFailure(e.getMessage());
                }
            }
            @Override
            public void onFailure(String errorMessage) {
                baseCallBack.onFailure(errorMessage);
            }
        });
    }
}
