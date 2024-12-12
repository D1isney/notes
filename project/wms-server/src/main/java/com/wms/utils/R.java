package com.wms.utils;

import com.wms.enums.ResultEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class R <T> {
    private String message;
    private Integer code;
    private T data;


    public R(String message, Integer code) {
        this.message = message;
        this.code = code;
    }
    public R(ResultEnum resultEnum, T data) {
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
        this.data = data;
    }

    public R(ResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
    }

    public R(String message, Integer code, T data) {
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public static <T> R<T> ok() {
        return new R<T>("", 200, null);
    }
    public static <T> R<T> ok(String message) {
        return new R<T>(message, 200, null);
    }
    public static <T> R<T> ok(T data) {
        return new R<T>("", 200, data);
    }


    public static <T> R<T> ok(String message, T data) {
        return new R<T>(message, 200, data);
    }

    public static <T> R<T> error(String message) {
        return new R<T>(message, 500, null);
    }
    public static <T> R<T> error(String message, T data) {
        return new R<T>(message, 500, data);
    }


}
