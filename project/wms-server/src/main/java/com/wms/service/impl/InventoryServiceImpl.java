package com.wms.service.impl;

import com.wms.connect.plc.PlcConnect;
import com.wms.connect.utils.PlcParam;
import com.wms.connect.websocket.WebSocketServerWeb;
import com.wms.constant.InOrOutConstant;
import com.wms.dao.InventoryDao;
import com.wms.dto.AddressValueDTO;
import com.wms.dto.PlcAddressDTO;
import com.wms.dto.StorageAndInventory;
import com.wms.dto.WarehousingDTO;
import com.wms.enums.InventoryEnum;
import com.wms.enums.TaskEnum;
import com.wms.enums.WebSocketEnum;
import com.wms.exception.EException;
import com.wms.pojo.Goods;
import com.wms.pojo.Inventory;
import com.wms.pojo.Storage;
import com.wms.pojo.Task;
import com.wms.service.*;
import com.wms.service.base.IBaseServiceImpl;
import com.wms.task.TaskExecutor;
import com.wms.task.intTask.InTaskExecutor;
import com.wms.task.outTask.OutTaskExecutor;
import com.wms.thread.MemberThreadLocal;
import com.wms.utils.CodeUtils;
import com.wms.utils.R;
import com.wms.utils.StringUtil;
import com.wms.vo.InventoryVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class InventoryServiceImpl extends IBaseServiceImpl<InventoryDao, Inventory, InventoryVo> implements InventoryService {

    @Resource
    @Lazy
    private InventoryDao inventoryDao;

    @Resource
//    @Lazy
    private PlcConnect plcConnect;

    @Resource
    @Lazy
    private GoodsService goodsService;
    @Resource
    @Lazy
    private InventoryService inventoryService;
    @Resource
    @Lazy
    private StorageService storageService;
    @Resource
    @Lazy
    private TaskService taskService;
    @Resource
    @Lazy
    private LogRecordService logRecordService;
    @Value("${plc.sleepTime}")
    private int sleepTime;

    @Override
    public String lastCode() {
        return CodeUtils.getString(inventoryDao.lastCode());
    }


    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 手动入库操作
     *
     * @param warehousingDTO 入库信息
     * @return R
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> warehousing(List<WarehousingDTO> warehousingDTO) {
        return operatingDuty(warehousingDTO);
    }

    @Override
    public R<?> operatingDuty(List<WarehousingDTO> warehousingDTO) {
        /*
            1、物料编码
            2、库位编码（有、无）
            3、库存编码（有、无）
         */
        if (!StringUtil.isEmpty(warehousingDTO)) {
            warehousingDTO.forEach(w -> {
                StorageAndInventory storageAndInventory = getStorageByCode(w.getStorageCode());
                Storage storageByCode = storageAndInventory.getStorage();
                Inventory inventoryByCode = getInventoryByCode(w.getInventoryCode(), w.getType(), storageAndInventory);
                if (w.getType().equals(InOrOutConstant.in)) {
                    Goods goodsByCode = getGoodsByCode(w.getGoodsCode());
                    in(goodsByCode, inventoryByCode, storageByCode, w.getTask());
                } else {
                    Goods goodsById = getGoodsById(w.getGoodsId());
                    out(goodsById, inventoryByCode, storageByCode, w.getTask());
                }
            });
            return R.ok("正在下发任务！！！");
        } else {
            return R.error("无效入库信息！！！");
        }
    }

    /**
     * 智能盘库
     */
    @Override
    public void intelligentDiskLibrary() {
        //  修改PLC初始化状态
        try {
            restPLC();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Inventory> inventories = queryAll();
        inventories.forEach(inventory -> {
            inventory.setGoodsId(0L);
            inventory.setStatus(InventoryEnum.EMPTY.getType());
            inventory.setUpdateTime(new Date());
            inventory.setUpdateMember(MemberThreadLocal.get().getMember().getId());
        });
        saveOrUpdateBatch(inventories);
    }

    @Value("${plc.address}")
    private String plcAddress;
    @Value("${plc.keep-alive}")
    private Boolean keepAlive;

    /**
     * 初始化PLC的所有状态
     */
    public void restPLC() throws IOException {
        PlcParam plcParam = new PlcParam(plcAddress, keepAlive);
        PlcAddressDTO plcAddressDTO = plcParam.getPlcAddressDTO();
        List<AddressValueDTO> pointList = plcAddressDTO.getPointList();
        pointList.forEach(point -> {
            plcConnect.writePlc(point.getAddress(), point.getValue());
        });
    }

    private void in(Goods goods, Inventory inventory, Storage storage, Task task) {
        if (!inventory.getStatus().equals(InventoryEnum.EMPTY.getType())) {
            throw new EException("该库位" + storage.getRow() + "-" + inventory.getLayer() + ",已有物料，无法再次入库！！！");
        }
        TaskExecutor taskExecutor = null;
        taskExecutor = new InTaskExecutor();
        TaskExecutorInit(taskExecutor, goods, inventory, task);

        taskExecutor.setExceptionHandler(e -> {
            throw new EException(e.getMessage());
        });
        threadPoolExecutor.execute(taskExecutor);
    }

    private void out(Goods goods, Inventory inventory, Storage storage, Task task) {
        if (!inventory.getStatus().equals(InventoryEnum.HAVE.getType())) {
            throw new EException("该库位" + storage.getRow() + "-" + inventory.getLayer() + "，没有物料或物料正在出入库，无法再次出库！！！");
        }
        TaskExecutor taskExecutor = null;
        taskExecutor = new OutTaskExecutor();
        TaskExecutorInit(taskExecutor, goods, inventory, task);
        taskExecutor.setExceptionHandler(e -> {
            throw new EException(e.getMessage());
        });
        threadPoolExecutor.execute(taskExecutor);
    }

    public Goods getGoodsByCode(String code) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        List<Goods> goods = goodsService.queryList(map);
        if (goods.isEmpty()) {
            throw new EException("不存在物料：" + code);
        } else {
            return goods.get(0);
        }
    }

    public Goods getGoodsById(Long id) {
        Goods goods = goodsService.queryById(id);
        if (StringUtil.isEmpty(goods)) {
            throw new EException("不存在物料：" + id);
        } else {
            return goods;
        }
    }

    public Inventory getInventoryByCode(String code, Integer type, StorageAndInventory storage) {
        Map<String, Object> map = new HashMap<>();
        if (type.equals(InOrOutConstant.in)) {
            List<Inventory> inventories;
            if (StringUtil.isEmpty(code)) {
                if (StringUtil.isEmpty(storage) || StringUtil.isEmpty(storage.getStorage())){
                    throw new EException("没有合适的库位！！！");
                }
                //  随便生成一个
                map.put("status", InventoryEnum.EMPTY.getType());
                map.put("storageId", storage.getStorage().getId());
                inventories = inventoryService.queryList(map);
                if (inventories.isEmpty()) {
                    throw new EException("没有合适的库位！！！");
                }
                return inventories.get(inventories.size() - 1);
            } else {
                map = new HashMap<>();
                map.put("code", code);
                inventories = inventoryService.queryList(map);
                if (!inventories.isEmpty()) {
                    if (!inventories.get(0).getStatus().equals(InventoryEnum.EMPTY.getType())) {
                        throw new EException("该库位已存在物料，无法再次入库！！！");
                    } else {
                        return inventories.get(0);
                    }
                } else {
                    throw new EException("该库位已存在物料，无法再次入库！！！");
                }
            }
        } else {
            //出库
            List<Inventory> inventories;
            if (StringUtil.isEmpty(code)) {
                throw new EException("库存编码不能为空！");
            }
            map = new HashMap<>();
            map.put("code", code);
            inventories = inventoryService.queryList(map);
            if (inventories.isEmpty()) {
                throw new EException("不存在该库存！！！");
            } else {
                return inventories.get(0);
            }
        }
    }

    public StorageAndInventory getStorageByCode(String code) {
        if (StringUtil.isEmpty(code)) {
            Map<String, Object> mapStorage = new HashMap<>();
            mapStorage.put("isForbidden", 1);
            List<Storage> storages = storageService.queryList(mapStorage);
            if (storages.isEmpty()) {
                throw new EException("没有合适的库位！！！");
            }
            return getStorage(storages);
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("code", code);
            List<Storage> storages = storageService.queryList(map);
            if (storages.isEmpty()) {
                throw new EException("不存在库位：" + code);
            } else {
                StorageAndInventory storageAndInventory = new StorageAndInventory();
                storageAndInventory.setStorage(storages.get(0));
                return storageAndInventory;
            }
        }
    }

    public StorageAndInventory getStorage(List<Storage> storages) {
        Map<String, Object> map = new HashMap<>();
        StorageAndInventory storageAndInventory = new StorageAndInventory();
        AtomicBoolean flag = new AtomicBoolean(false);
        if (!storages.isEmpty()) {
            for (Storage s : storages) {
                map.put("status", InventoryEnum.EMPTY.getType());
                map.put("storageId", s.getId());
                List<Inventory> inventories = inventoryService.queryList(map);
                inventories.forEach(i -> {
                    if (i.getStatus().equals(InventoryEnum.EMPTY.getType())) {
                        flag.set(true);
                        storageAndInventory.setInventory(i);
                    }
                });
                if (flag.get()) {
                    storageAndInventory.setStorage(s);
                    break;
                }
            }
        } else {
            throw new EException("没有合适的库位！！！");
        }
        return storageAndInventory;
    }

    public Task createTask(Goods goods, Inventory inventory) {
        Task task = new Task();
        task.setGoodsId(goods.getId());
        task.setInventoryId(inventory.getId());
        task.setCode(taskService.lastCode());
        task.setStatus(TaskEnum.INIT_IN.getStatus());
        task.setType(TaskEnum.INIT_IN.getType());
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());
        task.setCreateMember(MemberThreadLocal.get().getMember().getId());
        task.setUpdateMember(MemberThreadLocal.get().getMember().getId());
        task.setRemark("任务创建成功");
        task = taskService.saveOrModify(task);
        return task;
    }

    public void TaskExecutorInit(TaskExecutor taskExecutor, Goods goods, Inventory inventory, Task task) {
        if (StringUtil.isEmpty(task)) {
            task = createTask(goods, inventory);
        }
        taskExecutor.setTask(task);
        taskExecutor.setInventory(inventory);
        taskExecutor.setPlcConnect(plcConnect);
        taskExecutor.setTaskService(taskService);
        taskExecutor.setInventoryService(inventoryService);
        taskExecutor.setStorageService(storageService);
        taskExecutor.setGoodsService(goodsService);
        taskExecutor.setLogRecordService(logRecordService);
        taskExecutor.setSleepTime(sleepTime);
        taskExecutor.setMemberThreadLocal(MemberThreadLocal.get());
    }

}
