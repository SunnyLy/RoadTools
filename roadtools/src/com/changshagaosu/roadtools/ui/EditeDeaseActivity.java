package com.changshagaosu.roadtools.ui;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.changshagaosu.roadtools.R;
import com.changshagaosu.roadtools.base.BaseActivity;
import com.changshagaosu.roadtools.bean.CommonBean;
import com.changshagaosu.roadtools.bean.DeaseProjectBean;
import com.changshagaosu.roadtools.json.RequestManager;
import com.changshagaosu.roadtools.utils.ToastUtils;
import com.nobcdz.upload.URLUtils;

/**
 * @Annotation <p>修改病害信息</p>
 * @Auth Sunny
 * @date 2017/9/9
 * @Version V1.0.0
 */

public class EditeDeaseActivity extends BaseActivity {

    public static final String TAG = EditeDeaseActivity.class.getSimpleName();
    private static final String BUNDLE_KEY = "projectBean";

    private DeaseProjectBean mProjectBean;

    private String mDeaseKey;//病害主Key;
    private String mEngienNumber;//工程数量
    private String mProjectName;//项目名称
    private String mProjectCode;//项目代号
    private String mProjectUnit;//单位

    private TextView mTvProjectName;
    private TextView mTvProjectCode;
    private TextView mTvProjectUnit;

    private Button mBtnMouse, mBtnAdd;
    private EditText mETProjectNumber;


    public static void startEditeDeaseActivity(Context context, DeaseProjectBean projectBean) {
        Intent intent = new Intent(context, EditeDeaseActivity.class);
        if (projectBean != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(TAG, projectBean);
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edite_dease;
    }

    @Override
    public void onContentChanged() {
        mTvProjectName = (TextView) findViewById(R.id.tv_edite_project_name);
        mTvProjectCode = (TextView) findViewById(R.id.tv_proj_number);
        mTvProjectUnit = (TextView) findViewById(R.id.tv_edite_unit);

        mBtnMouse = (Button) findViewById(R.id.btn_edite_mouse);
        mBtnAdd = (Button) findViewById(R.id.btn_edite_add);
        mETProjectNumber = (EditText) findViewById(R.id.et_project_number);

        setOnClickListeners(mBtnMouse, mBtnAdd);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_edite_mouse:
                operateProjNumber(false);
                break;
            case R.id.btn_edite_add:
                operateProjNumber(true);
                break;
            default:
                break;
        }
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

    @Override
    public void initParams() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mProjectBean = (DeaseProjectBean) bundle.get(TAG);
                mDeaseKey = mProjectBean.getItemID() + "";
                mEngienNumber = mProjectBean.getEngineering();
                mProjectName = mProjectBean.getItemName();
                mProjectCode = mProjectBean.getItemCode();//项目代号
                mProjectUnit = mProjectBean.getItemUnit();

                freshUI();
            }
        }
    }

    private void freshUI() {
        mTvProjectName.setText(mProjectName);
        mTvProjectCode.setText(mProjectCode);
        mTvProjectUnit.setText(mProjectUnit);
        mETProjectNumber.setText(mEngienNumber);
    }

    /**
     * 删除病害维修项目
     *
     * @param view
     */
    public void onDelete(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(true)
                .setMessage("确定删除该项目吗？删除后不可恢复！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showLoading(R.string.deleting);
                        RequestManager.addRequest(new com.changshagaosu.roadtools.json.GsonRequest<>(
                                new URLUtils().getInitData(getApplicationContext())
                                        + "Action=SDelSDisease&"
                                        + "sKey=" + mProjectCode,
                                CommonBean.class, null,
                                new Response.Listener<CommonBean>() {
                                    @Override
                                    public void onResponse(CommonBean response) {
                                        hideLoading();
                                        if (response != null && response.isSuccess()) {
                                            ToastUtils.showShort(mContext, R.string.delete_success);
                                            setResult(RESULT_OK);
                                            finish();
                                        }
                                    }
                                }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {

                                hideLoading();
                                ToastUtils.showShort(mContext, R.string.delete_failure);
                            }
                        }), this);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();

    }

    /**
     * 保存病害维修项目的修改
     *
     * @param view
     */
    public void onSave(View view) {
        showLoading(R.string.uploading);
        RequestManager.addRequest(new com.changshagaosu.roadtools.json.GsonRequest<>(
                new URLUtils().getInitData(getApplicationContext())
                        + "Action=SUpSDisease&"
                        + "sKey=" + mProjectCode
                        + "&Engineering=" + mETProjectNumber.getText().toString(),
                CommonBean.class, null,
                new Response.Listener<CommonBean>() {
                    @Override
                    public void onResponse(CommonBean response) {
                        hideLoading();
                        if (response != null && response.isSuccess()) {
                            ToastUtils.showShort(mContext, R.string.upload_success);
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                hideLoading();
                ToastUtils.showShort(mContext, R.string.upload_failure);
            }
        }), this);
    }

    public void onBack(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(true)
                .setMessage("确定退出编辑吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();

    }
}
