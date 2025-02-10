package com.wms.service.impl;

import com.wms.dao.StorageDao;
import com.wms.dto.InventoryNum;
import com.wms.enums.InventoryEnum;
import com.wms.pojo.Goods;
import com.wms.pojo.Inventory;
import com.wms.pojo.Storage;
import com.wms.service.GoodsService;
import com.wms.service.InventoryService;
import com.wms.service.StorageService;
import com.wms.service.base.IBaseServiceImpl;
import com.wms.utils.R;
import com.wms.utils.StringUtil;
import com.wms.vo.StorageVo;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StorageServiceImpl extends IBaseServiceImpl<StorageDao, Storage, StorageVo> implements StorageService {

    @Resource
    @Lazy
    private InventoryService inventoryService;

    @Override
    public R<?> saveOrUpdateStorage(List<Storage> storage) {
        boolean b = saveOrUpdateBatch(storage);
//        saveOrModify(storage);
        return R.ok("保存成功！",b);
    }

    @Resource
    private GoodsService goodsService;

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
                inventories.forEach(inventory -> {
                    Goods goods = goodsService.queryById(inventory.getGoodsId());
                    inventory.setGoods(goods);
                });
                storage.setInventoryList(inventories);
            });
        }

        return R.ok(storages);
    }

    @Override
    public R<?> queryInventoryNum() {
        List<InventoryNum> list = new ArrayList<>();
        List<Storage> storages = queryAll();
        if (!StringUtil.isEmpty(storages) && !storages.isEmpty()) {
            Map<String, Object> map = new HashMap<>();
            storages.forEach(storage -> {
                map.put("storageId", storage.getId());
                List<Inventory> inventories = inventoryService.queryList(map);
                List<Inventory> emptyNum = inventories.stream()
                        .filter(inventory -> inventory.getStatus().equals(InventoryEnum.EMPTY.getType()))
                        .collect(Collectors.toList());
                List<Inventory> usedNum = inventories.stream()
                        .filter(inventory -> !inventory.getStatus().equals(InventoryEnum.EMPTY.getType()))
                        .collect(Collectors.toList());

                InventoryNum inventoryNum = new InventoryNum();
                inventoryNum.setStorageName(storage.getName());
                inventoryNum.setInventoryNum(inventories.size());
                inventoryNum.setInventoryUsed(usedNum.size());
                inventoryNum.setInventoryResidue(emptyNum.size());
                list.add(inventoryNum);
            });
        }
        return R.ok(list);
    }
}
