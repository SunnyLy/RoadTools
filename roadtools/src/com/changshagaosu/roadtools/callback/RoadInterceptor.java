package com.changshagaosu.roadtools.callback;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

import static com.zhouyou.http.utils.HttpUtil.UTF8;

/**
 * ------------------------------------------------
 * Copyright © 2014年 CLife. All Rights Reserved.
 * Shenzhen H&T Intelligent Control Co.,Ltd.
 * -----------------------------------------------
 *
 * @Author sunny
 * @Date 2017/9/9  10:23
 * @Version v1.0.0
 * @Annotation
 */

public class RoadInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        return process(chain);
    }


    private boolean isText(MediaType mediaType) {
        if (mediaType == null)
            return false;
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            if (mediaType.subtype().equals("json")) {
                return true;
            }
        }
        return false;
    }

    private Response process(Chain chain) throws IOException {
        Request request = chain.request();
        // try the request
        Response response = chain.proceed(request);
        /**通过如下的办法曲线取到请求完成的数据
         *
         * 原本想通过  originalResponse.body().string()
         * 去取到请求完成的数据,但是一直报错,不知道是okhttp的bug还是操作不当
         *
         * 然后去看了okhttp的源码,找到了这个曲线方法,取到请求完成的数据后,根据特定的判断条件去判断token过期
         */
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }
        String bodyString = buffer.clone().readString(charset);
        //Logc.i("uuok network interceptor:" + bodyString + " host:" + request.url().toString() + "\r\n header:" + request.headers());
        boolean isText = isText(contentType);
        if (!isText) {
            return response;
        }

        if (!TextUtils.isEmpty(bodyString)) {
            APIResult apiResult = new Gson().fromJson(bodyString, APIResult.class);
            if (apiResult != null) {
                Log.e("RoadTools", apiResult.toString());
                if (!apiResult.isSuccess()) {
                    //失败
                    proceeError(chain, request);
                }
            }
        }

        return response;
    }

    private void proceeError(Chain chain, Request request) {

    }

}
