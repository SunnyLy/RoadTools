package com.changshagaosu.roadtools.preference;

import java.util.HashMap;
import java.util.Map;

import com.changshagaosu.roadtools.RoadToolsApplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LoginPreference {

	public static void save(String userName, String password, String loginID) {
		SharedPreferences preferences = RoadToolsApplication.getContext().getSharedPreferences(
				"roadtools_login", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("userName", userName);
		editor.putString("password", password);
		editor.putString("loginID", loginID);
		editor.commit();
	}

	public static Map<String, String> find() {
		Map<String, String> params = new HashMap<String, String>();
		SharedPreferences preferences = RoadToolsApplication.getContext().getSharedPreferences(
				"roadtools_login", Context.MODE_PRIVATE);

		params.put("userName", preferences.getString("userName", ""));
		params.put("password", preferences.getString("password", ""));
		params.put("loginID", preferences.getString("loginID", ""));

		return params;
	}

	public static void setInit(boolean isInit) {
		SharedPreferences preferences = RoadToolsApplication.getContext().getSharedPreferences(
				"roadtools_login", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("init", isInit);
		editor.commit();
	}

	public static boolean isInit() {
		SharedPreferences preferences = RoadToolsApplication.getContext().getSharedPreferences(
				"roadtools_login", Context.MODE_PRIVATE);
		return preferences.getBoolean("init", true);
	}

	public static void clear() {
		SharedPreferences preferences = RoadToolsApplication.getContext().getSharedPreferences(
				"roadtools_login", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
	}
}
