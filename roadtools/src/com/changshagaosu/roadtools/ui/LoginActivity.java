package com.changshagaosu.roadtools.ui;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.changshagaosu.roadtools.R;
import com.changshagaosu.roadtools.preference.FtpPreference;
import com.changshagaosu.roadtools.preference.LoginPreference;
import com.changshagaosu.roadtools.preference.ServerPreference;
import com.changshagaosu.roadtools.utils.NetworkTool;
import com.nobcdz.upload.URLUtils;

@SuppressLint("ShowToast")
public class LoginActivity extends Activity {
	private EditText userNameEdit, pwdEdit;
	private Spinner serverSpinner, ftpSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
		selectHost();
	}

	private void selectHost() {
		ArrayAdapter adapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.simple_spinner_item,
				R.id.text1, new String[] { "58.20.182.212:89",
				ServerPreference.getDiyHost() });
		serverSpinner.setAdapter(adapter);
		if (ServerPreference.getHostName().equals("58.20.182.212:89")) {
			serverSpinner.setSelection(0);
		} else {
			serverSpinner.setSelection(1);
		}

		serverSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				TextView textView = (TextView) arg1.findViewById(R.id.text1);
				ServerPreference.save(textView.getText().toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		ArrayAdapter ftpAdapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.simple_spinner_item,
				R.id.text1, new String[] { "58.20.182.212:6009",
				FtpPreference.getDiyHost() });
		ftpSpinner.setAdapter(ftpAdapter);
		if (FtpPreference.getHostName().equals("58.20.182.212:6009")) {
			ftpSpinner.setSelection(0);
		} else {
			ftpSpinner.setSelection(1);
		}

		ftpSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				TextView textView = (TextView) arg1.findViewById(R.id.text1);
				FtpPreference.save(textView.getText().toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	public void mOnClick(View view) {
		switch (view.getId()) {
			case R.id.login_btn:
				String userName = userNameEdit.getText().toString();
				String password = pwdEdit.getText().toString();
				Log.i("show", "userName:" + userName);
				if (userName.equals("") || password.equals("")) {
					Toast.makeText(getApplicationContext(), "用户名或密码不能为空",
							Toast.LENGTH_LONG).show();
				} else {
					new LoginAsyn().execute(userName, password);
				}
				break;

			case R.id.add_btn:
				final EditText editText = new EditText(LoginActivity.this);
				new AlertDialog.Builder(LoginActivity.this).setTitle("请输入FTP地址")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setView(editText)
						.setPositiveButton("确定", new OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								FtpPreference.saveDiyHost(editText.getText()
										.toString());
								ArrayAdapter adapter = new ArrayAdapter<String>(
										getApplicationContext(),
										R.layout.simple_spinner_item, R.id.text1,
										new String[] { "58.20.182.212:6009",
												FtpPreference.getDiyHost() });
								ftpSpinner.setAdapter(adapter);
							}
						}).setNegativeButton("取消", null).show();
				break;
			case R.id.add_btn_1:
				final EditText editText_1 = new EditText(LoginActivity.this);
				new AlertDialog.Builder(LoginActivity.this).setTitle("请输入服务器地址")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setView(editText_1)
						.setPositiveButton("确定", new OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								ServerPreference.saveDiyHost(editText_1.getText()
										.toString());
								ArrayAdapter adapter = new ArrayAdapter<String>(
										getApplicationContext(),
										R.layout.simple_spinner_item, R.id.text1,
										new String[] { "58.20.182.212:89",
												ServerPreference.getDiyHost() });
								serverSpinner.setAdapter(adapter);
							}
						}).setNegativeButton("取消", null).show();
				break;
			default:
				break;
		}
	}

	private void initView() {
		userNameEdit = (EditText) findViewById(R.id.username_et);
		pwdEdit = (EditText) findViewById(R.id.password_et);
		serverSpinner = (Spinner) findViewById(R.id.server_spinner_1);
		ftpSpinner = (Spinner) findViewById(R.id.server_spinner);
	}

	private ProgressDialog progress;

	private class LoginAsyn extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = ProgressDialog.show(LoginActivity.this, "", "正在登录...");
			progress.setCancelable(true);
		}

		@Override
		protected String doInBackground(String... params) {
			return getUserData(params[0], params[1]);
		}

		@Override
		protected void onPostExecute(String result) {
			progress.dismiss();
			if (result.equals("1")) {
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(intent);
				finish();
			} else {
				Toast.makeText(getApplicationContext(), result,
						Toast.LENGTH_LONG).show();
			}
		}
	}

	private String getUserData(String userName, String password) {

		try {
			String url = new URLUtils().getLogOn(getApplicationContext())
					+ "&username=" + userName + "&userpwd=" + password;
			String json = NetworkTool.getContentForUTF(url);
			Log.i("url", url);
			JSONObject jsonObject = new JSONObject(json);

			boolean success = jsonObject.getBoolean("success");
			if (success) {
				String loginID = jsonObject.getJSONObject("data").getString(
						"UserID");
				LoginPreference.save(userName, password, loginID);

				return "1";
			} else {
				String message = jsonObject.getString("message");
				return message;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "登录失败";
	}
}
