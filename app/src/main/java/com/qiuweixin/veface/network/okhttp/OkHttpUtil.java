package com.qiuweixin.veface.network.okhttp;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiuweixin.veface.callback.RequestCallBack;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


/**
 * okHttp 的简单封装
 * Created by Allen Lake on 2016/1/14 0014.
 */
public class OkHttpUtil {

    private static final OkHttpClient mOkHttpClient = new OkHttpClient();
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8"); //简易文本类型
    private static final String TAG = "OkHttpUtil";

    static {
        //设置超时
        mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
    }

    public static void setCacheDirectory(File cacheDirectory){
        //设置缓存
        //OkHttp内部是使用LRU来管理缓存的
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(cacheDirectory, cacheSize);
        mOkHttpClient.setCache(cache);
        /*http请求的过期时间:缓存是由HTTP消息头中的”Cache-control”来控制的，常见的取值有private、no-cache、max-age、must-revalidate等，默认为private。
        * private  内容只缓存到私有缓存中
        no-cache 所有内容都不会被缓存
        no-store 所有内容都不会被缓存到缓存或 Internet 临时文件中
        must-revalidation/proxy-revalidation 如果缓存的内容失效，请求必须发送到服务器/代理以进行重新验证
        max-age=xxx (xxx is numeric) 缓存的内容将在 xxx 秒后失效, 这个选项只在HTTP 1.1可用, 并如果和Last-Modified一起使用时, 优先级较高*/
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
     * @param mCallBack
     */
    public static void enqueue(Request request, final RequestCallBack mCallBack){
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if(mCallBack != null){
                    mCallBack.onFailure(e.getMessage());
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String bodys = response.body().string();
                Log.d(TAG,"后台返回的数据为:"+bodys);
                JSONObject jsonObject = JSON.parseObject(bodys);
                if(mCallBack != null){
                    mCallBack.onSuccess(jsonObject);
                }
            }
        });
    }

    /**
     * 开启异步线程访问网络 实现空的callback
     * @param request
     */
    public static void enqueue(Request request){
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {}
            @Override
            public void onResponse(Response response) throws IOException {}
        });
    }

    /**
     * 简单的get请求
     * @param url
     * @throws IOException
     */
    public static void getSimple(String url,RequestCallBack mCallBack){
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        enqueue(request, mCallBack);
    }

    /**
     * Post方式提交String
     */
    public static void postStringMethod(String url,String stringBody,RequestCallBack mCallBack) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, stringBody))
                .build();

        enqueue(request, mCallBack);

    }

    /**
     * post提交文件
     * @param url
     * @param mFile
     * @param mCallBack
     * @throws IOException
     */
    public static void postFile(String url,File mFile,RequestCallBack mCallBack) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, mFile))
                .build();

        enqueue(request, mCallBack);
    }


    /**
     * post提交表单
     * 使用FormEncodingBuilder来构建和HTML<form>标签相同效果的请求体。键值对将使用一种HTML兼容形式的URL编码来进行编码。
     * @param url  访问地址
     * @param formBody 表单数据
     * @param mCallBack 回调
     * @throws IOException
     */
    public static void postForm(String url,FormEncodingBuilder formBody,RequestCallBack mCallBack) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(formBody.build())
                .build();

        enqueue(request,mCallBack);
    }
}
