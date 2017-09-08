package com.changshagaosu.roadtools.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;

/**
 * @Author sunny
 * @Date 2017/9/8  18:25
 * @Version v1.0.0
 * @Annotation Base基类
 */

public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        setContentView(getLayoutId());
        initParams();
    }

    private void initParams() {

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
}
