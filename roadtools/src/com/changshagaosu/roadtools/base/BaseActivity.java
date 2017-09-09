package com.changshagaosu.roadtools.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.changshagaosu.roadtools.R;

/**
 * @Author sunny
 * @Date 2017/9/8  18:25
 * @Version v1.0.0
 * @Annotation Base基类
 */

public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {

    public Context mContext;
    private ProgressDialog mDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.umeng_fb_slide_in_from_right, R.anim.umeng_fb_slide_out_from_left);
        mContext = this;
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        setContentView(getLayoutId());
        mDialog = new ProgressDialog(this);
        initParams();
    }

    @Override
    public void finish() {
        overridePendingTransition(R.anim.umeng_fb_slide_in_from_left, R.anim.umeng_fb_slide_out_from_right);
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void initParams() {

    }

    @Override
    public void onClick(View view) {

    }

    //获取布局id
    protected abstract int getLayoutId();

    /**
     * 设置点击监听事件
     *
     * @param views
     */
    public void setOnClickListeners(View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                view.setOnClickListener(this);
            }
        }
    }

    /**
     * 显示加载进度框
     *
     * @param msgId
     */
    public void showLoading(@StringRes int msgId) {

        if (mDialog == null) {
            mDialog = new ProgressDialog(mContext);
        }
        if (!mDialog.isShowing()) {
            mDialog.setMessage(mContext.getResources().getString(msgId));
            mDialog.show();
        }
    }

    public void hideLoading() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.hide();
            mDialog = null;
        }
    }
}
