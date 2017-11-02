package com.changshagaosu.roadtools.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ------------------------------------------------
 * Copyright © 2014-2017 CLife. All Rights Reserved.
 * Shenzhen H&T Intelligent Control Co.,Ltd.
 * -----------------------------------------------
 *
 * @Author sunny
 * @Date 2017/11/2  13:24
 * @Version v1.0.0
 * @Annotation 小数点工具类
 */
public class DecimalUtil {

    /**
     * @param number
     * @return
     */
    public static boolean isOnlyPointNumber(String number, int keepNo) {
        Pattern pattern = Pattern.compile("^\\d+\\.?\\d{0," + keepNo + "}$");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }
}
