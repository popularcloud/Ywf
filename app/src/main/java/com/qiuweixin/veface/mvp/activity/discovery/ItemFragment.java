package com.qiuweixin.veface.mvp.activity.discovery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
 */
public class ItemFragment extends Fragment implements IArticleView{

    @Bind(R.id.list)
    RecyclerView mRecyclerView;
    @Bind(R.id.refresh)
    BGARefreshLayout bgaRefreshLayout;
    @Bind(R.id.error_layout)
    TextView errorLayout;

    public static Long mNextPage = 0L;
    public static Boolean isLoadingMore;
    //是否刷新
    public static boolean isRefreshing;

    private ArticlePresenter mArticlePresenter = new ArticlePresenter(this);

    private String TAG = "ItemFragment";
    private String mChannelId;
    private ArticleAdapter adpter;

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
        adpter = new ArticleAdapter();
        mRecyclerView.setAdapter(adpter);
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

    @OnClick(R.id.error_layout)
    public void onErrorLayoutClick(View view){
        errorLayout.setVisibility(View.GONE);
        bgaRefreshLayout.setVisibility(View.VISIBLE);
        bgaRefreshLayout.beginRefreshing();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bgaRefreshLayout.beginRefreshing();
    }

    @Override
    public void onResume() {
        super.onResume();
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
}
