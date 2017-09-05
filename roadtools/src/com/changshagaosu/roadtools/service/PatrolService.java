package com.changshagaosu.roadtools.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.changshagaosu.roadtools.R;
import com.changshagaosu.roadtools.bean.AddCheckDailyRecord;
import com.changshagaosu.roadtools.json.RequestManager;
import com.changshagaosu.roadtools.preference.LoginPreference;
import com.changshagaosu.roadtools.ui.PatrolingActivity;
import com.changshagaosu.roadtools.utils.GPSUtil;
import com.nobcdz.upload.URLUtils;

public class PatrolService extends Service {
	private NotificationManager mNotificationManager;
	private String CheckDailyRecordID;
	private String Stauts;
	double longitude;
	double latitude;
	String userId;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		GPSUtil.getInstance().start();
		CheckDailyRecordID = intent.getStringExtra("CheckDailyRecordID");

		this.registerReceiver(new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				longitude = intent.getDoubleExtra("Longitude", 0);
				latitude = intent.getDoubleExtra("Latitude", 0);
				userId = LoginPreference.find().get("loginID");
				loadData(userId, String.valueOf(longitude),
						String.valueOf(latitude), CheckDailyRecordID, Stauts);
				Stauts = "1";
			}
		}, new IntentFilter("com.changshagaosu.roadtools.utils.GPSUtil"));

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.logo).setContentTitle("养护巡查管理系统")
				.setContentText("正在巡查中");
		Intent resultIntent = new Intent(this, PatrolingActivity.class);
		resultIntent.putExtra("CheckDailyRecordID", CheckDailyRecordID);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(PatrolingActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		Notification notification = mBuilder.build();
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1, notification);

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		super.onCreate();

		Stauts = "0";
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		loadData(userId, String.valueOf(longitude), String.valueOf(latitude),
				CheckDailyRecordID, "2");
		GPSUtil.getInstance().stop();
		mNotificationManager.cancelAll();
	}

	@SuppressWarnings("rawtypes")
	protected void executeRequest(Request request) {
		RequestManager.addRequest(request, this);
	}

	private void loadData(String LoginID, String Longitude, String Latitude,
						  String CheckDailyRecordID, String Stauts) {
		String urlString = new URLUtils().getInitData(getApplicationContext())
				+ "Action=AddGps" + "&UserID=" + LoginID + "&Longitude="
				+ Longitude + "&Latitude=" + Latitude + "&Key="
				+ CheckDailyRecordID + "&Stauts=" + Stauts;
		Log.i("show", "urlString:" + urlString);
		executeRequest(new com.changshagaosu.roadtools.json.GsonRequest<AddCheckDailyRecord>(
				urlString, AddCheckDailyRecord.class, null,
				new Listener<AddCheckDailyRecord>() {
					@Override
					public void onResponse(AddCheckDailyRecord response) {
						boolean success = response.isSuccess();
						if (success) {
							Toast.makeText(getApplicationContext(), "数据提交成功",
									Toast.LENGTH_LONG).show();
						} else {
							String msg = response.getMessage();
							Toast.makeText(getApplicationContext(), msg,
									Toast.LENGTH_LONG).show();
						}
					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(getApplicationContext(), "数据提交失败",
						Toast.LENGTH_LONG).show();
			}
		}));
	}

	public void stopGPS() {
		GPSUtil.getInstance().stop();
	}

	public void startGPS() {
		GPSUtil.getInstance().start();
	}
}