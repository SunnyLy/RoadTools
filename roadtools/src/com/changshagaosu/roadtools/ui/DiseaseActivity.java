package com.changshagaosu.roadtools.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.changshagaosu.roadtools.R;
import com.changshagaosu.roadtools.adapter.DeaseProjectItemAdapter;
import com.changshagaosu.roadtools.adapter.N_DiseLMTypeAdapter;
import com.changshagaosu.roadtools.adapter.N_DiseLocationAdapter;
import com.changshagaosu.roadtools.adapter.N_DiseTypeAdapter;
import com.changshagaosu.roadtools.base.BaseActivity;
import com.changshagaosu.roadtools.bean.DeaseProjectBean;
import com.changshagaosu.roadtools.bean.sub.Disease;
import com.changshagaosu.roadtools.bean.sub.RoadLine;
import com.changshagaosu.roadtools.json.RequestManager;
import com.changshagaosu.roadtools.preference.BasePreference;
import com.changshagaosu.roadtools.preference.LoginPreference;
import com.changshagaosu.roadtools.ui.view.RoadListView;
import com.changshagaosu.roadtools.utils.NetworkTool;
import com.nobcdz.upload.URLUtils;
import com.nobcdz.upload.UploadImageService;

import net.tsz.afinal.FinalDb;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 危害上报
 */
@SuppressLint("SimpleDateFormat")
public class DiseaseActivity extends BaseActivity {
	private String UserID;
	private String LineID;
	private String LineName;
	private String DiseLocationType;
	private String DiseLocationName;
	private String DiseLMType;
	private String DiseLMTypeName;
	private String DiseType;
	private String DiseTypeName;
	private String DiseRank;
	private String DiseStartStakePre;
	private String DiseEndStakePre;
	private String DiseStartStakeLast;
	private String DiseEndStakeLast;
	private String DiseReportTime;
	private String DiseDetail;

	private TextView lineNameTextView;
	private TextView diseReportTimeTextView;
	private TextView infoTextView;

	private Spinner diseLocationNameSpinner;
	private Spinner diseTypeNameSpinner;
	private Spinner diseLMTypeNameSpinner;
	private Spinner diseRankSpinner;

	private EditText diseStartStakePreEditText;
	private EditText diseEndStakePreEditText;
	private EditText diseStartStakeLastEditText;
	private EditText diseEndStakeLastEditText;
	private EditText diseDetailEditText;

	private FinalDb db;
	private List<RoadLine.Traffic> traffics;
	private List<Disease> diseases;
	private List<Disease.DiseaseType> diseaseTypes;

	private N_DiseLMTypeAdapter diseLMTypeAdapter;
	private N_DiseLocationAdapter diseLocationAdapter;
	private N_DiseTypeAdapter diseTypeAdapter;

	private String Key;
	private String SourceID;
	private final int REQUEST_CODE = 0x100;

	//病害维修项目列表
	private List<DeaseProjectBean> mDeaseProjectLists = new ArrayList<>();
	private DeaseProjectItemAdapter mDeaseProjAdapter;
	private RoadListView mLvDeaseItem;

