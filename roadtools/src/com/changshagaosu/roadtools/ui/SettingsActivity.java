package com.changshagaosu.roadtools.ui;

import java.util.List;

import net.tsz.afinal.FinalDb;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.changshagaosu.roadtools.BuildConfig;
import com.changshagaosu.roadtools.R;
import com.changshagaosu.roadtools.bean.RoadLine;
import com.changshagaosu.roadtools.preference.LoginPreference;
import com.changshagaosu.roadtools.utils.DataCleanManager;
import com.umeng.update.UmengUpdateAgent;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
	}

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        TextView tvVersion = (TextView) findViewById(R.id.bpm_tv);
        tvVersion.setText(BuildConfig.VERSION_NAME);
    }

	public void mOnClick(View view) {
		switch (view.getId()) {
			case R.id.take_pic:
				Intent intent = new Intent(getApplicationContext(),
						PictureActivity.class);
				startActivity(intent);
				break;
			case R.id.logout:
				AlertDialog.Builder builder = new Builder(SettingsActivity.this);
				builder.setMessage("确认退出吗？");
				builder.setTitle("提示");
				builder.setPositiveButton("确认", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						LoginPreference.clear();
						FinalDb db = FinalDb.create(getApplicationContext());
						List<RoadLine> lines = db.findAll(RoadLine.class);
						for (RoadLine line : lines) {
							db.delete(line);
						}
						finish();
					}
				});
				builder.setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.create().show();

				break;
			case R.id.clean:
				AlertDialog.Builder builder2 = new Builder(SettingsActivity.this);
				builder2.setMessage("确认清除吗？");
				builder2.setTitle("提示");
				builder2.setPositiveButton("确认", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						DataCleanManager.cleanApplicationData(
								getApplicationContext(),
								"mnt/sdcard/RoadTools/Images");
						Toast.makeText(getApplicationContext(), "数据清除完毕，请重新打开软件",
								Toast.LENGTH_LONG).show();
					}
				});
				builder2.setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder2.create().show();

				break;
			case R.id.update:
				AlertDialog.Builder builder3 = new Builder(SettingsActivity.this);
				builder3.setMessage("确认检查吗？");
				builder3.setTitle("提示");
				builder3.setPositiveButton("确认", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						UmengUpdateAgent.forceUpdate(getApplicationContext());
					}
				});
				builder3.setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder3.create().show();

				break;
			default:
				break;
		}
	}

}
