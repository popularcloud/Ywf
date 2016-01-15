package com.qiuweixin.veface.network.okhttp;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okio.BufferedSink;


/**
 * Created by Allen Lake on 2016/1/14 0014.
 */
public class OkHttpUtil {

    private static final OkHttpClient mOkHttpClient = new OkHttpClient();
    static {
        //设置超时
        mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
    }



    public OkHttpUtil(File cacheDirectory){
        //设置缓存
        //OkHttp内部是使用LRU来管理缓存的
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(cacheDirectory, cacheSize);
        mOkHttpClient.setCache(cache);

        /*http请求总该有个过期时间吧，缓存是由HTTP消息头中的”Cache-control”来控制的，常见的取值有private、no-cache、max-age、must-revalidate等，默认为private。
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


    /**
     * 设置头信息
     * @param url
     * @throws IOException
     */
    public static void setHeaderInfo(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent","OkHttp Headers.java")   //设置唯一的name value 如果有值 旧的将会被替换
                .addHeader("Accept","application/json; q=0.5")
                .addHeader("Accept", "application/vnd.github.v3+json")
                .build();
        Response response = mOkHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        System.out.println("Server: " + response.header("Server"));
        System.out.println("Date: " + response.header("Date"));
        System.out.println("Vary: " + response.headers("Vary"));
    }


    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8");

    /**
     * Post方式提交String
     */
    public static void postStringMethod(String url) throws IOException {
        String postBody = ""
                + "Releases\n"
                + "--------\n"
                + "\n"
                + " * _1.0_ May 6, 2013\n"
                + " * _1.1_ June 15, 2013\n"
                + " * _1.2_ August 11, 2013\n";

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN,postBody))
                .build();

        Response response = mOkHttpClient.newCall(request).execute();

        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        System.out.println(response.body().string());

    }

    /**
     * post方式提交流
     */
    public static void postStream(String url) throws IOException {
        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MEDIA_TYPE_MARKDOWN;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.writeUtf8("Numbers\n");
                sink.writeUtf8("-------\n");
                for (int i = 2; i <= 997; i++) {
                    sink.writeUtf8(String.format(" * %s = %s\n", i, factor(i)));
                }
            }
            private String factor(int n) {
                for (int i = 2; i < n; i++) {
                    int x = n / i;
                    if (x * i == n) return factor(x) + " × " + i;
                }
                return Integer.toString(n);
            }
        };
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Response response = mOkHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        System.out.println(response.body().string());

    }


    /**
     * post提交文件
     * @param url
     */
    public static void postFile(String url) throws IOException {
        File file = new File("README.md");

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                .build();

        Response response = mOkHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        System.out.println(response.body().string());
    }


    /**
     * post提交表单
     * 使用FormEncodingBuilder来构建和HTML<form>标签相同效果的请求体。键值对将使用一种HTML兼容形式的URL编码来进行编码。
     */
    public static void postForm(String url) throws IOException {
        RequestBody formBody = new FormEncodingBuilder()
                .add("search", "Jurassic Park")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Response response = mOkHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        System.out.println(response.body().string());
    }

    /**
     * 使用Gson来解析JSON响应
     */
/*    public static void parseJsonByGson(String url) throws IOException {
        Request request = new Request.Builder()
                .url("https://api.github.com/gists/c2a7c39532239ff261be")
                .build();
        Response response = mOkHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        Reader charSet = response.body().charStream();
        Gist gist = JSON.toJavaObject(,Gist.class);
        for (Map.Entry<String, String> entry : gist.files.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
    }

    static class Gist {
        Map<String, String> files;
    }*/

    public void addInterceptor(){
        final int maxCacheAge = 3600;
        Interceptor cacheInterceptor = new Interceptor() {
            @Override public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")//Pragma:no-cache。在HTTP/1.1协议中，它的含义和Cache-Control:no-cache相同。为了确保缓存生效
                        .header("Cache-Control", String.format("max-age=%d", maxCacheAge))//添加缓存请求头
                        .build();
            }
        };
    }
}
