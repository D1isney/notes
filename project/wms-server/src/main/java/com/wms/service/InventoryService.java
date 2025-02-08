package com.wms.service;

import com.wms.dto.WarehousingDTO;
import com.wms.pojo.Inventory;
import com.wms.service.base.BaseService;
import com.wms.utils.R;
import com.wms.vo.InventoryVo;

import java.util.List;

public interface InventoryService extends BaseService<Inventory, InventoryVo> {
    String lastCode();

    R<?> warehousing(WarehousingDTO warehousingDTO);

    void intelligentDiskLibrary();
}
