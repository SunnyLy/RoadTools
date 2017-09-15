package com.changshagaosu.roadtools.ui;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.changshagaosu.roadtools.R;
import com.changshagaosu.roadtools.permission.PermissionActivity;
import com.changshagaosu.roadtools.permission.PermissionManager;
import com.changshagaosu.roadtools.preference.LoginPreference;
import com.changshagaosu.roadtools.utils.GPSUtil;
import com.changshagaosu.roadtools.utils.NetworkTool;
import com.nobcdz.upload.URLUtils;
import com.nobcdz.upload.UploadImageService;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckinActivity extends Activity {

	@Override
	protected void onDestroy() {
		super.onDestroy();
		GPSUtil.getInstance().stop();
	}

	private TextView nameTextView;
	private TextView checkdateTextView;
	private TextView loctionTextView;
	private EditText remarkEditText;
	private Spinner statusSpinner;
	private TextView textView;
	private double Latitude;
	private double Longitude;
	private String Status;
	private String Remark;
	private String checkdate;
	private String UserID;
	BroadcastReceiver broadcastReceiver;
	String Key;
	int hours;
	//android6.0以上的危险权限
	private String[] mPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION,
			Manifest.permission.ACCESS_FINE_LOCATION};
	private PermissionManager mPermissionManager;
	private final int REQUEST_CODE = 0x100;

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_checkin);
		mPermissionManager = new PermissionManager(this);
		checkdateTextView = (TextView) findViewById(R.id.CheckDate_tv);
		loctionTextView = (TextView) findViewById(R.id.loction_tv);
		nameTextView = (TextView) findViewById(R.id.name_tv);
		remarkEditText = (EditText) findViewById(R.id.Remark_et);
		statusSpinner = (Spinner) findViewById(R.id.Status_spinner);
		textView = (TextView) findViewById(R.id.text);

		UserID = LoginPreference.find().get("loginID");
		checkdate = new SimpleDateFormat("yyyyMMdd").format(new Date());
		checkdateTextView.setText(checkdate);
		nameTextView.setText(LoginPreference.find().get("userName"));
		hours = Integer.parseInt(new SimpleDateFormat("HH").format(new Date()));
		if (hours >= 12) {
			Status = "1";
		} else {
			Status = "0";
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.simple_spinner_item,
				R.id.text1, new String[] { "上班", "下班" });
		statusSpinner.setAdapter(adapter);

		if (GPSUtil.getInstance().isProviderEnabled()) {
			GPSUtil.getInstance().start();
		} else {
			Toast.makeText(getApplicationContext(), "GPS未打开",
					Toast.LENGTH_SHORT).show();
		}
		broadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				Longitude = intent.getDoubleExtra("Longitude", 0);
				Latitude = intent.getDoubleExtra("Latitude", 0);
				loctionTextView.setText("(" + Longitude + "," + Latitude + ")");
				CheckinActivity.this.unregisterReceiver(broadcastReceiver);
				GPSUtil.getInstance().stop();
				remarkEditText.setText("成功获取经纬度");
				Remark = remarkEditText.getText().toString();
				new PatrolAsyn().execute();
			}
		};
		this.registerReceiver(broadcastReceiver, new IntentFilter(
				"com.changshagaosu.roadtools.utils.GPSUtil"));
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mPermissionManager.lacksPermissions(mPermissions)) {
			startPermissionsActivity();
		}
	}

	private void startPermissionsActivity() {
		PermissionActivity.startActivityForResult(this, REQUEST_CODE, mPermissions);
	}

	public void mOnClick(View view) {
		switch (view.getId()) {
		case R.id.start_btn:
			// Status = String.valueOf(statusSpinner.getSelectedItemPosition());
			if (Key == null) {
				Remark = remarkEditText.getText().toString();
				new PatrolAsyn().execute();
			} else {
				Toast.makeText(getApplicationContext(), "请勿重复保存",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.cancel_btn:
			Intent intent = new Intent(getApplicationContext(),
					UploadImageService.class);
			startService(intent);
			finish();
			break;
		case R.id.take_btn:
			// Status = String.valueOf(statusSpinner.getSelectedItemPosition());
			takePic();
			break;
		default:
			break;
		}
	}

	private ProgressDialog progress;

	private class PatrolAsyn extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = ProgressDialog.show(CheckinActivity.this, "",
					"正在进行签到...");
			progress.setCancelable(true);
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				String url = new URLUtils()
						.getInitData(getApplicationContext())
						+ "Action=AddUserWork"
						+ "&UserID="
						+ UserID
						+ "&Latitude="
						+ Latitude
						+ "&Longitude="
						+ Longitude
						+ "&Remark=" + Remark + "&Status=" + Status;
				url = url.replaceAll(" ", "%20");
				Log.i("show", url);
				String json = NetworkTool.getContentForUTF(url);
				Log.i("show", json);
				JSONObject jsonObject = new JSONObject(json);
				boolean success = jsonObject.getBoolean("success");
				if (success) {
					Key = jsonObject.getJSONObject("data").getString("Key");
					return "1";
				} else {
					String message = jsonObject.getString("message");
					return message;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "提交失败";
		}

		@Override
		protected void onPostExecute(String result) {
			progress.dismiss();
			if (result.equals("1")) {
				textView.setText("签到成功");
				Toast.makeText(getApplicationContext(), "保存成功",
						Toast.LENGTH_LONG).show();
			} else {
				textView.setText("签到失败");
				Toast.makeText(getApplicationContext(), result,
						Toast.LENGTH_LONG).show();
			}
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1:
				try {
					Bitmap bitmap = null;
					Options op = new Options();
					op.inSampleSize = 8;
					op.inJustDecodeBounds = false;
					bitmap = BitmapFactory.decodeFile(imageFile.getPath(), op);
					createThumbnail(bitmap);
					
					Intent intent = new Intent(getApplicationContext(),
							UploadImageService.class);
					startService(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			default:
				break;
			}
		} else if (requestCode == REQUEST_CODE && resultCode == PermissionActivity.PERMISSIONS_DENIED) {
			finish();
		}
	}

	public String createThumbnail(Bitmap source) {
		Bitmap imgThumb = ThumbnailUtils.extractThumbnail(source, 220, 145);
		return saveBitmap(imgThumb);
	}

	public String saveBitmap(Bitmap bm) {
		File f = new File(imagePath, imageName_s);
		try {
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private File imageFile;
	private File imagePath;
	private String imageName_s;

	private void takePic() {
		String imageName = checkdate + "_" + Status + ".jpg";
		imageName_s = checkdate + "_" + Status + "_s.jpg";

		imagePath = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ "/RoadTools/UploadPicture/WorkUser/"
				+ UserID + "/");
		if (!imagePath.exists()) {
			imagePath.mkdirs();
		}
		imageFile = new File(imagePath + "/" + imageName);
//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
//		startActivityForResult(intent, 1);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			//Android7.0以下
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
			startActivityForResult(intent, 1);
		} else {
			//Android7.0以上
			if (!imageFile.getParentFile().exists()) imageFile.getParentFile().mkdirs();
			Uri imageUri = FileProvider.getUriForFile(CheckinActivity.this, "com.changshagaosu.roadtools", imageFile);//通过FileProvider创建一个content类型的Uri
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
			intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
			startActivityForResult(intent, 1);
		}
	}
}