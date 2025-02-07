package com.wms.dao;

import com.wms.pojo.Inventory;
import com.wms.service.base.IBaseMapper;
import com.wms.vo.InventoryVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InventoryDao extends IBaseMapper<Inventory, InventoryVo> {
    String lastCode();
}
