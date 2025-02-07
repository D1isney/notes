package com.wms.enums;

import lombok.Getter;

@Getter
public enum TaskEnum {
    INIT_IN(0,0,"初始化状态"),
    ONGOING_IN(1,0,"进行状态"),
    PENDING_IN(2,0,"挂起状态"),
    ACCOMPLISH_IN(3,0,"完成"),
    INIT_OUT(0,1,"初始化状态"),
    ONGOING_OUT(1,1,"进行状态"),
    PENDING_OUT(2,1,"挂起状态"),
    ACCOMPLISH_OUT(3,1,"完成"),
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
