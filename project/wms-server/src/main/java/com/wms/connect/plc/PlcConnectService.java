package com.wms.connect.plc;

import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.wms.enums.PLCEnum;

import java.io.IOException;
import java.net.UnknownHostException;

public interface PlcConnectService {

    void open() throws ModbusIOException, IOException;

    void close() throws ModbusIOException;

    int readPlc(int location);

    int readPlc(PLCEnum plcEnum);

    void writePlc(int location, int value);

    void writePlc(PLCEnum plcEnum, int value);

}
