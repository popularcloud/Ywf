package com.qiuweixin.veface.base;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

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

        //初始化 Fresco
        Fresco.initialize(this);
    }
}
