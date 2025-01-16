package com.wms.service.impl;

import com.wms.connect.utils.PlcParam;
import com.wms.constant.ConfigConstant;
import com.wms.dto.PlcAddressDTO;
import com.wms.exception.EException;
import com.wms.service.SystemService;
import com.wms.utils.FastJsonUtils;
import com.wms.utils.ObjectUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
public class SystemServiceImpl implements SystemService {

    @Value("${plc.address}")
    private String plcAddress;
    @Value("${plc.keep-alive}")
    private Boolean keepAlive;

    @Override
    public PlcAddressDTO getPlcSetting() {
        PlcParam plcParam = null;
        try {
            plcParam = new PlcParam(plcAddress, keepAlive);
            return plcParam.getPlcAddressDTO();
        } catch (IOException e) {
            throw new EException(e.getMessage());
        }
    }

    /**
     * 保存新的配置文件
     * @param plcAddressDTO dto
     */
    @Override
    public void saveOrUpdatePlcAddress(PlcAddressDTO plcAddressDTO) {
        String PLC_ADDRESS = ConfigConstant.CONF_BASE_MODBUS + plcAddress;
        plcAddressDTO = checkPlcAddress(plcAddressDTO);
        String jsonNoFeatures = FastJsonUtils.toJSONNoFeatures(plcAddressDTO);
        try {
            ObjectUtil.writerString2File(jsonNoFeatures, PLC_ADDRESS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Value("${plc.default-ip}")
    private String IP;
    @Value("${plc.default-port}")
    private int port;
    @Value("${plc.default-slaveId}")
    private int slaveId;
    public PlcAddressDTO checkPlcAddress(PlcAddressDTO plcAddressDTO) {
        if (Objects.isNull(plcAddressDTO.getIp())){
            plcAddressDTO.setIp(IP);
        }
        if (Objects.isNull(plcAddressDTO.getPort())){
            plcAddressDTO.setPort(port);
        }
        if (Objects.isNull(plcAddressDTO.getSlaveId())){
            plcAddressDTO.setSlaveId(slaveId);
        }
        return plcAddressDTO;
    }
}
