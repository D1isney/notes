package com.wms.constant;


import java.io.File;

public interface DateConstant {
    String DATE_PATH = "yyyy"+ File.separator+"MM"+File.separator+"dd";
    /** 时间格式(yyyy-MM-dd) */
    String DATE_PATTERN = "yyyy-MM-dd";
    String DATE_PATTERN_CODE = "yyyyMMdd";
    /** 时间格式(yyyy-MM-dd HH:mm:ss) */
    String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    String DATE_TIME_PATTERN_CODE = "yyyyMMddHHmmss";
    /** 时间格式(HH:mm:ss) */
    String TIME_PATTERN = "HH:mm:ss";
    String yyyy_MM_dd_HH_mm="yyyy-MM-dd HH:mm";
    Long ONE_DAY = 24 * 60 * 60 * 1000L;
    String TIME_PATTERN_HOURS = "HH";
    String DAY_PATTERN = "dd";
    String YEAR_PATTERN = "yyyy";
    String MONTH_PATTERN = "MM";
    String YEAR_MONTH_PATTERN = "yyyy-MM";
    String LAST_TIME = " 23:59:59";
    String START_TIME = " 00:00:00";
    public static final Integer EXPRIED_TIME_LIMIT = 30;
    String AFTERNOON_WORK_TIME = " 13:00:00";
    String MORING_WORK_TIME = " 09:00:00";

    String MORING_WORK_END_TIME = " 12:00:00";

    String AFTERNOON_WORK_END_TIME = " 18:00:00";

    Long FOUR_WORK_TIME = 60 * 60 * 4 * 1000L ;

    Long A_WORK_TIME = 60 * 60 * 5 * 1000L ;
    Long HOUR = 60 * 60 * 1 * 1000L ;//一个小时

    Long ONE_DAY_WORK_TIME =  60 * 60 * 8 * 1000L;

    /**
     * 设每一台手机的平均生产时间为10分钟
     *///GENERATION
    Long GENERATION_TIME =  60 * 10 * 1000L;
}