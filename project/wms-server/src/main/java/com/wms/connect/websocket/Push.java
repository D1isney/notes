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
}
