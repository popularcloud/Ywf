package com.qiuweixin.veface.mvp.activity.discovery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiuweixin.veface.R;
import com.qiuweixin.veface.base.AppConstants;
import com.qiuweixin.veface.callback.RequestCallBack;
import com.qiuweixin.veface.mvp.adpter.ArticleAdapter;
import com.qiuweixin.veface.mvp.model.ArticleInfo;
import com.qiuweixin.veface.network.okhttp.OkHttpUtil;
import com.qiuweixin.veface.util.BGARefreshLayoutBuilder;
import com.qiuweixin.veface.util.DividerItemDecoration;
import com.qiuweixin.veface.util.QBLToast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Allen Lake on 2016/1/12 0012.
 */
public class ItemFragment extends Fragment {

    @Bind(R.id.list)
    RecyclerView mRecyclerView;
    @Bind(R.id.refresh)
    BGARefreshLayout bgaRefreshLayout;

    private String TAG = "ItemFragment";
    private String mChannelId;
    private String mNextPage;
    private ArticleAdapter adpter;
    //private ArrayList<ArticleInfo> articles;
    private Boolean isLoadingMore;
    private LinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //动态找到布局文件，再从这个布局中find出TextView对象
        View contextView = inflater.inflate(R.layout.fragment_item, container, false);
        ButterKnife.bind(this, contextView);

        //获取Activity传递过来的参数
        Bundle mBundle = getArguments();
        mChannelId = mBundle.getString("arg");

        initContentView();
        return contextView;
    }

    private void initContentView() {
        //初始化刷新组件
        BGARefreshLayoutBuilder.init(getContext(), bgaRefreshLayout, true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(new ArticleAdapter());

        bgaRefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout bgaRefreshLayout) {
                Log.d(TAG, "刷新");
                requestArticleData(true);
            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout bgaRefreshLayout) {
                Log.d(TAG, "加载");
                return requestArticleData(false);
            }
        });

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bgaRefreshLayout.beginRefreshing();
    }


    /**
     * 加载文章列表
     */
    private boolean requestArticleData(final boolean isRefreshing) {
        if (isRefreshing) {
            mNextPage = "0";
        }
        isLoadingMore = true;
        String url = AppConstants.API.ARTICLE_LIST + "?cate_id=" + mChannelId + "&pubtime=" + mNextPage;
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
                        mNextPage = ext.getString("nexttime");

                        Log.d(TAG, "mNextPage:" + mNextPage);
                        // 解析列表数据
                        JSONArray JSONArr = json.getJSONArray("data");
                        final ArrayList<ArticleInfo> articles = new ArrayList<>(JSONArr.size());
                        if (JSONArr != null) {
                            adpter = (ArticleAdapter) mRecyclerView.getAdapter();

                            for (Object articleArrObject : JSONArr) {
                                JSONObject j = (JSONObject) articleArrObject;

                                ArticleInfo article = JSON.toJavaObject(j, ArticleInfo.class);

                                articles.add(article);
                            }
                            pushDataToUI(isRefreshing,articles);
                        }
                    } else {
                        if ("2".equals(count)) {
                            mNextPage = "-1";
                            QBLToast.show(R.string.no_more_data);
                            isLoadingMore = false;
                        } else {
                            String errorData = json.getString("data");
                            QBLToast.show(errorData);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    QBLToast.show(e.getMessage());
                } finally {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isRefreshing) {
                                bgaRefreshLayout.endRefreshing();
                            } else {
                                bgaRefreshLayout.endLoadingMore();
                            }
                        }
                    });
                }
            }
            @Override
            public void onFailure(String errorMessage) {
                if (isRefreshing) {
                    bgaRefreshLayout.endRefreshing();
                } else {
                    bgaRefreshLayout.endLoadingMore();
                }
                QBLToast.show(R.string.network_error);
            }
        });
        return true;
    }

    /**
     * 刷新数据到页面
     * @param isRefreshing
     * @param articles
     */
    private void pushDataToUI(final Boolean isRefreshing, final ArrayList<ArticleInfo> articles){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isRefreshing) {
                    adpter.setArticleData(articles);
                } else {
                    adpter.appendArticleData(articles);
                }

            }
        });
    }
}
