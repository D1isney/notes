package com.wms.service;

import com.wms.dto.PlcAddressDTO;

public interface SystemService {

    PlcAddressDTO getPlcSetting();

    void saveOrUpdatePlcAddress(PlcAddressDTO plcAddressDTO);
}
