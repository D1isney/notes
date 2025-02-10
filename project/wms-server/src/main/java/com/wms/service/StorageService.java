package com.wms.service;

import com.wms.pojo.Storage;
import com.wms.service.base.BaseService;
import com.wms.utils.R;
import com.wms.vo.StorageVo;

import java.util.List;

public interface StorageService extends BaseService<Storage, StorageVo> {
    R<?> saveOrUpdateStorage(List<Storage> storage);

    R<?> queryStorageAndInventory();

    R<?> queryInventoryNum();
}
