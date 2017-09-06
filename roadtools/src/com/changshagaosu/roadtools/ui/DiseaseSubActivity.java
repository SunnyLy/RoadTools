package com.changshagaosu.roadtools.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.changshagaosu.roadtools.R;
import com.changshagaosu.roadtools.adapter.Item1Adapter;
import com.changshagaosu.roadtools.adapter.ItemAdapter;
import com.changshagaosu.roadtools.bean.Item;
import com.changshagaosu.roadtools.json.RequestManager;
import com.changshagaosu.roadtools.preference.LoginPreference;
import com.changshagaosu.roadtools.utils.NetworkTool;
import com.nobcdz.upload.URLUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DiseaseSubActivity extends Activity {
    private String UserID;
    private String Key;
    private String DiseaseCode;
    private String DiseLocationName;
    private TextView engineeringTextView;
    private List<Item> items;
    private List<Item> items1;
    private Spinner nameSpinner;
    private Item1Adapter item1Adapter;
    private ListView listView;
    private ItemAdapter itemAdapter;
    private String DTypeID;
    private String DTypeName;
    private String DTypeNumber;
    private String DTypeUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_disease_sub);
        engineeringTextView = (TextView) findViewById(R.id.Engineering_tv);
        nameSpinner = (Spinner) findViewById(R.id.item_spinner);
        listView = (ListView) findViewById(R.id.list);

        UserID = LoginPreference.find().get("loginID");
        Key = getIntent().getStringExtra("Key");
        DiseaseCode = getIntent().getStringExtra("DiseaseCode");
        DiseLocationName = getIntent().getStringExtra("DiseLocationName");

        engineeringTextView.setText(DiseLocationName);

        loadData();

        items1 = new ArrayList<Item>();

        nameSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (items != null && items.size() > 0) {
                    DTypeID = items.get(arg2).getDTypeID();
                    DTypeName = items.get(arg2).getDTypeName();
                    DTypeNumber = items.get(arg2).getDTypeNumber();
                    DTypeUnit = items.get(arg2).getDTypeUnit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    public void mOnClick(View view) {
        switch (view.getId()) {
            case R.id.start_btn:
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

    private void loadData() {
        executeRequest(new com.changshagaosu.roadtools.json.GsonRequest<Item.RequestData>(
                new URLUtils().getInitData(getApplicationContext())
                        + "Action=DiseaseItem&Key=" + Key + "&DiseaseCode="
                        + DiseaseCode, Item.RequestData.class, null,
                new Listener<Item.RequestData>() {
                    @Override
                    public void onResponse(Item.RequestData response) {
                        if (response.isSuccess()) {
                            if (items != null && items.size() > 0) {
                                items = response.getData().getDiseaseItem();
                                item1Adapter = new Item1Adapter(
                                        getApplicationContext(), items);
                                nameSpinner.setAdapter(item1Adapter);
                                DTypeID = items.get(0).getDTypeID();
                                DTypeName = items.get(0).getDTypeName();
                                DTypeNumber = items.get(0).getDTypeNumber();
                                DTypeUnit = items.get(0).getDTypeUnit();
                            }
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }));
    }

    private ProgressDialog progress;

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
                        + "Action=AddSDisease"
                        + "&UserID="
                        + UserID
                        + "&Key="
                        + Key
                        + "&DTypeID="
                        + DTypeID
                        + "&Engineering="
                        + DiseLocationName;

                url = url.replaceAll(" ", "%20");
                String json = NetworkTool.getContentForUTFGet(url);
                Log.i("show", url);
                Log.i("show", json);
                JSONObject jsonObject = new JSONObject(json);
                boolean success = jsonObject.getBoolean("success");
                if (success) {
                    Item item = new Item();
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
                itemAdapter = new ItemAdapter(getApplicationContext(), items1);
                listView.setAdapter(itemAdapter);
                Toast.makeText(getApplicationContext(), "添加成功",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), result,
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}