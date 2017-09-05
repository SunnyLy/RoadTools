package com.changshagaosu.roadtools;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

public class RoadToolsApplication extends Application {
	private static Context sContext;

	@Override
	public void onCreate() {
		super.onCreate();
		sContext = getApplicationContext();

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