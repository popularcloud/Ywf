package com.qiuweixin.veface.util;


import android.content.Context;
import android.graphics.Bitmap;

import com.qiuweixin.veface.R;

import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

public class BGARefreshLayoutBuilder {

    static Bitmap originalBitmap;
    static int ultimateColor = -1;

    public static void init(Context context, BGARefreshLayout refreshLayout, boolean loadingMoreEnabled) {
        RefreshViewHolder refreshViewHolder = new RefreshViewHolder(context, loadingMoreEnabled);

        int bitmapSize = UIUtil.dip2px(context, 36);
        if (originalBitmap == null) {
            Bitmap srcBitmap = ExtendedBitmapFactory.createBitmapFromResource(R.mipmap.ic_veface_transparent);
            originalBitmap = ExtendedBitmapFactory.shrink(srcBitmap, bitmapSize, bitmapSize);
        }
        if (ultimateColor == -1) {
            ultimateColor = context.getResources().getColor(R.color.theme_color);
        }
        refreshViewHolder.setOriginalBitmap(originalBitmap);
        refreshViewHolder.setUltimateColor(ultimateColor);

        refreshLayout.setRefreshViewHolder(refreshViewHolder);

        // 设置自定义头部视图（也可以不用设置）     参数1：自定义头部视图（例如广告位）， 参数2：上拉加载更多是否可用
        //refreshLayout.setCustomHeaderView(bannerView, loadingMoreEnabled);
        // 可选配置  -------------END
    }

    protected static class RefreshViewHolder extends BGAMoocStyleRefreshViewHolder {

        public RefreshViewHolder(Context context, boolean isLoadingMoreEnabled) {
            super(context, isLoadingMoreEnabled);

            // 为了增加下拉刷新头部和加载更多的通用性，提供了以下可选配置选项  -------------START
            // 设置正在加载更多时不显示加载更多控件
            // mRefreshLayout.setIsShowLoadingMoreView(false);
            // 设置正在加载更多时的文本
            this.setLoadingMoreText(context.getString(R.string.app_name));
            // 设置整个加载更多控件的背景颜色资源id
            this.setLoadMoreBackgroundColorRes(R.color.colorPrimary);
            // 设置整个加载更多控件的背景drawable资源id
            //this.setLoadMoreBackgroundDrawableRes(loadMoreBackgroundDrawableRes);
            // 设置下拉刷新控件的背景颜色资源id
            this.setRefreshViewBackgroundColorRes(R.color.base_bg);
            // 设置下拉刷新控件的背景drawable资源id
            //this.setRefreshViewBackgroundDrawableRes(refreshViewBackgroundDrawableRes);
            // 可选配置  -------------END
        }
    }
}