	private LinearLayout mLLDeaseProj;

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
	}

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		getWindow().setSoftInputMode(
//				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//		setContentView(R.layout.activity_disease);
//
//	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_disease;
	}

	@Override
	public void initParams() {
		SourceID = getIntent().getStringExtra("CheckDailyRecordID");
		mDeaseProjectLists.clear();
		mDeaseProjAdapter = new DeaseProjectItemAdapter(this, mDeaseProjectLists);
		getView();
		mLvDeaseItem.setAdapter(mDeaseProjAdapter);
		mLvDeaseItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
				if (mDeaseProjectLists == null || mDeaseProjectLists.size() <= 0) {
					return;
				}
				DeaseProjectBean projectBean = mDeaseProjectLists.get(position);
				Log.e("RoadTools", projectBean.toString());
				//EditeDeaseActivity.startEditeDeaseActivity(DiseaseActivity.this,projectBean);
				Intent intent = new Intent(DiseaseActivity.this, EditeDeaseActivity.class);
				if (projectBean != null) {
					Bundle bundle = new Bundle();
					bundle.putSerializable(EditeDeaseActivity.TAG, projectBean);
					intent.putExtras(bundle);
				}
				startActivityForResult(intent, 0x101);
			}
		});
		init();
		initListener();
	}

	private void init() {
		UserID = LoginPreference.find().get("loginID");
		Map<String, String> map = BasePreference.findRoadLine();
		LineName = map.get("RoadLineName");
		LineID = map.get("RoadLineID");
		lineNameTextView.setText(LineName);

		db = FinalDb.create(getApplicationContext());
		traffics = db.findAllByWhere(RoadLine.Traffic.class, "LineName='"
				+ LineName.split("-")[1].trim() + "'");
		if (!traffics.isEmpty()) {
			diseLocationAdapter = new N_DiseLocationAdapter(
					getApplicationContext(), traffics);
			diseLocationNameSpinner.setAdapter(diseLocationAdapter);
			DiseLocationType = traffics.get(0).getTrafficLineID();
			DiseLocationName = traffics.get(0).getTrafficLineName();
		}

		diseases = db.findAll(Disease.class);
		if (!diseases.isEmpty()) {
			diseTypeAdapter = new N_DiseTypeAdapter(getApplicationContext(),
					diseases);
			diseTypeNameSpinner.setAdapter(diseTypeAdapter);
			DiseType = diseases.get(0).getDictionaryId();
			DiseTypeName = diseases.get(0).getDictionaryName();

			diseaseTypes = db.findAllByWhere(Disease.DiseaseType.class,
					"DictionaryName='" + DiseTypeName + "'");
			if (!diseaseTypes.isEmpty()) {
				diseLMTypeAdapter = new N_DiseLMTypeAdapter(
						getApplicationContext(), diseaseTypes);
				diseLMTypeNameSpinner.setAdapter(diseLMTypeAdapter);
				DiseLMTypeName = diseaseTypes.get(0).getCommonTypeName();
				DiseLMType = diseaseTypes.get(0).getCommonTypeID();
			}
		}

		ArrayAdapter<String> diseaseLevelAdapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.simple_spinner_item,
				R.id.text1, new String[] { "I", "II", "III" });
		diseRankSpinner.setAdapter(diseaseLevelAdapter);
		DiseRank = "I";

		DiseReportTime = new SimpleDateFormat("yyyy-MM-dd HH:mm")
				.format(new Date());
		diseReportTimeTextView.setText(DiseReportTime);

	}

	private void initListener() {
		diseLocationNameSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
											   int arg2, long arg3) {
						DiseLocationType = traffics.get(arg2)
								.getTrafficLineID();
						DiseLocationName = traffics.get(arg2)
								.getTrafficLineName();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

		//病害类别
		diseTypeNameSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
											   int arg2, long arg3) {

						DiseType = diseases.get(arg2).getDictionaryId() + "";
						DiseTypeName = diseases.get(arg2).getDictionaryName();

						diseaseTypes = db.findAllByWhere(
								Disease.DiseaseType.class, "DictionaryName='"
										+ DiseTypeName + "'");
						if (!diseaseTypes.isEmpty()) {
							Log.e("RoadTools", "diseaseTypes列表:" + diseaseTypes.toString());
							diseLMTypeAdapter = new N_DiseLMTypeAdapter(
									getApplicationContext(), diseaseTypes);
							diseLMTypeNameSpinner.setAdapter(diseLMTypeAdapter);
							//当切换病害类别时，病害类型默认选中第0个
							DiseLMTypeName = diseaseTypes.get(0)
									.getCommonTypeName();
							DiseLMType = diseaseTypes.get(0).getCommonTypeID();
							Log.e("RoadTools", "DiseaseType:" + DiseLMType);
						}
						Log.e("RoadTools", "Disease:" + diseases.get(arg2).toString());
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
		//病害类型
		diseLMTypeNameSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
											   int arg2, long arg3) {
						DiseLMTypeName = diseaseTypes.get(arg2)
								.getCommonTypeName();
						DiseLMType = diseaseTypes.get(arg2).getCommonTypeID();

						Log.e("RoadTools", "病害类型:名称:" + DiseLMTypeName + ",ID:" + DiseLMType);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

		diseRankSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				TextView text = (TextView) arg1.findViewById(R.id.text1);
				DiseRank = text.getText().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	private void getView() {
		lineNameTextView = (TextView) findViewById(R.id.LineName);
		diseReportTimeTextView = (TextView) findViewById(R.id.DiseReportTime);
		infoTextView = (TextView) findViewById(R.id.Info);

		diseLocationNameSpinner = (Spinner) findViewById(R.id.DiseLocationName);
		diseTypeNameSpinner = (Spinner) findViewById(R.id.DiseTypeName);
		diseLMTypeNameSpinner = (Spinner) findViewById(R.id.DiseLMTypeName);
		diseRankSpinner = (Spinner) findViewById(R.id.DiseRank);

		diseStartStakePreEditText = (EditText) findViewById(R.id.DiseStartStakePre);
		diseEndStakePreEditText = (EditText) findViewById(R.id.DiseEndStakePre);
		diseStartStakeLastEditText = (EditText) findViewById(R.id.DiseStartStakeLast);
		diseEndStakeLastEditText = (EditText) findViewById(R.id.DiseEndStakeLast);
		diseDetailEditText = (EditText) findViewById(R.id.DiseDetail);

		mLvDeaseItem = (RoadListView) findViewById(R.id.list_project);
		mLLDeaseProj = (LinearLayout) findViewById(R.id.layout_dease_project);
		mLLDeaseProj.setVisibility(View.GONE);
	}

	public void mOnClick(View view) {
		switch (view.getId()) {
			case R.id.disease_btn:
				dialog();
				break;
			case R.id.take_btn:
				if (Key != null) {
					takePic();
				} else {
					Toast.makeText(getApplicationContext(), "请先保存病害信息",
							Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.add_btn:
				if (Key != null) {
					Intent intent = new Intent(getApplicationContext(),DiseaseSubActivity.class);
					intent.putExtra("Key", Key);
					intent.putExtra("DiseaseCode", DiseType);
					intent.putExtra("DiseLocationName", DiseLocationName);
					intent.putExtra("DieaseTypeName", DiseLMTypeName);
					intent.putExtra("DiseLMType", DiseLMType);//

					startActivityForResult(intent, REQUEST_CODE);
				} else {
					Toast.makeText(getApplicationContext(), "请先保存病害信息",
							Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.cancel_btn:
				Intent intent = new Intent(getApplicationContext(),
						UploadImageService.class);
				startService(intent);
				finish();
				break;
			default:
				break;
		}
	}

	public void dialog() {
		AlertDialog.Builder builder = new Builder(DiseaseActivity.this);
		builder.setMessage("确认上报吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				DiseStartStakePre = diseStartStakePreEditText.getText().toString();
				DiseEndStakePre = diseEndStakePreEditText.getText().toString();
				DiseStartStakeLast = diseStartStakeLastEditText.getText()
						.toString();
				DiseEndStakeLast = diseEndStakeLastEditText.getText().toString();
				DiseDetail = diseDetailEditText.getText().toString();
				new DiseaseAsyn().execute();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
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
					break;

				case 0x101:
				case REQUEST_CODE:
					//添加病害维修项目成功
					showLoading(R.string.comm_loading);
					getDeaseItems();

					break;
				default:
					break;
			}
		}
	}

	/**
	 * 获取病害项目列表
	 */
	private void getDeaseItems() {
		RequestManager.addRequest(new com.changshagaosu.roadtools.json.GsonRequest<>(
				new URLUtils().getInitData(getApplicationContext())
						+ "Action=SDiseaseList&"
						+ "Key=" + Key,
				DeaseProjectBean.RequestData.class, null,
				new Response.Listener<DeaseProjectBean.RequestData>() {
					@Override
					public void onResponse(DeaseProjectBean.RequestData response) {
						if (response != null && response.isSuccess()) {
							hideLoading();
							DeaseProjectBean.Data data = response.getData();
							if (data != null) {
								mDeaseProjectLists = data.getDiseaseItem();
								if (mDeaseProjectLists != null && mDeaseProjectLists.size() > 0) {
									mLLDeaseProj.setVisibility(View.VISIBLE);
									Log.e("RoadTools_Success", mDeaseProjectLists.toString());
									mDeaseProjAdapter.notifyDataSetChanged();
								}
							}

						}
					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {

				mLLDeaseProj.setVisibility(View.GONE);
				hideLoading();
				Log.e("RoadTools_onError", error.getMessage());
			}
		}), this);
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
				+ "/RoadTools/UploadPicture/Disease/"
				+ Key
				+ "/");
		if (!imagePath.exists()) {
			imagePath.mkdirs();
		}
		imageFile = new File(imagePath + "/" + imageName);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
		startActivityForResult(intent, 1);
	}

	private ProgressDialog progress;

	private class DiseaseAsyn extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = ProgressDialog.show(DiseaseActivity.this, "",
					"正在提交病害报告...");
			progress.setCancelable(true);
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				String url = new URLUtils()
						.getInitData(getApplicationContext())
						+ "Action=AddMDisease"
						+ "&UserID="
						+ UserID
						+ "&LineID="
						+ LineID
						+ "&LineName="
						+ LineName
						+ "&DiseLocationType="
						+ DiseLocationType
						+ "&DiseLocationName="
						+ DiseLocationName
						+ "&DiseLMType="
						+ DiseLMType
						+ "&DiseLMTypeName="
						+ DiseLMTypeName
						+ "&DiseType="
						+ DiseType
						+ "&DiseTypeName="
						+ DiseTypeName
						+ "&DiseRank="
						+ DiseRank
						+ "&DiseStartStakePre="
						+ DiseStartStakePre
						+ "&DiseEndStakePre="
						+ DiseEndStakePre
						+ "&DiseStartStakeLast="
						+ DiseStartStakeLast
						+ "&DiseEndStakeLast="
						+ DiseEndStakeLast
						+ "&DiseReportTime="
						+ DiseReportTime
						+ "&DiseDetail="
						+ DiseDetail
						+ "&SourceID="
						+ SourceID;
				url = url.replaceAll(" ", "%20");
				url = url.replaceAll("　　", "%20%20");
				String json = NetworkTool.getContentForUTF(url);
				Log.i("show", url);
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
				return "提交失败";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			progress.dismiss();
			if (result.equals("1")) {
				infoTextView.setText("可以拍照了");
				Toast.makeText(getApplicationContext(), "保存成功",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), result,
						Toast.LENGTH_LONG).show();
			}
		}
	}

}