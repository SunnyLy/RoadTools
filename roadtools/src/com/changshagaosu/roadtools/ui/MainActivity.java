package com.changshagaosu.roadtools.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import net.tsz.afinal.FinalDb;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.changshagaosu.roadtools.R;
import com.changshagaosu.roadtools.bean.RoadLine;
import com.changshagaosu.roadtools.json.RequestManager;
import com.changshagaosu.roadtools.preference.BasePreference;
import com.changshagaosu.roadtools.preference.LoginPreference;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.google.code.microlog4android.appender.Appender;
import com.google.code.microlog4android.appender.FileAppender;
import com.google.code.microlog4android.config.PropertyConfigurator;
import com.nobcdz.upload.URLUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends Activity {
	private EditText userEditText;
	private Map<String, String> map;
	private int tag;
	private ImageView refreshImageView;
	private ProgressBar progress;
	private FinalDb db;
	private String userId;/*
	private static final Logger logger = LoggerFactory.getLogger();*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*PropertyConfigurator.getConfigurator(this).configure();
		final Appender fa = logger.getAppender(1);
		((FileAppender) fa).setAppend(true);*/
		setContentView(R.layout.activity_main);
		userEditText = (EditText) findViewById(R.id.user_et);
		refreshImageView = (ImageView) findViewById(R.id.refresh_iv);
		progress = (ProgressBar) findViewById(R.id.progress);

		map = BasePreference.findcCompanyAndUser();

		db = FinalDb.create(getApplicationContext());

		if (!map.get("user").equals("")) {
			userEditText.setText(map.get("user"));
		}
		userId = LoginPreference.find().get("loginID");
		if (db.findAll(RoadLine.class).isEmpty()) {
			Log.i("show", "线路为空");
			LoginPreference.setInit(false);
			refreshImageView.setVisibility(View.INVISIBLE);
			progress.setVisibility(View.VISIBLE);
			loadData();
		} else if (LoginPreference.isInit()) {
			Log.i("show", "我是新登录的");
			LoginPreference.setInit(false);
			refreshImageView.setVisibility(View.INVISIBLE);
			progress.setVisibility(View.VISIBLE);
			loadData();
		}
		refreshImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				/*
				 * LoginPreference.setInit(false);
				 * refreshImageView.setVisibility(View.INVISIBLE);
				 * progress.setVisibility(View.VISIBLE); loadData();
				 */
			}
		});

		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);
	}

	@SuppressWarnings("rawtypes")
	protected void executeRequest(Request request) {
		RequestManager.addRequest(request, this);
	}

	private void loadData() {
		executeRequest(new com.changshagaosu.roadtools.json.GsonRequest<RoadLine.RequestData>(
				new URLUtils().getInitData(getApplicationContext())
						+ "Action=RoadLine&UserID=" + userId,
				RoadLine.RequestData.class, null,
				new Listener<RoadLine.RequestData>() {
					@Override
					public void onResponse(RoadLine.RequestData response) {
						new DataAsyn().execute(response);
					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {

			}
		}));
	}

	private class DataAsyn extends
			AsyncTask<RoadLine.RequestData, String, Boolean> {

		@Override
		protected Boolean doInBackground(RoadLine.RequestData... params) {
			if (params[0].isSuccess()) {
				RoadLine.Data data = params[0].getData();

				for (RoadLine roadLine : data.getRoadLine()) {
					db.save(roadLine);
				}
				return true;
			} else {
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (!result) {
				Toast.makeText(getApplicationContext(), "获取线路数据失败",
						Toast.LENGTH_SHORT).show();
			}
			refreshImageView.setVisibility(View.VISIBLE);
			progress.setVisibility(View.INVISIBLE);

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (LoginPreference.find().get("loginID").equals("")) {
			finish();
		}
		MobclickAgent.onResume(this);
	}

	public void mOnClick(View view) {
		switch (view.getId()) {
			case R.id.ok_btn:
				BasePreference.saveCompanyAndUser(String.valueOf(tag), null, null,
						userEditText.getText().toString());
				Intent intent = new Intent(getApplicationContext(),
						PatrolActivity.class);
				startActivity(intent);
				break;
			case R.id.settings_btn:
				Intent settingsIntent = new Intent(getApplicationContext(),
						SettingsActivity.class);
				startActivity(settingsIntent);
				break;
			case R.id.checkin_btn:
				int	hours = Integer.parseInt(new SimpleDateFormat("HH").format(new Date()));
				if(hours>=7&&hours<18){
					Intent checkinIntent = new Intent(getApplicationContext(),
							CheckinActivity.class);
					startActivity(checkinIntent);
				}else{
					Toast.makeText(getApplicationContext(), "签到时间：7点~18点", Toast.LENGTH_SHORT).show();
				}

				break;
			default:
				break;
		}
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
