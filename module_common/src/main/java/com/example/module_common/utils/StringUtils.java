package com.example.module_common.utils;

import android.text.TextUtils;

public class StringUtils {

    /**
     * 判断字符是否为null或空串，会去除两边空格
     *
     * @param src 待判断的字符
     * @return
     */
    public static boolean isEmpty(String src) {
        return src == null || TextUtils.isEmpty(src.trim());
    }
}
