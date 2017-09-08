package com.changshagaosu.roadtools.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.changshagaosu.roadtools.R;
import com.changshagaosu.roadtools.adapter.Item1Adapter;
import com.changshagaosu.roadtools.adapter.ItemAdapter;
import com.changshagaosu.roadtools.adapter.RepairProjectAdapter;
import com.changshagaosu.roadtools.bean.RepairProjectBean;
import com.changshagaosu.roadtools.bean.UserBean;
import com.changshagaosu.roadtools.callback.ITreeSelectCallback;
import com.changshagaosu.roadtools.json.RequestManager;
import com.changshagaosu.roadtools.preference.LoginPreference;
import com.changshagaosu.roadtools.ui.view.tree.TreeListView;
import com.changshagaosu.roadtools.ui.view.tree.TreeNode;
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
    private List<RepairProjectBean> items;
    private List<RepairProjectBean> items1;
    //    private Spinner nameSpinner;
    private TextView mTvProjName;//项目名称层级树
    private Item1Adapter item1Adapter;
    private ListView listView;
    private ItemAdapter itemAdapter;
    private String DTypeID;
    private String DTypeName;
    private String DTypeNumber;
    private String DTypeUnit;

    //多级树形
    private View mTreeCompact;
    private TreeListView mTreeListView;
    private RepairProjectAdapter mRepairAdapter;
    private PopupWindow mPopupWindow;
    private List<UserBean> mDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_disease_sub);
//        nameSpinner = (Spinner) findViewById(R.id.item_spinner);
        mTvProjName = (TextView) findViewById(R.id.item_spinner);
        listView = (ListView) findViewById(R.id.list);

        UserID = LoginPreference.find().get("loginID");
        Key = getIntent().getStringExtra("Key");
        DiseaseCode = getIntent().getStringExtra("DiseaseCode");
        DiseLocationName = getIntent().getStringExtra("DiseLocationName");

        //loadData();
        makeData();

        items1 = new ArrayList<RepairProjectBean>();

        mTreeCompact = LayoutInflater.from(this).inflate(R.layout.widget_tree_view, null);
        mTreeListView = (TreeListView) mTreeCompact.findViewById(R.id.tree_lv);
        mRepairAdapter = new RepairProjectAdapter(this, mDataList);
        mRepairAdapter.setmCallback(new ITreeSelectCallback() {
            @Override
            public void onItemSelected(int id, boolean selected) {

            }
        });
        mTreeListView.setAdapter(mRepairAdapter);
        initPopupWindow();
        mTvProjName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPopupWindow != null && !mPopupWindow.isShowing()) {
                    mPopupWindow.showAsDropDown(mTvProjName);
                } else if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }

            }
        });

