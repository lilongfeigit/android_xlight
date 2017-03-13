package com.umarbhutta.xlightcompanion;

import android.app.Application;

import com.umarbhutta.xlightcompanion.imgloader.ImageLoaderUtils;

/**
 * Created by guangbinw on 2017/3/13.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化APP
        ImageLoaderUtils.initImageLoader(getApplicationContext());
    }
}
