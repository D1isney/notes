package com.wms.connect.plc;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;
import com.wms.connect.utils.PlcParam;
import com.wms.enums.PLCEnum;
import com.wms.exception.EException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;


@Slf4j
@Getter
@Setter
@Component
public class PlcConnect implements PlcConnectService {
    //  PLC参数
    private PlcParam plcParam;

    //  PLC Master
    private ModbusMaster master;

    private boolean connected = false;

    @Value("${plc.address}")
    private String plcAddress;
    @Value("${plc.keep-alive}")
    private Boolean keepAlive;

    /**
     * 打开PLC连接
     */
    @Override
    public void open() throws ModbusIOException, EException, IOException {
        plcParam = new PlcParam(plcAddress, keepAlive);
        setMaster(initMaster());
        if (!master.isConnected()) {
            master.connect();
            connected = true;
        }
    }

    /**
     * 关闭PLC连接
     */
    @Override
    public void close() throws ModbusIOException {
        if (!Objects.isNull(master) && master.isConnected()) {
            master.disconnect();
            connected = false;
        }
    }

    @Override
    public int readPlc(int location) {
        if (!Objects.isNull(master) && master.isConnected()) {
            try {
                int[] value = master.readHoldingRegisters(plcParam.getSlaveId(), location, PlcParam.size);
                return value[0];
            } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException e) {
                throw new RuntimeException(e);
            }
        }
        return 0;
    }

    /**
     * 根据需要读取的枚举值读取相应的PLC地址值
     *
     * @param plcEnum 枚举
     * @return 值
     */
    @Override
    public int readPlc(PLCEnum plcEnum) {
        if (!Objects.isNull(master) && master.isConnected()) {
            try {
                int[] value = master.readHoldingRegisters(plcParam.getSlaveId(), plcEnum.getAddress(), PlcParam.size);
                return value[0];
            } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException e) {
                throw new RuntimeException(e);
            }
        }
        return 0;
    }

    /**
     * 写入PLC
     *
     * @param location 地址
     * @param value    值
     */
    @Override
    public void writePlc(int location, int value) {
        if (!Objects.isNull(master) && master.isConnected()) {
            try {
                master.writeSingleRegister(plcParam.getSlaveId(), location, value);
            } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException e) {
                throw new RuntimeException(e);
            }
        }else{
            throw new EException("请先连接PLC！！！");
        }
    }


    /**
     * 写入PLC
     *
     * @param plcEnum 枚举值
     * @param value   值
     */
    @Override
    public void writePlc(PLCEnum plcEnum, int value) {
        if (!Objects.isNull(master) && master.isConnected()) {
            try {
                master.writeSingleRegister(plcParam.getSlaveId(), plcEnum.getAddress(), value);
            } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException e) {
                throw new RuntimeException(e);
            }
        }else{
            throw new EException("请先连接PLC！！！");
        }
    }


    /**
     * 初始化Master，拿到这个对象
     *
     * @return Master对象
     * @throws UnknownHostException 端口异常
     */
    public ModbusMaster initMaster() throws UnknownHostException {
        //  设置主机TCP参数
        TcpParameters tcpParameters = new TcpParameters();
        //  设置TCP的ip地址
        InetAddress address = InetAddress.getByName(plcParam.getIp());
        //  TCP参数设置IP地址
        tcpParameters.setHost(address);
        //  TCP设置长连接
        tcpParameters.setKeepAlive(plcParam.isKeepAlive());
        //  设置TCP端口
        tcpParameters.setPort(plcParam.getPort());
        // 创建一个主机
        ModbusMaster master = ModbusMasterFactory.createModbusMasterTCP(tcpParameters);
        Modbus.setAutoIncrementTransactionId(true);
        return master;
    }

}
