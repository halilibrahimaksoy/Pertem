package com.haksoy.pertem.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.haksoy.pertem.firebase.FirebaseClient;
import com.haksoy.pertem.service.DataUpdateHelper;
import com.haksoy.pertem.tools.Constant;
import com.haksoy.pertem.tools.INotifyAction;


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
