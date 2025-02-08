package com.wms.enums;

import lombok.Getter;

@Getter
public enum TaskEnum {
    INIT_IN(0,4,"初始化状态"),
    ONGOING_IN(1,4,"进行状态"),
    PENDING_IN(2,4,"挂起状态"),
    ACCOMPLISH_IN(3,4,"完成"),
    INIT_OUT(0,5,"初始化状态"),
    ONGOING_OUT(1,5,"进行状态"),
    PENDING_OUT(2,5,"挂起状态"),
    ACCOMPLISH_OUT(3,5,"完成"),
    ;
    private final Integer status;
    private final Integer type;
    private final String message;

    TaskEnum(Integer status,Integer type , String message) {
        this.type = type;
        this.status = status;
        this.message = message;
    }
}
