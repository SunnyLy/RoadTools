package com.changshagaosu.roadtools.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.changshagaosu.roadtools.R;
import com.nobcdz.upload.UploadImageService;

public class PictureActivity extends Activity {
	private Button startBtn;
	private TextView text;
	private int fileNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture);
		text = (TextView) findViewById(R.id.text);
		startBtn = (Button) findViewById(R.id.start_btn);

		init();
		this.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				init();
			}
		}, new IntentFilter("com.nobcdz.upload"));
	}

	private void init() {
		GetFiles(Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/RoadTools/UploadPicture");
		fileNum = listFile.size();

		if (isWorked()) {
			startBtn.setText("正在上传");
		} else {
			startBtn.setText("开始上传");
		}

		if (fileNum > 0) {
			text.setText("还有" + fileNum + "张照片未上传");
		} else {
			text.setText("照片已经上传完毕");
			startBtn.setText("开始上传");
		}

	}

	public void mOnClick(View view) {
		switch (view.getId()) {
			case R.id.start_btn:
				if (!isWorked()) {
					if (fileNum > 0) {
						Intent intent = new Intent(getApplicationContext(),
								UploadImageService.class);
						startService(intent);
						startBtn.setText("正在上传");
					}else{
						Toast.makeText(getApplicationContext(), "照片已经上传完毕", Toast.LENGTH_SHORT).show();
					}
				}
				break;
			default:
				break;
		}
	}

	// 判断上传服务是否开启

	private boolean isWorked() {
		ActivityManager myManager = (ActivityManager) this
				.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
				.getRunningServices(50);
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service.getClassName().toString()
					.equals("com.nobcdz.upload.UploadImageService")) {
				return true;
			}
		}
		return false;
	}

	private List<String> listFile; // 结果 List

	public void GetFiles(String Path) {
		try {
			listFile = new ArrayList<String>();
			File[] files = new File(Path).listFiles();

			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isFile()) {
					listFile.add(file.getPath());
				} else if (file.isDirectory()
						&& file.getPath().indexOf("/.") == -1) {
					GetFiles(file.getPath());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
