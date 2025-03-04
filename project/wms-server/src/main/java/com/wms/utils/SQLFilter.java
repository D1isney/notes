package com.wms.utils;


import com.wms.exception.EException;
import org.apache.commons.lang.StringUtils;

public class SQLFilter {

    /**
     * 数据格式的校验
     * SQL注入过滤
     * @param str  待验证的字符串
     */
    public static String sqlInject(String str){
        if(StringUtils.isBlank(str)){
            return null;
        }
        //去掉'|"|;|\字符
        str = StringUtils.replace(str, "'", "");
        str = StringUtils.replace(str, "\"", "");
        str = StringUtils.replace(str, ";", "");
        str = StringUtils.replace(str, "\\", "");

        //转换成小写
        str = str.toLowerCase();

        //非法字符
        String[] keywords = {"truncate", "insert", "select", "delete", "update", "declare", "alert", "create ", "drop"};

        //判断是否包含非法字符
        for(String keyword : keywords){
            if(str.contains(keyword)){
                throw new EException("包含非法字符");
            }
        }

        return str;
    }
}
