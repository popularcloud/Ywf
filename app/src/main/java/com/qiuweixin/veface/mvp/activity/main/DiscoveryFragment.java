package com.qiuweixin.veface.mvp.activity.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiuweixin.veface.R;
import com.qiuweixin.veface.base.AppConstants;
import com.qiuweixin.veface.cache.ChannelCache;
import com.qiuweixin.veface.callback.RequestCallBack;
import com.qiuweixin.veface.mvp.activity.discovery.ItemFragment;
import com.qiuweixin.veface.mvp.bean.ChannelInfo;
import com.qiuweixin.veface.network.okhttp.okHttpPost;
import com.qiuweixin.veface.util.QBLToast;
import com.squareup.okhttp.FormEncodingBuilder;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Allen Lake on 2016/1/6 0006.
 */
public class DiscoveryFragment extends Fragment {

    @Bind(R.id.indicator)
    TabPageIndicator tabPageIndicator;
    @Bind(R.id.pagers)
    ViewPager viewPager;

    private ArrayList<ChannelInfo> selChannelInfoList;
    private ArrayList<ChannelInfo> notSelChannelInfoList;

    private static final String TAG = "DiscoveryFragment";

    private ArrayList<ChannelInfo> defaultChannelList;

    private ArrayList<ChannelInfo> channelList;
    private TabPageIndicatorAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discovery, container, false);
        ButterKnife.bind(this, view);

        initDefaultChannel();
        initContent();
        initData();
        return view;
    }

    private void initDefaultChannel() {
        defaultChannelList = new ArrayList<>();
        defaultChannelList.add(new ChannelInfo(10,"热门","http://img.weilianapp.com/master/images/icons/1_0007.png"));
        defaultChannelList.add(new ChannelInfo(2,"动态","http://img.weilianapp.com/master/images/icons/1_0007.png"));
        defaultChannelList.add(new ChannelInfo(7,"时尚","http://img.weilianapp.com/master/images/icons/1_0007.png"));
        defaultChannelList.add(new ChannelInfo(4,"娱乐","http://img.weilianapp.com/master/images/icons/1_0007.png"));
        defaultChannelList.add(new ChannelInfo(8,"生活","http://img.weilianapp.com/master/images/icons/1_0007.png"));
        defaultChannelList.add(new ChannelInfo(3,"社会","http://img.weilianapp.com/master/images/icons/1_0007.png"));
        defaultChannelList.add(new ChannelInfo(9,"美食","http://img.weilianapp.com/master/images/icons/1_0007.png"));
    }

    /**
     * 初始化数据
     */
    private void initData() {

        loadCache();

      //  requestChannelData();
    }

    /**
     * 加载缓存数据
     */
    private void loadCache() {
        channelList = ChannelCache.getSelChannelInfoList();
        adapter.notifyDataSetChanged();
    }

    /**
     * 初始化页面和布局
     */
    private void initContent() {
        //给ViewPager设置Adapter
        adapter = new TabPageIndicatorAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        //将indicator与ViewPager绑在一起（核心步骤）
        tabPageIndicator.setViewPager(viewPager);

        //如果要设置监听ViewPager中包含的Fragment的改变(滑动切换页面)，使用OnPageChangeListener为它指定一个监听器，那么不能像之前那样直接设置在ViewPager上了，而要设置在Indicator上，
        tabPageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //Toast.makeText(getActivity().getApplicationContext(), channelList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 加载频道列表
     */
    private void requestChannelData() {

        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("1", "1");

        okHttpPost.excuteSimple(AppConstants.API.ARTICLE_CATEGORY_LIST, builder, new RequestCallBack() {
            @Override
            public void onSuccess(JSONObject json) {
                Log.d(TAG, "频道列表json:" + json);
                try {
                    Boolean status = json.getBoolean("status");
                    if (status) {
                        JSONObject jObject = json.getJSONObject("data");
                        JSONArray isAtt = jObject.getJSONArray("is_att");
                        if (isAtt != null) {
                            selChannelInfoList = new ArrayList<>(isAtt.size());

                            for (Object arrObject : isAtt) {
                                JSONObject j = (JSONObject) arrObject;

                                ChannelInfo channel = JSON.toJavaObject(j, ChannelInfo.class);

                                selChannelInfoList.add(channel);
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    channelList = selChannelInfoList;
                                    adapter.notifyDataSetChanged();
                                }
                            });

                        }
                        JSONArray not_att = jObject.getJSONArray("not_att");
                        if (not_att != null) {
                            notSelChannelInfoList = new ArrayList<>(not_att.size());

                            for (Object arrObject : not_att) {
                                JSONObject j = (JSONObject) arrObject;

                                ChannelInfo channel = JSON.toJavaObject(j, ChannelInfo.class);

                                notSelChannelInfoList.add(channel);
                            }
                        }

                        /**
                         * 保存频道信息
                         */
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ChannelCache.saveSelChannelInfoList(selChannelInfoList);
                                ChannelCache.saveNotSelChannelInfoList(notSelChannelInfoList);
                            }
                        }).start();
                    } else {
                        String errorData = json.getString("data");
                        QBLToast.show(errorData);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    // QBLToast.show(R.string.unknown_exception);
                    return;
                } finally {
                    //
                    //sendVistorCount();
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    /**
     * 定义ViewPager的适配器
     */
    class TabPageIndicatorAdapter extends FragmentPagerAdapter {
        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //新建一个Fragment来展示ViewPager item的内容，并传递参数
            Fragment fragment = new ItemFragment();
            Bundle args = new Bundle();
          /*  if(channelList != null){
                args.putInt("arg", channelList.get(position).getId());
            }else{*/
                args.putString("arg", defaultChannelList.get(position).getId()+"");
           // }
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
           /* if(channelList != null){
                return channelList.get(position).getName();
            }else{*/
                return defaultChannelList.get(position).getName();
            //}
        }

        @Override
        public int getCount() {
           // return channelList != null?channelList.size():defaultChannelList.size();
            return defaultChannelList.size();
        }
    }
}
