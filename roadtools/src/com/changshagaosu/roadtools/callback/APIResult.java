package com.changshagaosu.roadtools.callback;

import com.zhouyou.http.model.ApiResult;

/**
 * ------------------------------------------------
 * Copyright © 2014年 CLife. All Rights Reserved.
 * Shenzhen H&T Intelligent Control Co.,Ltd.
 * -----------------------------------------------
 *
 * @Author sunny
 * @Date 2017/9/9  10:29
 * @Version v1.0.0
 * @Annotation
 */

public class APIResult<T> extends ApiResult<T> {
    private boolean success;
    private T data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    @Override
    public boolean isOk() {
        return success;
    }

    @Override
    public String toString() {
        return "APIResult{" +
                "success=" + success +
                ", data=" + data +
                '}';
    }
}
