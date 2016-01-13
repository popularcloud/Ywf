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
import com.qiuweixin.veface.cache.ChannelCache;
import com.qiuweixin.veface.callback.RequestCallBack;
import com.qiuweixin.veface.mvp.adpter.RecyclerViewAdapter;
import com.qiuweixin.veface.mvp.model.ChannelInfo;
import com.qiuweixin.veface.network.okhttp.okHttpPost;
import com.qiuweixin.veface.util.QBLToast;
import com.squareup.okhttp.FormEncodingBuilder;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Allen Lake on 2016/1/12 0012.
 */
public class ItemFragment extends Fragment{

    @Bind(R.id.list)
    RecyclerView mRecyclerView;
    private String TAG = "ItemFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //动态找到布局文件，再从这个布局中find出TextView对象
        View contextView = inflater.inflate(R.layout.fragment_item, container, false);
        ButterKnife.bind(this, contextView);


        //获取Activity传递过来的参数
        Bundle mBundle = getArguments();
        String title = mBundle.getString("arg");

        initContentView();
        return contextView;
    }

    private void initContentView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new RecyclerViewAdapter(new String[]{"111111111111", "2222222222222", "33333333333", "444444444444", "555555555555555", "6666666666666"}));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    /**
     * 加载频道列表
     */
    private void requestArticleData() {

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
}
