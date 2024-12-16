package com.wms.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
    Result_OK("请求成功"),
    RESULT_OK_FOR_MESSAGE("请求成功", 200),
    RESULT_ERROR("请求失败", 500),
    ;
    private final String message;
    private final Integer code;


    ResultEnum(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    ResultEnum(String message) {
        this.message = message;
        this.code = 200;
    }


}
