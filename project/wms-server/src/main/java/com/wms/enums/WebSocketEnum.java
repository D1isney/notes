package com.wms.enums;

import lombok.Getter;

@Getter
public enum WebSocketEnum {
    LOG("更新日志", null, "log"),
    TASK("更新任务", null, "task");

    private final String message;
    private final Object data;
    private final String type;

    WebSocketEnum(String message, Object data, String type) {
        this.message = message;
        this.data = data;
        this.type = type;
    }
}
