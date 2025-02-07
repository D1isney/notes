package com.wms.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum LogRecordEnum{
    NORMAL_LOG("普通日志",0),
    WARNING_LOG("警告日志",1),
    DANGER_LOG("危险日志",2),
    ALARM_LOG("报警日志",3),
    INT_LOG("入库日志",4),
    OUT_LOG("出库日志",5),
    ;
    private final String message;
    private final Integer code;

    LogRecordEnum(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

}
