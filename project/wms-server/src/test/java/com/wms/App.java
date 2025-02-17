package com.wms;

import java.net.InetAddress;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;
import com.wms.exception.EException;


/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {
//        plcTest();
        for (Integer i : splitCode("25020701")) {
            System.out.println(i);
        }
    }




    public static Integer[] splitCode(String code) {
        int split = code.length() / 2;

        int start = Integer.parseInt(code.substring(0, split + 1));
        int end = Integer.parseInt(code.substring(split + 1));
        return new Integer[]{start, end};
    }

    public static void plcTest() {
        try {
            // 设置主机TCP参数
            TcpParameters tcpParameters = new TcpParameters();

            // 设置TCP的ip地址
            InetAddress adress = InetAddress.getByName("127.0.0.1");

            // TCP参数设置ip地址
            // tcpParameters.setHost(InetAddress.getLocalHost());
            tcpParameters.setHost(adress);

            // TCP设置长连接
            tcpParameters.setKeepAlive(true);
            // TCP设置端口，这里设置是默认端口502
            tcpParameters.setPort(Modbus.TCP_PORT);

            // 创建一个主机
            ModbusMaster master = ModbusMasterFactory.createModbusMasterTCP(tcpParameters);
            Modbus.setAutoIncrementTransactionId(true);

            int slaveId = 1;//从机地址
            int offset = 0;//寄存器读取开始地址
            int quantity = 10;//读取的寄存器数量


            try {
                if (!master.isConnected()) {
                    master.connect();// 开启连接
                }

//                master.writeSingleRegister(slaveId, offset, 24);
//                master.writeSingleRegister(slaveId, offset + 1, 24122);

                int value1 = 25020601;

                short highWord = (short) ((value1 >> 16) & 0xFFFF); // 高位部分
                short lowWord = (short) (value1 & 0xFFFF); // 低位部分

                master.writeMultipleRegisters(slaveId, 0, new int[]{highWord, lowWord});


                // 读取对应从机的数据，readInputRegisters读取的写寄存器，功能码04
                int[] registerValues = master.readHoldingRegisters(slaveId, offset, quantity);

                // 控制台输出
                for (int value : registerValues) {
                    System.out.println("Address: " + offset++ + ", Value: " + value);
                }

//				master.writeSingleRegister();
//                master.writeSingleRegister(slaveId, offset, 1);

            } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException e) {
                e.printStackTrace();
            } finally {
                try {
                    master.disconnect();
                } catch (ModbusIOException e) {
                    e.printStackTrace();
                }
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

