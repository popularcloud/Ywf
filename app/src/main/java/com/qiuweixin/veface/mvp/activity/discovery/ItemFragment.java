package com.qiuweixin.veface.mvp.activity.discovery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qiuweixin.veface.R;
import com.qiuweixin.veface.mvp.adpter.ArticleAdapter;
import com.qiuweixin.veface.mvp.bean.ArticleInfo;
import com.qiuweixin.veface.mvp.presenter.ArticlePresenter;
import com.qiuweixin.veface.mvp.view.IArticleView;
import com.qiuweixin.veface.util.BGARefreshLayoutBuilder;
import com.qiuweixin.veface.util.DividerItemDecoration;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Allen Lake on 2016/1/12 0012.
 * 发现页fragment页签
 */
public class ItemFragment extends Fragment implements IArticleView{

    @Bind(R.id.list)
    RecyclerView mRecyclerView;
    @Bind(R.id.refresh)
    BGARefreshLayout bgaRefreshLayout;
    @Bind(R.id.error_layout)
    TextView errorLayout;

    private static final String TAG = "ItemFragment";
    public static Long mNextPage = 0L;
    public static Boolean isLoadingMore;
    public static boolean isRefreshing;

    /**
     * 文章列表显示的控制器类
     */
    private ArticlePresenter mArticlePresenter = new ArticlePresenter(this);
    /**
     * 频道id
     */
    private String mChannelId;
    /**
     * 文章列表中的适配器
     */
    private ArticleAdapter adpter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contextView = inflater.inflate(R.layout.fragment_item, container, false);
        //fragment中绑定ButterKnife
        ButterKnife.bind(this, contextView);

        //获取Activity传递过来的参数
        Bundle mBundle = getArguments();
        mChannelId = mBundle.getString("mChannelId");

        if (savedInstanceState != null && "".equals(mChannelId)) {
            mChannelId = savedInstanceState.getString("mChannelId");
        }
        //初始化的方法
        initContentView();

        Log.d(TAG,"进入onCreateView方法");
        return contextView;
    }

    private void initContentView() {
        //初始化刷新组件
        BGARefreshLayoutBuilder.init(getContext(), bgaRefreshLayout, true);

        //设置RecyclerView
        LinearLayoutManager  mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.VERTICAL_LIST));
        adpter = new ArticleAdapter();
        mRecyclerView.setAdapter(adpter);

        //为刷新组件设置事件
        bgaRefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout bgaRefreshLayout) {
                mNextPage = 0L;
                isRefreshing = true;
                mArticlePresenter.loadArticleDataFromServese(mChannelId);
            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout bgaRefreshLayout) {
                isRefreshing = false;
                if (isLoadingMore) {
                    mArticlePresenter.loadArticleDataFromServese(mChannelId);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 点击重新加载数据
     * @param view
     */
    @OnClick(R.id.error_layout)
    public void onErrorLayoutClick(View view){
        errorLayout.setVisibility(View.GONE);
        bgaRefreshLayout.setVisibility(View.VISIBLE);
        bgaRefreshLayout.beginRefreshing();
    }

    /**
     * 刷新数据到页面
     * @param articleInfos
     */
    @Override
    public void showArticleInDiscovery(final ArrayList<ArticleInfo> articleInfos) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isRefreshing) {
                    adpter.setArticleData(articleInfos);
                } else {
                    adpter.appendArticleData(articleInfos);
                }
            }
        });
    }

    /**
     * 结束刷新和加载更多
     */
    @Override
    public void endLoadingAndRefresh() {
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

    /**
     * 无数据的显示
     */
    @Override
    public void showNoData() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                errorLayout.setVisibility(View.VISIBLE);
                bgaRefreshLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("mChannelId",mChannelId);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "进入onActivityCreated方法");
        bgaRefreshLayout.beginRefreshing();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "进入onResume方法");
    }
}
