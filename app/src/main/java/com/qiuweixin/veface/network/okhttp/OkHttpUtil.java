package com.qiuweixin.veface.network.okhttp;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by Allen Lake on 2016/1/14 0014.
 */
public class OkHttpUtil {

    private static final OkHttpClient mOkHttpClient = new OkHttpClient();
    static {
        mOkHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
    }

    /**
     * 不开启异步线程
     * @param request
     * @return
     * @throws IOException
     */
    public static Response execute(Request request) throws IOException {
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 开启异步线程访问网络
     * @param request
     * @param responseCallback
     */
    public static void enqueue(Request request,Callback responseCallback){
        mOkHttpClient.newCall(request).enqueue(responseCallback);
    }

    /**
     * 开启异步线程访问网络 实现空的callback
     * @param request
     */
    public static void enqueue(Request request){
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
    }

    /**
     * 从服务器获取字符串
     * @param url
     * @return
     * @throws IOException
     */
    public static String getStringFromServer(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = execute(request);

        if(response.isSuccessful()){
            String resposeStr = response.body().string();
            return resposeStr;
        }else {
            throw new IOException("Unexpected code " + response);
        }
    }


    /**
     * 打印响应头
     * @param url
     * @throws IOException
     */
    public static void printHeaderInfo(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();

        Response response = mOkHttpClient.newCall(request).execute();
        if(!response.isSuccessful()){
            throw new IOException("Unexpected code"+response);
        }

        Headers responseHeader = response.headers();

        for (int i = 0;i<responseHeader.size();i++){
            System.out.println(responseHeader.name(i) + ": " + responseHeader.value(i));
        }

        System.out.println(response.body().string());
    }


    public static void setHeaderInfo(String url){
        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent","OkHttp Headers.java")   //设置唯一的name value 如果有值 旧的将会被替换
                .addHeader("Accept","application/json; q=0.5")
                .addHeader("Accept", "application/vnd.github.v3+json")
                .build();
    }

}
