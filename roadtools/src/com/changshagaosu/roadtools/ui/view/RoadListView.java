package com.changshagaosu.roadtools.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * ------------------------------------------------
 * Copyright © 2014年 CLife. All Rights Reserved.
 * Shenzhen H&T Intelligent Control Co.,Ltd.
 * -----------------------------------------------
 *
 * @Author sunny
 * @Date 2017/9/9  15:01
 * @Version v1.0.0
 * @Annotation
 */

public class RoadListView extends ListView {
    public RoadListView(Context context) {
        super(context);
    }

    public RoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
