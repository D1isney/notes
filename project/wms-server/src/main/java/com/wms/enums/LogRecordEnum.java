package com.wms.enums;

public enum LogRecordEnum{
    NORMAL_LOG("普通日志",0),
    WARNING_LOG("警告日志",1),
    DANGER_LOG("危险日志",2),
    ALARM_LOG("报警日志",3),
    ;

    private String message;
    private Integer code;

    LogRecordEnum(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
