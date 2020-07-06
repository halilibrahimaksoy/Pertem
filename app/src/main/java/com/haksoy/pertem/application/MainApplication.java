package com.haksoy.pertem.application;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;


public class MainApplication extends Application {
    private String TAG = "MainApplication";

    @Override
    public void onCreate() {
        super.onCreate();



    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}
