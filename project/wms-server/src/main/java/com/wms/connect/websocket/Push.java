package com.wms.connect.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Push {
    private String type;
    private String message;
    private Object data;
    private Integer code;

    public Push(String type, String message, Object data) {
        this.type = type;
        this.message = message;
        this.data = data;
        this.code = 200;
    }
}