//        nameSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> arg0, View arg1,
//                                       int arg2, long arg3) {
//                if (items != null && items.size() > 0) {
//                    DTypeID = items.get(arg2).getDTypeID();
//                    DTypeName = items.get(arg2).getDTypeName();
//                    DTypeNumber = items.get(arg2).getDTypeNumber();
//                    DTypeUnit = items.get(arg2).getDTypeUnit();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//
//            }
//        });
    }

    private void initPopupWindow() {
        mPopupWindow = new PopupWindow(mTreeCompact, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //mPopupWindow.setContentView(mTreeCompact);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
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

    /**
     * 加载病害维修项目的数据
     */
    private void loadData() {
        executeRequest(new com.changshagaosu.roadtools.json.GsonRequest<RepairProjectBean.RequestData>(
                new URLUtils().getInitData(getApplicationContext())
                        + "Action=DiseaseItem&Key=" + Key + "&DiseaseCode="
                        + DiseaseCode, RepairProjectBean.RequestData.class, null,
                new Listener<RepairProjectBean.RequestData>() {
                    @Override
                    public void onResponse(RepairProjectBean.RequestData response) {
                        if (response.isSuccess()) {
                            if (items != null && items.size() > 0) {
                                items = response.getData().getDiseaseItem();
                                item1Adapter = new Item1Adapter(
                                        getApplicationContext(), items);
                                //  nameSpinner.setAdapter(item1Adapter);
                                DTypeID = items.get(0).getDTypeID();
                                DTypeName = items.get(0).getDTypeName();
                                DTypeNumber = items.get(0).getDTypeNumber();
                                DTypeUnit = items.get(0).getDTypeUnit();
                            } else {
                                makeData();
                            }
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }));
    }

    /**
     * 静态数据
     */
    private void makeData() {
        mDataList.clear();
        // 根组
        UserBean bean = new UserBean(0x01, TreeNode.ROOT_PARENT_ID, true, "第1组");
        mDataList.add(bean);
        bean = new UserBean(0x02, TreeNode.ROOT_PARENT_ID, true, "第2组");
        mDataList.add(bean);
        bean = new UserBean(0x03, TreeNode.ROOT_PARENT_ID, true, "第3组");
        mDataList.add(bean);
        bean = new UserBean(0x04, TreeNode.ROOT_PARENT_ID, true, "第4组");
        mDataList.add(bean);
        bean = new UserBean(0x05, TreeNode.ROOT_PARENT_ID, true, "第5组");
        mDataList.add(bean);
        bean = new UserBean(0x06, TreeNode.ROOT_PARENT_ID, true, "第6组");
        mDataList.add(bean);

        // ////////////////////////////
        // 第1组
        bean = new UserBean(0x11, 0x01, true, "1组_1");
        mDataList.add(bean);
        bean = new UserBean(0x12, 0x01, true, "1组_2");
        mDataList.add(bean);

        // ////////////////////////////
        // 1组_1
        bean = new UserBean(0x111, 0x11, false, "卡卡西");
        mDataList.add(bean);
        bean = new UserBean(0x112, 0x11, false, "白牙");
        mDataList.add(bean);
        bean = new UserBean(0x113, 0x11, true, "1组_1_1");
        mDataList.add(bean);

        // ////////////////////////////
        // 1组_1_1
        bean = new UserBean(0x1131, 0x113, false, "九尾妖狐");
        mDataList.add(bean);

        // ////////////////////////////
        // 1组_2
        bean = new UserBean(0x1211, 0x12, false, "波风水门");
        mDataList.add(bean);

        // ////////////////////////////
        // 第2组
        bean = new UserBean(0x21, 0x02, true, "2组_1");
        mDataList.add(bean);
        bean = new UserBean(0x22, 0x02, false, "鸣人");
        mDataList.add(bean);
        bean = new UserBean(0x23, 0x02, false, "佐助");
        mDataList.add(bean);

        // ////////////////////////////
        // 2组_1
        bean = new UserBean(0x211, 0x21, false, "我爱罗");
        mDataList.add(bean);

        // ////////////////////////////
        // 第3组
        bean = new UserBean(0x31, 0x03, false, "大蛇丸");
        mDataList.add(bean);
        bean = new UserBean(0x32, 0x03, false, "自来也");
        mDataList.add(bean);
        bean = new UserBean(0x33, 0x03, false, "纲手");
        mDataList.add(bean);

        // ////////////////////////////
        // 第5组
        bean = new UserBean(0x51, 0x05, true, "5组_1");
        mDataList.add(bean);
        bean = new UserBean(0x52, 0x05, false, "君麻吕");
        mDataList.add(bean);

        // ////////////////////////////
        // 5组_1
        bean = new UserBean(0x511, 0x51, true, "5组_1_1");
        mDataList.add(bean);
        bean = new UserBean(0x512, 0x51, false, "春野樱");
        mDataList.add(bean);

        // ////////////////////////////
        // 5组_1_1
        bean = new UserBean(0x5111, 0x511, true, "5组_1_1_1");
        mDataList.add(bean);
        bean = new UserBean(0x5112, 0x511, false, "鹿丸");
        mDataList.add(bean);

        // ////////////////////////////
        // 5组_1_1_1
        bean = new UserBean(0x51111, 0x5111, true, "5组_1_1_1_1");
        mDataList.add(bean);
        bean = new UserBean(0x51112, 0x5111, false, "柱间");
        mDataList.add(bean);

        // ////////////////////////////
        // 5组_1_1_1_1
        bean = new UserBean(0x511111, 0x51111, false, "宇智波鼬");
        mDataList.add(bean);
        bean = new UserBean(0x511112, 0x51111, false, "迪达拉");
        mDataList.add(bean);
        bean = new UserBean(0x511113, 0x51111, false, "佩恩");
        mDataList.add(bean);
        bean = new UserBean(0x511114, 0x51111, false, "阿飞");
        mDataList.add(bean);
        bean = new UserBean(0x511115, 0x51111, false, "黑绝");
        mDataList.add(bean);

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
                    RepairProjectBean item = new RepairProjectBean();
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