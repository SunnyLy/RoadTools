package com.nobcdz.upload;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class UploadImageService extends Service {
	private List<String> listFile = new ArrayList<String>(); // 结果 List

	private Timer timer;

	@Override
	public void onDestroy() {
		super.onDestroy();
		timer.cancel();
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		try {
			timer = new Timer();
			TimerTask timerTask = new TimerTask() {
				int i;

				@Override
				public void run() {
					i++;
					if (i == 10) {
						timer.cancel();
					}
					startUpload();
				}
			};
			timer.schedule(timerTask, 5000, 500000); // 推迟5秒执行，每500执行一次
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startUpload() {
		try {
			GetFiles(Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ "/RoadTools/");
			for (String file : listFile) {

				if (upload(file)) {
					new File(file).delete();
					Intent sendIntent = new Intent();
					sendIntent.setAction("com.nobcdz.upload");
					sendBroadcast(sendIntent);
					Log.i("show", "上传成功：" + file);
				} else {
					Log.i("show", "上传失败：" + file);
				}
			}
			stopSelf(); // 停止服务
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean upload(String path) {
		ContinueFTP myFtp = new ContinueFTP(getApplicationContext());
		try {
			int indx = path.lastIndexOf("/");
			String name = path.substring(indx + 1, path.length());
			String pathName = path.substring(0, path.length())
					.replace(Environment.getExternalStorageDirectory()
							.getAbsolutePath()
							+ "/RoadTools/", "")
					.replace("/" + name, "");
			Log.i("show", "filePath:" + pathName);
			URLUtils urlUtils = new URLUtils();
			myFtp.connect(urlUtils.getFtpHost(getApplicationContext()),urlUtils.getFtpPort(getApplicationContext()),
					urlUtils.getFtpUser(getApplicationContext()),urlUtils.getFtpPwd(getApplicationContext()));
			myFtp.ftpClient.makeDirectory(new String(
					pathName.getBytes("UTF-8"), "iso-8859-1"));
			myFtp.ftpClient.changeWorkingDirectory(new String(pathName
					.getBytes("UTF-8"), "iso-8859-1"));

			myFtp.ftpClient.enterLocalActiveMode(); // 默认为被动模式，设置成主动模式

			UploadStatus status = myFtp.upload(path, "/" + name);
			if (status.equals(UploadStatus.Upload_From_Break_Success)
					|| status.equals(UploadStatus.Upload_New_File_Success)) {
				return true;
			} else if (status.equals(UploadStatus.File_Exits)
					|| status.equals(UploadStatus.Remote_Bigger_Local)) {
				return true;
			}
			myFtp.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void GetFiles(String Path) {

		File[] files = new File(Path).listFiles();

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.isFile()) {
				listFile.add(file.getPath());
			} else if (file.isDirectory() && file.getPath().indexOf("/.") == -1) {
				GetFiles(file.getPath());
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}