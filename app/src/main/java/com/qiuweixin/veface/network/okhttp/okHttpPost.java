package com.qiuweixin.veface.network.okhttp;

import com.alibaba.fastjson.JSON;
import com.qiuweixin.veface.callback.RequestCallBack;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


import java.io.IOException;

/**
 * Created by Allen Lake on 2016/1/12 0012.
 */
public class okHttpPost {

    private static RequestCallBack mCallBack;

    public static void excuteSimple(String url,FormEncodingBuilder builder,RequestCallBack requestCallBack){

        mCallBack = requestCallBack;
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(Request request, IOException e) {
                String bodys = request.body().toString();

              String json = JSON.toJSONString(bodys);
                if(mCallBack != null){
                    mCallBack.onSuccess(json);
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(mCallBack != null){
                    mCallBack.onFailure(response.message());
                }
            }
        });
    }
}
