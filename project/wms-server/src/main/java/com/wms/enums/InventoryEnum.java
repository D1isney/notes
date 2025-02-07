package com.wms.enums;

import lombok.Getter;

@Getter
public enum InventoryEnum {
    EMPTY(0,"无货物"),
    COMING_IN(1,"正在入库"),
    STORAGE_COMPLETED(2,"入库完成"),
    LEAVING_THE_WAREHOUSE(3,"正在出库"),
    ISSUE_COMPLETED(4,"出库完成"),
    HAVE(5,"有货物")
    ;
    private final Integer type;
    private final String message;

    InventoryEnum(Integer type, String message) {
        this.type = type;
        this.message = message;
    }
}
