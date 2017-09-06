package com.changshagaosu.roadtools.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.changshagaosu.roadtools.R;
import com.changshagaosu.roadtools.adapter.RoadLineAdapter;
import com.changshagaosu.roadtools.bean.RoadLine;
import com.changshagaosu.roadtools.permission.PermissionActivity;
import com.changshagaosu.roadtools.permission.PermissionManager;
import com.changshagaosu.roadtools.preference.BasePreference;
import com.changshagaosu.roadtools.preference.LoginPreference;
import com.changshagaosu.roadtools.utils.NetworkTool;
import com.nobcdz.upload.URLUtils;

import net.tsz.afinal.FinalDb;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 巡查日志
 */
public class PatrolActivity extends Activity {
    private TextView checkDateTextView;
    private EditText userEditText;
    private EditText contentEditText;
    private EditText roadpartEditText;
    private Spinner roadlineSpinner;
    private Spinner weatherSpinner;
    private RoadLineAdapter adapter;
    private FinalDb db;
    private String CheckDate;
    private String CheckRoadPart;
    private String Weather;
    private String RoadLineID;
    private String RoadLineName;
    private String LoginID;
    private String content;
    private List<RoadLine> lists;
    private String CheckDailyRecordID;
    private String[] weathers;
    private String InspectPerson;
    private String CheckPerson;
    //android6.0以上的危险权限
    private String[] mPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private PermissionManager mPermissionManager;
    private final int REQUEST_CODE = 0x100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_patrol);
        mPermissionManager = new PermissionManager(this);

        checkDateTextView = (TextView) findViewById(R.id.CheckDate_tv);
        roadpartEditText = (EditText) findViewById(R.id.roadpart_et);
        contentEditText = (EditText) findViewById(R.id.content_et);
        userEditText = (EditText) findViewById(R.id.user_et);
        roadlineSpinner = (Spinner) findViewById(R.id.roadline_spinner);
        weatherSpinner = (Spinner) findViewById(R.id.weather_spinner);

        db = FinalDb.create(getApplicationContext());
        lists = db.findAll(RoadLine.class);
        if (!lists.isEmpty()) {
            adapter = new RoadLineAdapter(getApplicationContext(), lists);
            roadlineSpinner.setAdapter(adapter);
            RoadLineID = lists.get(0).getLineID();
            RoadLineName = lists.get(0).getLineName();
        }
        weathers = getResources().getStringArray(R.array.weathers);
        ArrayAdapter<String> weatherAdapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.simple_spinner_item,
                R.id.text1, weathers);
        weatherSpinner.setAdapter(weatherAdapter);
        Weather = weathers[0];

        CheckDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        LoginID = LoginPreference.find().get("loginID");
        CheckPerson = BasePreference.findcCompanyAndUser().get("user");
        checkDateTextView.setText(CheckDate);
        userEditText.setText(CheckPerson);

        roadlineSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                RoadLineID = lists.get(arg2).getLineID();
                RoadLineName = lists.get(arg2).getLineName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        weatherSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Weather = weathers[arg2];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPermissionManager.lacksPermissions(mPermissions)) {
            startPermissionsActivity();
        }
    }

    //进入权限设置界面
    private void startPermissionsActivity() {

        PermissionActivity.startActivityForResult(this, REQUEST_CODE, mPermissions);
    }

    public void mOnClick(View view) {
        switch (view.getId()) {
            case R.id.start_btn:
                //开始巡查
                dialog();
                break;
            case R.id.cancel_btn:
                finish();
                break;
            default:
                break;
        }
    }

    public void dialog() {
        AlertDialog.Builder builder = new Builder(PatrolActivity.this);
        builder.setMessage("确认开始吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                content = contentEditText.getText().toString();
                CheckRoadPart = roadpartEditText.getText().toString();
                InspectPerson = userEditText.getText().toString();
                if (CheckRoadPart.equals("") || CheckRoadPart == null) {
                    CheckRoadPart = "全线";
                }
                CheckPerson = userEditText.getText().toString();
                BasePreference.saveRoadLine(RoadLineID, null, RoadLineName);
                new PatrolAsyn().execute();
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

    private ProgressDialog progress;

    private class PatrolAsyn extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = ProgressDialog
                    .show(PatrolActivity.this, "", "正在提交日志...");
            progress.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = new URLUtils()
                        .getAddCheckDailyRecord(getApplicationContext())
                        + "&UserID="
                        + LoginID
                        + "&LineID="
                        + RoadLineID
                        + "&LineNames="
                        + RoadLineName
                        + "&InspectStartTime="
                        + CheckDate
                        + "&InspectPerson="
                        + InspectPerson.trim()
                        + "&InspectLinePart="
                        + CheckRoadPart.trim()
                        + "&InspectContent="
                        + content.trim()
                        + "&InspectWeeather=" + Weather;
                url = url.replaceAll(" ", "%20");
                url = url.replaceAll("　　", "%20%20");
                Log.i("show", url);
                String json = NetworkTool.getContentForUTFGet(url);
                Log.i("show", json);
                JSONObject jsonObject = new JSONObject(json);
                boolean success = jsonObject.getBoolean("success");
                if (success) {
                    CheckDailyRecordID = jsonObject.getJSONObject("data")
                            .getString("Key");
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
                Intent intent = new Intent(getApplicationContext(),
                        PatrolingActivity.class);
                intent.putExtra("CheckDailyRecordID", CheckDailyRecordID);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), result,
                        Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }
}