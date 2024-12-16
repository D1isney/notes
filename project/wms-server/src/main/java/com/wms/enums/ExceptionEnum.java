package com.wms.enums;


import lombok.Getter;

@Getter
public class ExceptionEnum {

    ;
    private final String message;
    private final Integer code;

    public ExceptionEnum(String message, Integer code) {
        this.message = message;
        this.code = code;
    }
}
