package com.memoryleakdemo.king.memoryleakdemo;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by king on 2017/5/14.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
