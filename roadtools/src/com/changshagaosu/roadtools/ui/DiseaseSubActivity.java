package com.changshagaosu.roadtools.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.changshagaosu.roadtools.R;
import com.changshagaosu.roadtools.adapter.Item1Adapter;
import com.changshagaosu.roadtools.adapter.ItemAdapter;
import com.changshagaosu.roadtools.adapter.RepairProjectAdapter;
import com.changshagaosu.roadtools.bean.DeaseItem;
import com.changshagaosu.roadtools.bean.UserBean;
import com.changshagaosu.roadtools.json.RequestManager;
import com.changshagaosu.roadtools.preference.LoginPreference;
import com.changshagaosu.roadtools.ui.view.tree.TreeListView;
import com.changshagaosu.roadtools.utils.NetworkTool;
import com.changshagaosu.roadtools.utils.ToastUtils;
import com.nobcdz.upload.URLUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DiseaseSubActivity extends Activity {
    private String UserID;
    private String Key;
    private String DiseaseCode;//病害类别Id
    private String DiseLocationName;
    private String LMTypeName;//病害主表选择选择的病害类型名称
    private List<DeaseItem> items;
    private List<DeaseItem> items1;
    private Spinner nameSpinner;
    private Item1Adapter item1Adapter;
    private ListView listView;
    private ItemAdapter itemAdapter;
    private String DTypeID;
    private String DTypeName;
    private String DTypeNumber;
    private String DTypeUnit;

    private TextView mTvProjTypeNum;
    private TextView mTvProjUnit;
    private TextView mDeaseTypeName;
    private EditText mETProjectNumber;//工程数量

    //多级树形
    private View mTreeCompact;
    private TreeListView mTreeListView;
    private RepairProjectAdapter mRepairAdapter;
    private List<UserBean> mDataList = new ArrayList<>();
    private String mProjectNumber;
    private String LMTypeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_disease_sub);
        nameSpinner = (Spinner) findViewById(R.id.item_spinner);
        listView = (ListView) findViewById(R.id.list);
        mTvProjTypeNum = (TextView) findViewById(R.id.tv_proj_number);
        mTvProjUnit = (TextView) findViewById(R.id.tv_unit);
        mDeaseTypeName = (TextView) findViewById(R.id.tv_deasename);
        mETProjectNumber = (EditText) findViewById(R.id.et_project_number);

        UserID = LoginPreference.find().get("loginID");
        Intent intent = getIntent();
        if (intent != null) {
            Key = intent.getStringExtra("Key");
            DiseaseCode = intent.getStringExtra("DiseaseCode");
            DiseLocationName = intent.getStringExtra("DiseLocationName");
            LMTypeName = intent.getStringExtra("DieaseTypeName");
            LMTypeID = intent.getStringExtra("DiseLMType");
            mDeaseTypeName.setText(LMTypeName);
            Log.e("RoadTools", "传过来的DiseaseCode:" + DiseaseCode);
        }

        loadData();
        items1 = new ArrayList<>();
        nameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                if (items != null && items.size() > 0) {
                    DTypeID = items.get(position).getDTypeID();
                    DTypeName = items.get(position).getDTypeName();
                    DTypeNumber = items.get(position).getDTypeNumber();
                    DTypeUnit = items.get(position).getDTypeUnit();
                    freshUI();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    /**
     * 刷新界面
     */
    private void freshUI() {
        mTvProjTypeNum.setText(DTypeNumber);
        mTvProjUnit.setText(DTypeUnit);
    }

    public void mOnClick(View view) {
        switch (view.getId()) {
            case R.id.start_btn:
                mProjectNumber = mETProjectNumber.getText().toString().trim();
                if (TextUtils.isEmpty(Key) || TextUtils.isEmpty(DTypeName)) {
                    ToastUtils.showShort(DiseaseSubActivity.this, R.string.select_project);
                    return;
                }
                new PatrolAsyn().execute();
                break;
            case R.id.cancel_btn:
                finish();
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("rawtypes")
    protected void executeRequest(Request request) {
        RequestManager.addRequest(request, this);
    }

    /**
     * 加载病害维修项目的数据
     */
    private void loadData() {
//      http://58.20.182.212:89/yhapp/API/API.aspx?Action=SDiseaseItem&DiseaseCode=2ad4c62a-2308-492e-a266-f70ed7608772

        executeRequest(new com.changshagaosu.roadtools.json.GsonRequest<>(
                new URLUtils().getInitData(getApplicationContext())
                        + "Action=SDiseaseItem&" +
                        "DiseaseCode=" + DiseaseCode,
                DeaseItem.RequestData.class, null,
                new Response.Listener<DeaseItem.RequestData>() {
                    @Override
                    public void onResponse(DeaseItem.RequestData response) {
                        if (response != null && response.isSuccess()) {
                            Log.e("RoadTools_Success", response.getData().getDiseaseItem().toString());
                            items = response.getData().getDiseaseItem();
                            if (items != null && items.size() > 0) {
                                item1Adapter = new Item1Adapter(
                                        getApplicationContext(), items);
                                nameSpinner.setAdapter(item1Adapter);
                            }
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("RoadTools_onError", error.getMessage());
            }
        }));
    }

    private ProgressDialog progress;

    /**
     * 增加病害维修项目
     * http://58.20.182.212:89/yhapp/API/API.aspx?Action=SAddSDisease&UserID=3cd1cd06-3d77-4efb-a78d-ad9a9cea3d80
     * &Key=d56a519d-f0e8-4db4-89b1-63aca0ac3c1f
     * &DTypeID=3B7A5704-945E-4F51-B191-63B758FBEA88
     * &Engineering=1111
     * &LMTypeID=null
     * &LMTypeName=null
     */
    private class PatrolAsyn extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = ProgressDialog.show(DiseaseSubActivity.this, "",
                    "正在添加病害维修项目...");
            progress.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = new URLUtils()
                        .getInitData(getApplicationContext())
                        + "Action=SAddSDisease"
                        + "&UserID="
                        + UserID
                        + "&Key="
                        + Key
                        + "&DTypeID="
                        + DTypeID
                        + "&Engineering="
                        + (TextUtils.isEmpty(mProjectNumber) ? 0 : mProjectNumber)
                        + "&LMTypeID="
                        + LMTypeID
                        + "&LMTypeName="
                        + LMTypeName;

                url = url.replaceAll(" ", "%20");
                String json = NetworkTool.getContentForUTFGet(url);
                Log.i("show", url);
                Log.i("show", json);
                JSONObject jsonObject = new JSONObject(json);
                boolean success = jsonObject.getBoolean("success");
                if (success) {
                    DeaseItem item = new DeaseItem();
                    item.setDTypeID(DTypeID);
                    item.setDTypeName(DTypeName);
                    item.setDTypeNumber(DTypeNumber);
                    item.setDTypeUnit(DTypeUnit);
                    items1.add(item);
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
                //添加成功回到病害信息主界面
                Toast.makeText(getApplicationContext(), "添加成功",
                        Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), result,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void doMouse(View view) {
        operateProjNumber(false);
    }

    public void doAdd(View view) {
        operateProjNumber(true);
    }

    /**
     * 修改工程数量
     *
     * @param toAdd
     */
    private void operateProjNumber(boolean toAdd) {

        String projNumber = mETProjectNumber.getText().toString();
        int num = Integer.parseInt(TextUtils.isEmpty(projNumber) ? "0" : projNumber);
        if (toAdd) {
            num++;
        } else {
            num--;
            if (num <= 0) {
                num = 0;
            }
        }

        mETProjectNumber.setText(num + "");
    }
}