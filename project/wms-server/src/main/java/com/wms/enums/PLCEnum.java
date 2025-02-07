package com.wms.enums;

import lombok.Getter;

@Getter
public enum PLCEnum {
    PLC_INT(0,0,"enterIn","入库信息位"),
    PLC_TASK1_INT(1,0,"taskIn1","任务入库信息位1"),
    PLC_TASK2_INT(2,0,"taskIn2","任务入库信息位2"),
    PLC_RFID_IN1(4,0,"rfidIn1","RFID入库信息位1"),
    PLC_RFID_IN2(5,0,"rfidIn1","RFID入库信息位2"),
    PLC_STORAGE_LEVEL_IN(7,0,"storageLevelIn","入库水平信息位"),
    PLC_STORAGE_VERTICAL_IN(8,0,"storageVerticalIn","入库垂直信息位置"),
    PLC_ACCOMPLISH_IN(22,0,"accomplishIn","入库完成标志位"),


    PLC_OUT(10,0,"enterOut","出库信息位"),
    PLC_TASK1_OUT(11,0,"taskIn1","任务出库信息位1"),
    PLC_TASK2_OUT(12,0,"taskIn2","任务出库信息位2"),
    PLC_RFID_OUT1(15,0,"rfidOut1","RFID出库信息位1"),
    PLC_RFID_OUT2(16,0,"rfidOut2","RFID出库信息位2"),
    PLC_STORAGE_LEVEL_OUT(18,0,"storageLevelOut","出库水平信息位"),
    PLC_STORAGE_VERTICAL_OUT(19,0,"storageVerticalOut","出库库垂直信息位置"),
    PLC_ACCOMPLISH_OUT(23,0,"accomplishOut","出库完成标志位"),

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
