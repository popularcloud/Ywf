package com.qiuweixin.veface.base;

import android.app.Application;

/**
 * Created by Allen Lake on 2016/1/13 0013.
 */
public class App extends Application{

    public static App self;

    @Override
    public void onCreate() {
        super.onCreate();

        // 保存App实例
        self = this;
    }
}
