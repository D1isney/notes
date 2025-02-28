package com.wms.service;

import com.wms.dto.PlcAddressDTO;

import java.io.IOException;

public interface SystemService {

    PlcAddressDTO getPlcSetting();

    void saveOrUpdatePlcAddress(PlcAddressDTO plcAddressDTO);

    //  是否继续执行
    void executePendingTasks(boolean flag,Integer type);

    //  是否还有任务在执行
    void rollbackTask();
}
