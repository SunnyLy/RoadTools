package com.changshagaosu.roadtools.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.changshagaosu.roadtools.RoadToolsApplication;

public class ServerPreference {

	public static void save(String hostName) {
		SharedPreferences preferences = RoadToolsApplication.getContext().getSharedPreferences(
				"server", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("hostName", hostName);
		editor.commit();
	}

	public static String getHostName() {
		SharedPreferences preferences = RoadToolsApplication.getContext().getSharedPreferences(
				"server", Context.MODE_PRIVATE);
		return preferences.getString("hostName", "58.20.182.212:89");
	}

	public static void saveDiyHost(String hostName) {
		SharedPreferences preferences = RoadToolsApplication.getContext().getSharedPreferences(
				"server", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("diyHost", hostName);
		editor.commit();
	}

	public static String getDiyHost() {
		SharedPreferences preferences = RoadToolsApplication.getContext().getSharedPreferences(
				"server", Context.MODE_PRIVATE);
		return preferences.getString("diyHost", "xxxxxx");
	}
}
