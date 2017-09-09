package com.changshagaosu.roadtools.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * @Annotation <p>描述</p>
 * @Auth Sunny
 * @date 2017/9/9
 * @Version V1.0.0
 */

public class ToastUtils {

    public static void showShort(@NonNull Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    public static void showLong(@NonNull Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showShort(@NonNull Context context, @StringRes int msgId) {
        showShort(context, context.getResources().getString(msgId));
    }

    public static void showLong(@NonNull Context context, @StringRes int msgId) {
        showLong(context, context.getResources().getString(msgId));
    }

}
