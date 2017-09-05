package com.changshagaosu.roadtools.ui;

import java.io.File;
import java.io.FileOutputStream;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.changshagaosu.roadtools.R;
import com.changshagaosu.roadtools.preference.LoginPreference;
import com.changshagaosu.roadtools.utils.NetworkTool;
import com.nobcdz.upload.URLUtils;
import com.nobcdz.upload.UploadImageService;

public class PatrolContentActivity extends Activity {
	private EditText inspectContentEditText;
	private EditText problemLocationEditText;
	private EditText problemContentEditText;
	private EditText dealMeasuresEditText;
	private String inspectContent;
	private String problemLocation;
	private String problemContent;
	private String dealMeasures;
	private String UserID;
	private String Key;
	private String subKey;
	private TextView textView;

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_patrol_content);
		inspectContentEditText = (EditText) findViewById(R.id.InspectContent);
		problemLocationEditText = (EditText) findViewById(R.id.ProblemLocation);
		problemContentEditText = (EditText) findViewById(R.id.ProblemContent);
		dealMeasuresEditText = (EditText) findViewById(R.id.DealMeasures);
		textView = (TextView) findViewById(R.id.text);
		UserID = LoginPreference.find().get("loginID");
		Key = getIntent().getStringExtra("Key");

	}

	public void mOnClick(View view) {
		switch (view.getId()) {
			case R.id.start_btn:
				inspectContent = inspectContentEditText.getText().toString();
				problemLocation = problemLocationEditText.getText().toString();
				problemContent = problemContentEditText.getText().toString();
				dealMeasures = dealMeasuresEditText.getText().toString();
				new PatrolAsyn().execute();
				break;
			case R.id.cancel_btn:
				Intent intent = new Intent(getApplicationContext(),
						UploadImageService.class);
				startService(intent);
				finish();
				break;
			case R.id.take_btn:
				if (subKey != null) {
					takePic();
				} else {
					Toast.makeText(getApplicationContext(), "请先保存巡查内容",
							Toast.LENGTH_LONG).show();
				}
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
			progress = ProgressDialog.show(PatrolContentActivity.this, "",
					"正在提交巡查内容...");
			progress.setCancelable(true);
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				String url = new URLUtils()
						.getInitData(getApplicationContext())
						+ "Action=AddSLog"
						+ "&UserID="
						+ UserID
						+ "&Key="
						+ Key
						+ "&InspectContent="
						+ inspectContent
						+ "&ProblemLocation="
						+ problemLocation
						+ "&ProblemContent="
						+ problemContent
						+ "&DealMeasures=" + dealMeasures;
				url = url.replaceAll(" ", "%20");
				String json = NetworkTool.getContentForUTFGet(url);
				Log.i("show", url);
				Log.i("show", json);
				JSONObject jsonObject = new JSONObject(json);
				boolean success = jsonObject.getBoolean("success");
				if (success) {
					subKey = jsonObject.getJSONObject("data").getString("Key");
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
				textView.setText("巡查内容保存成功");
				Toast.makeText(getApplicationContext(), "提交成功",
						Toast.LENGTH_LONG).show();
			} else {
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
					} catch (Exception e) {
						e.printStackTrace();
					}
				default:
					break;
			}
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
		long time = System.currentTimeMillis();
		String imageName = time + ".jpg";
		imageName_s = time + "_s.jpg";
		imagePath = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ "/RoadTools/UploadPicture/InspectLog/"
				+ subKey + "/");
		if (!imagePath.exists()) {
			imagePath.mkdirs();
		}
		imageFile = new File(imagePath + "/" + imageName);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
		startActivityForResult(intent, 1);
	}
}