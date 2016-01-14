package com.qiuweixin.veface.mvp.activity.discovery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.qiuweixin.veface.network.okhttp.okHttpGet;
import com.qiuweixin.veface.util.DividerItemDecoration;
import com.qiuweixin.veface.util.QBLToast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Allen Lake on 2016/1/12 0012.
 */
public class ItemFragment extends Fragment{

    @Bind(R.id.list)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private String TAG = "ItemFragment";
    private String mChannelId;
    private String mNextPage;
    private ArticleAdapter adpter;
    private ArrayList<ArticleInfo> articles;
    private LinearLayoutManager mLayoutManager;
    private boolean isLoadingMore = true;

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
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(new ArticleAdapter());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "下拉");
                requestArticleData(true);
            }
        });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                int totalItemCount = mLayoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 4 && dy > 0) {
                    if (isLoadingMore) {
                        isLoadingMore = false;
                        Log.d(TAG, "上拉");
                        requestArticleData(false);
                    }

                }
            }
        });

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestArticleData(true);
    }


    /**
     * 加载文章列表
     */
    private void requestArticleData(final boolean isRefreshing) {
        if(isRefreshing){
            mNextPage = "0";
        }
        String url = AppConstants.API.ARTICLE_LIST + "?cate_id=" + mChannelId + "&pubtime=" + mNextPage;
        Log.d(TAG,"url="+url);
        okHttpGet.excute(url,new RequestCallBack() {
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
                        if (JSONArr != null) {
                            if(articles == null){
                                articles = new ArrayList<>(JSONArr.size());
                            }else{
                                articles.clear();
                            }

                            adpter = (ArticleAdapter) mRecyclerView.getAdapter();

                            for (Object articleArrObject : JSONArr) {
                                JSONObject j = (JSONObject) articleArrObject;

                                ArticleInfo article = JSON.toJavaObject(j, ArticleInfo.class);

                                articles.add(article);
                            }

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(isRefreshing){
                                        adpter.setArticleData(articles);
                                    }else{
                                        adpter.appendArticleData(articles);
                                    }

                                }
                            });

                        }
                    } else {
                        if ("2".equals(count)) {
                            mNextPage = "-1";
                            QBLToast.show(R.string.no_more_data);
                        } else {
                            String errorData = json.getString("data");
                            QBLToast.show(errorData);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    QBLToast.show(e.getMessage());
                    return;
                } finally {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(isRefreshing){
                                swipeRefreshLayout.setRefreshing(false);
                            }else{
                                isLoadingMore = true;
                            }
                        }
                    });

                }
            }

            @Override
            public void onFailure(String errorMessage) {
                if(isRefreshing){
                    swipeRefreshLayout.setRefreshing(false);
                }else{
                    isLoadingMore = true;
                }
                QBLToast.show(R.string.network_error);
            }
        });
    }
}
