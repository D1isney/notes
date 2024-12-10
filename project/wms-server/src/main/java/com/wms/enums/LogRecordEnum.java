package com.wms.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
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

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
