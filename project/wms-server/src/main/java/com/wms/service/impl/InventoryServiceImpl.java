package com.wms.service.impl;

import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.wms.connect.plc.PlcConnect;
import com.wms.dao.InventoryDao;
import com.wms.dto.WarehousingDTO;
import com.wms.pojo.Inventory;
import com.wms.service.InventoryService;
import com.wms.service.base.IBaseServiceImpl;
import com.wms.utils.CodeUtils;
import com.wms.utils.R;
import com.wms.utils.StringUtil;
import com.wms.vo.InventoryVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class InventoryServiceImpl extends IBaseServiceImpl<InventoryDao, Inventory, InventoryVo> implements InventoryService {

    @Resource
    private InventoryDao inventoryDao;

    @Resource
    private PlcConnect plcConnect;

    @Override
    public String lastCode() {
        return CodeUtils.getString(inventoryDao.lastCode());
    }

    /**
     * 手动入库操作
     * @param warehousingDTO 入库信息
     * @return R
     */
    @Override
    public R<?> warehousing(List<WarehousingDTO> warehousingDTO) {
        /*
            1、物料编码
            2、库位编码（有、无）
            3、库存编码（有、无）
         */
        if(!StringUtil.isEmpty(warehousingDTO)){


            return R.ok("入库任务下发！");
        }else{
            return R.error("无效入库信息！！！");
        }
    }
}
