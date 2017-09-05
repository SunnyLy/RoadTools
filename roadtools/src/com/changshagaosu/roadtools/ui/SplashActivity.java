package com.changshagaosu.roadtools.ui;

import java.util.List;

import net.tsz.afinal.FinalDb;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.changshagaosu.roadtools.R;
import com.changshagaosu.roadtools.bean.sub.Disease;
import com.changshagaosu.roadtools.bean.sub.RoadLine;
import com.changshagaosu.roadtools.bean.sub.RoadLine.Data;
import com.changshagaosu.roadtools.json.RequestManager;
import com.changshagaosu.roadtools.preference.LoginPreference;
import com.nobcdz.upload.URLUtils;
import com.umeng.analytics.MobclickAgent;

public class SplashActivity extends Activity {
	private FinalDb db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		db = FinalDb.create(getApplicationContext());
		if (db.findAll(Disease.class).isEmpty()) {
			loadData();
		} else {
			start();
		}
	}

	@SuppressWarnings("rawtypes")
	protected void executeRequest(Request request) {
		RequestManager.addRequest(request, this);
	}

	private void loadData() {
		executeRequest(new com.changshagaosu.roadtools.json.GsonRequest<RoadLine.RequestData>(
				new URLUtils().getInitData(getApplicationContext())
						+ "Action=RoadLocation", RoadLine.RequestData.class,
				null, new Listener<RoadLine.RequestData>() {
			@Override
			public void onResponse(RoadLine.RequestData response) {

				new DataAsyn().execute(response);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				start();
			}
		}));
	}

	private class DataAsyn extends
			AsyncTask<RoadLine.RequestData, String, Boolean> {

		@Override
		protected Boolean doInBackground(RoadLine.RequestData... params) {
			if (params[0].isSuccess()) {
				Data datas = params[0].getData();
				List<RoadLine> lines = datas.getRoadLine();
				for (RoadLine line : lines) {
					db.save(line);
					for(RoadLine.Traffic traffic:line.getTraffic()){
						traffic.setLineID(line.getLineID());
						traffic.setLineName(line.getLineName());
						db.save(traffic);
					}
				}
				return true;
			} else {
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				loadDiseaseData();
			} else {
				start();
			}
		}
	}

	private void loadDiseaseData() {
		executeRequest(new com.changshagaosu.roadtools.json.GsonRequest<Disease.RequestData>(
				new URLUtils().getInitData(getApplicationContext())
						+ "Action=DiseaseToType", Disease.RequestData.class,
				null, new Listener<Disease.RequestData>() {
			@Override
			public void onResponse(Disease.RequestData response) {
				new DiseaseDataAsyn().execute(response);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				start();
			}
		}));
	}

	private class DiseaseDataAsyn extends
			AsyncTask<Disease.RequestData, String, Boolean> {

		@Override
		protected Boolean doInBackground(Disease.RequestData... params) {
			if (params[0].isSuccess()) {
				Disease.Data datas = params[0].getData();
				List<Disease> lines = datas.getDisease();
				for (Disease disease : lines) {
					db.save(disease);
					for(Disease.DiseaseType diseaseType:disease.getDiseaseType()){
						diseaseType.setDictionaryCode(disease.getDictionaryCode());
						diseaseType.setDictionaryName(disease.getDictionaryName());
						db.save(diseaseType);
					}
				}
				return true;
			} else {
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			start();
		}
	}

	private void start() {
		String loginID = LoginPreference.find().get("loginID");
		if (loginID.equals("")) {
			Intent intent = new Intent(getApplicationContext(),
					LoginActivity.class);
			startActivity(intent);
			finish();
		} else {
			Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);
			startActivity(intent);
			finish();
		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
