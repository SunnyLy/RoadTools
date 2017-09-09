package com.changshagaosu.roadtools;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.changshagaosu.roadtools.bean.RoadConstant;
import com.changshagaosu.roadtools.preference.ServerPreference;
import com.zhouyou.http.EasyHttp;

public class RoadToolsApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        EasyHttp.init(this);
        EasyHttp.getInstance()
                .setBaseUrl(RoadConstant.BASE_URL_SCHEME + ServerPreference.getHostName())
                .debug("RoadTools", true)
                .setRetryCount(3);//网络不好自动重试3次

    }

    public static Context getContext() {
        return sContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}