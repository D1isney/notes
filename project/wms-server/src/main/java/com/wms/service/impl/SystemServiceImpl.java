package com.wms.service.impl;

import com.wms.connect.plc.PlcConnectService;
import com.wms.connect.utils.PlcParam;
import com.wms.connect.websocket.WebSocketServerWeb;
import com.wms.constant.ConfigConstant;
import com.wms.constant.InOrOutConstant;
import com.wms.dto.PlcAddressDTO;
import com.wms.dto.WarehousingDTO;
import com.wms.enums.PLCEnum;
import com.wms.enums.TaskEnum;
import com.wms.enums.WebSocketEnum;
import com.wms.exception.EException;
import com.wms.pojo.Goods;
import com.wms.pojo.Inventory;
import com.wms.pojo.Task;
import com.wms.service.GoodsService;
import com.wms.service.InventoryService;
import com.wms.service.SystemService;
import com.wms.service.TaskService;
import com.wms.utils.FastJsonUtils;
import com.wms.utils.ObjectUtil;
import com.wms.utils.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
     *
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

    @Resource
    @Lazy
    private TaskService taskService;
    @Resource
    @Lazy
    private GoodsService goodsService;
    @Resource
    @Lazy
    private InventoryService inventoryService;

    /**
     * 是否执行挂起任务
     *
     * @param flag true执行，false不执行了
     * @param type 4入库，5出库
     */
    @Override
    public void executePendingTasks(boolean flag, Integer type) {
        if (flag) {
            //  继续操作
            if (type.equals(InOrOutConstant.in)) {
                //  入库操作
                int task1 = plcConnectService.readPlc(PLCEnum.PLC_TASK1_INT);
                int task2 = plcConnectService.readPlc(PLCEnum.PLC_TASK2_INT);
                String taskCode = String.valueOf(task1) + task2;
                Task task = taskService.queryTaskByCode(taskCode);
                if (StringUtil.isEmpty(task)) {
                    throw new EException("查询不到当前正在执行的任务！");
                }
                int good1 = plcConnectService.readPlc(PLCEnum.PLC_RFID_IN1);
                int good2 = plcConnectService.readPlc(PLCEnum.PLC_RFID_IN2);
                execute(good1, good2, task, type);
            } else {
                //  出库操作
                int task1 = plcConnectService.readPlc(PLCEnum.PLC_TASK1_OUT);
                int task2 = plcConnectService.readPlc(PLCEnum.PLC_TASK2_OUT);
                String taskCode = String.valueOf(task1) + task2;
                Task task = taskService.queryTaskByCode(taskCode);
                if (StringUtil.isEmpty(task)) {
                    throw new EException("查询不到当前正在执行的任务！");
                }
                int good1 = plcConnectService.readPlc(PLCEnum.PLC_RFID_OUT1);
                int good2 = plcConnectService.readPlc(PLCEnum.PLC_RFID_OUT2);
                execute(good1, good2, task, type);
            }
        } else {
            // 不操作了,直接清空plc所有状态手
            try {
                plcConnectService.restPLC();
            } catch (IOException e) {
                throw new EException(e.getMessage());
            }
        }
    }

    public void restPLCStatus(Integer type){
        if (Objects.equals(type, InOrOutConstant.in)) {
            plcConnectService.writePlc(PLCEnum.PLC_INT,PLCEnum.PLC_INT.getValue());
            plcConnectService.writePlc(PLCEnum.PLC_ACCOMPLISH_IN,PLCEnum.PLC_ACCOMPLISH_IN.getValue());
        }else if (Objects.equals(type, InOrOutConstant.out)){
            plcConnectService.writePlc(PLCEnum.PLC_OUT,PLCEnum.PLC_OUT.getValue());
            plcConnectService.writePlc(PLCEnum.PLC_ACCOMPLISH_OUT,PLCEnum.PLC_ACCOMPLISH_OUT.getValue());
        }
    }

    public void execute(int good1, int good2, Task task, Integer type) {
        //  去除久状态，重新下发
        restPLCStatus(type);
        String goodCode = good1 + String.valueOf(good2);
        Inventory inventory = inventoryService.queryById(task.getInventoryId());
        String inventoryCode = inventory.getCode();
        List<WarehousingDTO> list = new ArrayList<>();
        WarehousingDTO warehousingDTO = new WarehousingDTO();
        warehousingDTO.setGoodsCode(goodCode);
        warehousingDTO.setInventoryCode(inventoryCode);
        warehousingDTO.setTask(task);
        warehousingDTO.setType(type);
        list.add(warehousingDTO);
        //  下发
        inventoryService.warehousing(list);
    }

    @Resource
    @Lazy
    private PlcConnectService plcConnectService;

    /**
     * 是否还有任务需要再次执行
     */
    @Override
    public void rollbackTask() {
        int intPLC = plcConnectService.readPlc(PLCEnum.PLC_INT);
        int intPLCSuccess = plcConnectService.readPlc(PLCEnum.PLC_ACCOMPLISH_IN);
        //  说明任务还没完
        if (intPLC == 1 && intPLCSuccess == 1) {
            //  是否继续入库任务
            WebSocketServerWeb.send(WebSocketEnum.CONTINUE_WAREHOUSING_TASK);
        }
        int outPLC = plcConnectService.readPlc(PLCEnum.PLC_OUT);
        int outPLCSuccess = plcConnectService.readPlc(PLCEnum.PLC_ACCOMPLISH_OUT);
        if (outPLC == 1 && outPLCSuccess == 1) {
            // 是否需要继续出库
            WebSocketServerWeb.send(WebSocketEnum.CONTINUE_ISSUE_TASK);
        }
    }

    @Value("${plc.default-ip}")
    private String IP;
    @Value("${plc.default-port}")
    private int port;
    @Value("${plc.default-slaveId}")
    private int slaveId;

    public PlcAddressDTO checkPlcAddress(PlcAddressDTO plcAddressDTO) {
        if (Objects.isNull(plcAddressDTO.getIp())) {
            plcAddressDTO.setIp(IP);
        }
        if (Objects.isNull(plcAddressDTO.getPort())) {
            plcAddressDTO.setPort(port);
        }
        if (Objects.isNull(plcAddressDTO.getSlaveId())) {
            plcAddressDTO.setSlaveId(slaveId);
        }
        return plcAddressDTO;
    }
}
