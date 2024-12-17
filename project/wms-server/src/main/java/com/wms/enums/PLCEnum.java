package com.wms.enums;

import lombok.Getter;

@Getter
public enum PLCEnum {
    PLC_INT(0,0,"enterIn","入库信息位"),
    PLC_RFID_IN(4,0,"rfidIn","RFID入库信息位"),
    PLC_STORAGE_LEVEL_IN(7,0,"storageLevelIn","入库水平信息位"),
    PLC_STORAGE_VERTICAL_IN(8,0,"storageVerticalIn","入库垂直信息位置"),


    PLC_OUT(10,0,"enterOut","出库信息位"),
    PLC_RFID_OUT(15,0,"rfidOut","RFID入库信息位"),
    PLC_STORAGE_LEVEL_OUT(18,0,"storageLevelOut","出库水平信息位"),
    PLC_STORAGE_VERTICAL_OUT(19,0,"storageVerticalOut","出库库垂直信息位置"),

    ;
    private final Integer address;
    private final Integer value;
    private final String name;
    private final String message;

    PLCEnum(Integer address, Integer value, String name, String message) {
        this.address = address;
        this.value = value;
        this.name = name;
        this.message = message;
    }
}
