package com.wms.service.impl;

import com.wms.dao.StorageDao;
import com.wms.pojo.Inventory;
import com.wms.pojo.Storage;
import com.wms.service.InventoryService;
import com.wms.service.StorageService;
import com.wms.service.base.IBaseServiceImpl;
import com.wms.utils.R;
import com.wms.utils.StringUtil;
import com.wms.vo.StorageVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StorageServiceImpl extends IBaseServiceImpl<StorageDao, Storage, StorageVo> implements StorageService {

    @Resource
    private InventoryService inventoryService;

    @Override
    public R<?> saveOrUpdateStorage(Storage storage) {
        return null;
    }

    /**
     * 通过查询库位信息再查询库存信息直接返回给前端
     *
     * @return R
     */
    @Override
    public R<?> queryStorageAndInventory() {
        List<Storage> storages = queryAll();
        if (!StringUtil.isEmpty(storages) && !storages.isEmpty()) {
            Map<String, Object> map = new HashMap<>();
            storages.forEach(storage -> {
                map.put("storageId", storage.getId());
                List<Inventory> inventories = inventoryService.queryList(map);
                storage.setInventoryList(inventories);
            });
        }
        return R.ok(storages);
    }
}
