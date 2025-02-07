package com.wms.service;

import com.wms.pojo.Storage;
import com.wms.service.base.BaseService;
import com.wms.utils.R;
import com.wms.vo.StorageVo;

public interface StorageService extends BaseService<Storage, StorageVo> {
    R<?> saveOrUpdateStorage(Storage storage);

    R<?> queryStorageAndInventory();
}
