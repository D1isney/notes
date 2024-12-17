package com.wms.connect.plc;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;
import com.wms.constant.ConfigConstant;
import com.wms.dto.AddressValueDTO;
import com.wms.dto.PlcAddressDTO;
import com.wms.exception.EException;
import com.wms.utils.FastJsonUtils;
import com.wms.utils.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class PlcConnect {

    @Value("${plc.address}")
    private String plcAddress;
    @Value("${plc.keep-alive}")
    private boolean keepAlive;

    public String PLC_ADDRESS = "";

    private List<AddressValueDTO> list = new ArrayList<>();
    private String ip;
    private Integer port;
    private Integer slaveId;

    public void setPlcAddress(String plcAddress) {
        this.PLC_ADDRESS = ConfigConstant.CONF_BASE_MODBUS + plcAddress;
    }

    @PostConstruct
    public void init() throws IOException {
        setPlcAddress(plcAddress);
        String read = ObjectUtil.readString(PLC_ADDRESS);
        PlcAddressDTO plcAddressDTO = FastJsonUtils.toBean(read, PlcAddressDTO.class);
        ip = plcAddressDTO.getIp();
        port = plcAddressDTO.getPort();
        slaveId = plcAddressDTO.getSlaveId();
        list = plcAddressDTO.getPointList();
    }

    public ModbusMaster getMaster() throws UnknownHostException {
        //  设置主机TCP参数
        TcpParameters tcpParameters = new TcpParameters();

        //  设置TCP的ip地址
        InetAddress address = InetAddress.getByName(ip);

        //  TCP参数设置IP地址
        tcpParameters.setHost(address);

        //  TCP设置长连接
        tcpParameters.setKeepAlive(keepAlive);
        //  设置TCP端口
        tcpParameters.setPort(port);
        // 创建一个主机
        ModbusMaster master = ModbusMasterFactory.createModbusMasterTCP(tcpParameters);
        Modbus.setAutoIncrementTransactionId(true);
//        List<AddressValueDTO> pointList = plcAddressDTO.getPointList();
        return master;
    }

    public void runConnect(ModbusMaster master) {
        if (master != null) {
            try {
                master.connect();
//                log.info("用户：{}，主动连接PLC：{}:{}", MemberThreadLocal.get().getMember().getUsername(), ip, port);
            } catch (ModbusIOException e) {
                throw new EException("PLC连接失败：" + e.getMessage());
            }
        }
    }

    public void stopConnect(ModbusMaster master) {
        if (master != null) {
            try {
                master.disconnect();
//                log.info("用户：{}，主动关闭PLC：{}:{}", MemberThreadLocal.get().getMember().getUsername(), ip, port);
            } catch (ModbusIOException e) {
                throw new EException("PLC取消连接失败：" + e.getMessage());
            }
        }
    }

    public void check() {
        try {
            ModbusMaster master = getMaster();
            //  开启连接
            runConnect(master);
            //  拿值
            getAllPlcValue(list, master);
            //  关闭PLC连接
            stopConnect(master);
        } catch (Exception e) {
            throw new EException(e.getMessage());
        }
    }

    public void checkByName(String name) {
        try {
            ModbusMaster master = getMaster();
            //  开启连接
            runConnect(master);
            //  拿值
            getAllPlcValueByName(list, master, name);
            //  关闭PLC连接
            stopConnect(master);
        } catch (Exception e) {
            throw new EException(e.getMessage());
        }
    }

    public void getAllPlcValue(List<AddressValueDTO> list, ModbusMaster master) throws ModbusIOException {
        if (!master.isConnected()) {
            throw new EException("PLC未连接！");
        }
        list.forEach(add -> {
            Integer address = add.getAddress();
            String name = add.getName();
            read(name, master, address);
        });
    }

    public void getAllPlcValueByName(List<AddressValueDTO> list, ModbusMaster master, String name) throws ModbusIOException {
        if (!master.isConnected()) {
            throw new EException("PLC未连接！");
        }
        list.forEach(add -> {
            Integer address = add.getAddress();
            String addName = add.getName();
            if (name.equals(addName)) {
                read(addName, master, address);
            }
        });
    }

    private void read(String name, ModbusMaster master, Integer address) {
        try {
            //  只读一位
            int[] registerValues = master.readInputRegisters(slaveId, address, 1);
            for (int registerValue : registerValues) {
                System.out.println(name + ":" + address++ + ", Values:" + registerValue);
            }
        } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException e) {
            throw new RuntimeException(e);
        }
    }


}
