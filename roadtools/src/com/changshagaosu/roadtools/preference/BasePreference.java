package com.changshagaosu.roadtools.preference;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.changshagaosu.roadtools.RoadToolsApplication;

public class BasePreference {
	
	public static void saveCompanyAndUser(String companyTag,String company, String companyId,String user) {
		SharedPreferences preferences = RoadToolsApplication.getContext().getSharedPreferences(
				"roadtools_base", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("companyTag", companyTag);
		editor.putString("company", company);
		editor.putString("companyId", companyId);
		editor.putString("user", user);
		editor.commit();
	}

	public  static Map<String, String> findcCompanyAndUser() {
		Map<String, String> params = new HashMap<String, String>();
		SharedPreferences preferences = RoadToolsApplication.getContext().getSharedPreferences(
				"roadtools_base", Context.MODE_PRIVATE);

		params.put("companyTag", preferences.getString("companyTag", "0"));
		params.put("company", preferences.getString("company", ""));
		params.put("companyId", preferences.getString("companyId", "0"));
		params.put("user", preferences.getString("user", ""));
		return params;
	}
	
	public static void saveRoadLine(String RoadLineID,String RoadLineNumber, String RoadLineName) {
		SharedPreferences preferences = RoadToolsApplication.getContext().getSharedPreferences(
				"roadtools_base", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("RoadLineID", RoadLineID);
		editor.putString("RoadLineNumber", RoadLineNumber);
		editor.putString("RoadLineName", RoadLineName);
		editor.commit();
	}

	public  static Map<String, String> findRoadLine() {
		Map<String, String> params = new HashMap<String, String>();
		SharedPreferences preferences = RoadToolsApplication.getContext().getSharedPreferences(
				"roadtools_base", Context.MODE_PRIVATE);
		params.put("RoadLineID", preferences.getString("RoadLineID", "0"));
		params.put("RoadLineNumber", preferences.getString("RoadLineNumber", ""));
		params.put("RoadLineName", preferences.getString("RoadLineName", "0"));
		return params;
	}
	
	public static void clear() {
		SharedPreferences preferences = RoadToolsApplication.getContext().getSharedPreferences(
				"roadtools_base", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
	}
}
