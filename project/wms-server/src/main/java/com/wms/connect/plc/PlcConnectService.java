package com.wms.connect.plc;

import com.wms.enums.PLCEnum;

public interface PlcConnectService {

    void open();

    void close();

    int readPlc(int location);

    int readPlc(PLCEnum plcEnum);

    void writePlc(int location,int value);

    void writePlc(PLCEnum plcEnum,int value);

}
