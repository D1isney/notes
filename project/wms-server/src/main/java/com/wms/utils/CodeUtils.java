package com.wms.utils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * 通过时间格式生成出唯一的Code
 */
public class CodeUtils {

    public static String getString(String s2) {
        String s = s2;
        if (Objects.isNull(s)) {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat(DateConstant.YY_MM_DD);
            s = sdf.format(date) + "01";
            return s;
        } else {
            int i = Integer.parseInt(s);
            return String.valueOf(++i);
        }
    }

}
