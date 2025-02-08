package com.wms.enums;

import lombok.Getter;

@Getter
public enum WebSocketEnum {
    LOG("更新日志", null, "log",200),
    TASK("更新任务", null, "task",200),
    PLC_CONNECT_ERROR("请先连接PLC！！！", null, "PlcConnectError",200),
    TASK_MESSAGE_ISSUED("任务发下", "success", "TaskMessageIssued",200),
    ;

    private final String message;
    private final Object data;
    private final String type;
    private final Integer code;

    WebSocketEnum(String message, Object data, String type,Integer code) {
        this.message = message;
        this.data = data;
        this.type = type;
        this.code = code;
    }
}
