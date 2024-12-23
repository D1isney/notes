package com.wms.connect.utils;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;
import com.wms.constant.ConfigConstant;
import com.wms.dto.AddressValueDTO;
import com.wms.dto.PlcAddressDTO;
import com.wms.utils.FastJsonUtils;
import com.wms.utils.ObjectUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * PLC ==========》 参数
 * 配置文件
 */
@Component
@Slf4j
@Getter
@Setter
public class PlcParam {

    //  配置文件
    @Value("${plc.address}")
    private String plcAddress;

    //  是否长连接
    @Value("${plc.keep-alive}")
    private boolean keepAlive;

    public static final int size = 1;

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


}
