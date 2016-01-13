package com.qiuweixin.veface.base;

/**
 * Created by Allen Lake on 2016/1/12 0012.
 */
public class AppConstants {

    public static String API_DOMAIN = "http://dev.weimingdan.com";

    public interface API {

        //首页频道列表
        String ARTICLE_CATEGORY_LIST = API_DOMAIN + "/api/article/category/list";
        //首页---文章列表
        String ARTICLE_LIST = API_DOMAIN + "/api/article/list";
        //首页---轮播图
        String ARTICLE_BANNER_SHOW = API_DOMAIN + "/api/article/banner/show";
    }

}
