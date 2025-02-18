package com.wms.service.impl;

import com.wms.connect.plc.PlcConnect;
import com.wms.connect.utils.PlcParam;
import com.wms.constant.InOrOutConstant;
import com.wms.dao.InventoryDao;
import com.wms.dto.*;
import com.wms.enums.InventoryEnum;
import com.wms.enums.TaskEnum;
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
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl extends IBaseServiceImpl<InventoryDao, Inventory, InventoryVo> implements InventoryService {

    @Resource
    @Lazy
    private InventoryDao inventoryDao;
    @Resource
    @Lazy
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

    /**
     * 出入库操作
     *
     * @param warehousingDTO 操作DTO
     * @return 结果
     */
    @Override
    public R<?> operatingDuty(List<WarehousingDTO> warehousingDTO) {
        /*
            1、物料编码
            2、库位编码（有、无）
            3、库存编码（有、无）
            4、任务（有、无）
         */
        if (!StringUtil.isEmpty(warehousingDTO)) {
            warehousingDTO.forEach(w -> {
                StorageAndInventoryDTO storageAndInventoryDTO = getStorageByCode(w.getStorageCode());
                Storage storageByCode = storageAndInventoryDTO.getStorage();
                Inventory inventoryByCode = getInventoryByCode(w.getInventoryCode(), w.getType(), storageAndInventoryDTO);
                if (w.getType().equals(InOrOutConstant.in)) {
                    Goods goodsByCode = goodsService.getGoodsByCode(w.getGoodsCode());
                    in(goodsByCode, inventoryByCode, storageByCode, w.getTask());
                } else {
                    Goods goodsById;
                    if (!StringUtil.isEmpty(w.getGoodsCode())) {
                        goodsById = goodsService.getGoodsByCode(w.getGoodsCode());
                    } else {
                        goodsById = goodsService.getGoodsById(w.getGoodsId());
                    }
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

    /**
     * 入库操作
     *
     * @param goods     物料
     * @param inventory 库存
     * @param storage   库位
     * @param task      任务
     */
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

    /**
     * 出库操作
     *
     * @param goods     出库物料
     * @param inventory 库存
     * @param storage   库位
     * @param task      任务
     */
    private void out(Goods goods, Inventory inventory, Storage storage, Task task) {
        //  挂起的库存或者是有货物的库存才能出库
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

    /**
     * 通过库存Code找库位，没有就拿库位Code找库存，再没有就随机生成
     *
     * @param code    库位Code
     * @param type    出入库类型
     * @param storage 库位
     * @return 库存
     */
    @Override
    public Inventory getInventoryByCode(String code, Integer type, StorageAndInventoryDTO storage) {
        Map<String, Object> map = new HashMap<>();
        if (type.equals(InOrOutConstant.in)) {
            List<Inventory> inventories;
            if (StringUtil.isEmpty(code)) {
                if (StringUtil.isEmpty(storage) || StringUtil.isEmpty(storage.getStorage())) {
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

    /**
     * public R<?> billOfInventory() {
     * //  拿到所有库位
     * List<Storage> list = storageService.list();
     * List<ParentSelector> parentSelectorList = new ArrayList<>();
     * //  通过ID来区分
     * Map<Long, List<Storage>> collect = list.stream().collect(Collectors.groupingBy(Storage::getId));
     * //  所有ID
     * Set<Long> ids = collect.keySet();
     * ids.forEach(id -> {
     * ParentSelector parentSelector = new ParentSelector();
     * //  一般只有一个 能不能选，能不能用
     * List<Storage> storages = collect.get(id);
     * if (!storages.isEmpty()) {
     * boolean forbidden = storages.get(0).isForbidden();
     * parentSelector.setDisabled(forbidden);
     * }
     * parentSelector.setLabel(storages.get(0).getName());
     * parentSelector.setValue(storages.get(0).getCode());
     * //  查询所有的竖项
     * Map<String,Object> map = new HashMap<>();
     * map.put("storageId", id);
     * List<Inventory> inventories = inventoryService.queryList(map);
     * List<ChildrenSelector> childrenSelectors = new ArrayList<>();
     * inventories.forEach(inventory -> {
     * ChildrenSelector childrenSelector = new ChildrenSelector();
     * childrenSelector.setLabel(inventory.getName());
     * childrenSelector.setValue(inventory.getCode());
     * childrenSelector.setDisabled(true);
     * childrenSelectors.add(childrenSelector);
     * });
     * parentSelector.setChildren(childrenSelectors);
     * parentSelectorList.add(parentSelector);
     * });
     * return R.ok(parentSelectorList);
     * }
     *
     * @return R
     */
    @Override
    public R<?> billOfInventory() {
        // 获取所有库位
        List<Storage> storages = storageService.list();

        // 使用stream API将库位按ID分组
        Map<Long, List<Storage>> storageMap = storages.stream()
                .collect(Collectors.groupingBy(Storage::getId));

        // 将每个库位转换为ParentSelector对象
        List<ParentSelector> parentSelectors = storageMap.entrySet().stream()
                .map(entry -> {
                    Long storageId = entry.getKey();
                    List<Storage> storageList = entry.getValue();
                    // 创建ParentSelector对象
                    ParentSelector parentSelector = new ParentSelector();
                    if (!storageList.isEmpty()) {
                        Storage storage = storageList.get(0);
                        parentSelector.setDisabled(!storage.isForbidden());
                        parentSelector.setLabel(storage.getName());
                        parentSelector.setValue(storage.getCode());
                    }
                    // 查询该库位下的所有库存项
                    Map<String, Object> queryParams = Collections.singletonMap("storageId", storageId);
                    List<Inventory> inventories = inventoryService.queryList(queryParams);
                    // 将库存项转换为ChildrenSelector对象
                    List<ChildrenSelector> childrenSelectors = inventories.stream()
                            .map(inventory -> {
                                ChildrenSelector childrenSelector = new ChildrenSelector();
                                childrenSelector.setLabel(inventory.getName());
                                childrenSelector.setValue(inventory.getCode());
                                childrenSelector.setDisabled(false);
                                return childrenSelector;
                            })
                            .collect(Collectors.toList());
                    parentSelector.setChildren(childrenSelectors);
                    return parentSelector;
                })
                .collect(Collectors.toList());
        return R.ok(parentSelectors);
    }


    /**
     * 根据Code拿到合适的库位
     *
     * @param code 库位Code
     * @return 库位
     */
    public StorageAndInventoryDTO getStorageByCode(String code) {
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
                StorageAndInventoryDTO storageAndInventoryDTO = new StorageAndInventoryDTO();
                storageAndInventoryDTO.setStorage(storages.get(0));
                return storageAndInventoryDTO;
            }
        }
    }

    /**
     * 在多个库位中，拿到最近最合适的那一列
     *
     * @param storages 库位
     * @return 库位
     */
    public StorageAndInventoryDTO getStorage(List<Storage> storages) {
        Map<String, Object> map = new HashMap<>();
        StorageAndInventoryDTO storageAndInventoryDTO = new StorageAndInventoryDTO();
        AtomicBoolean flag = new AtomicBoolean(false);
        if (!storages.isEmpty()) {
            for (Storage s : storages) {
                map.put("status", InventoryEnum.EMPTY.getType());
                map.put("storageId", s.getId());
                List<Inventory> inventories = inventoryService.queryList(map);
                inventories.forEach(i -> {
                    if (i.getStatus().equals(InventoryEnum.EMPTY.getType())) {
                        flag.set(true);
                        storageAndInventoryDTO.setInventory(i);
                    }
                });
                if (flag.get()) {
                    storageAndInventoryDTO.setStorage(s);
                    break;
                }
            }
        } else {
            throw new EException("没有合适的库位！！！");
        }
        return storageAndInventoryDTO;
    }

    /**
     * 根据物料、库存、创建对应的任务
     *
     * @param goods     物料
     * @param inventory 库存
     * @return 任务
     */
    public Task createTask(Goods goods, Inventory inventory) {
        Task task = new Task();
        task.setGoodsId(goods.getId());
        task.setInventoryId(inventory.getId());
        task.setCode(taskService.lastCode());
        task.setName(taskService.lastCode());
        task.setStatus(TaskEnum.INIT_IN.getStatus());
        task.setType(TaskEnum.INIT_IN.getType());
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());
        task.setCreateMember(MemberThreadLocal.get().getMember().getId());
        task.setUpdateMember(MemberThreadLocal.get().getMember().getId());
        task.setResource(TaskEnum.RESOURCE_AUTO.getStatus());
        task.setRemark("任务创建成功");

        task = taskService.saveOrModify(task);
        return task;
    }

    /**
     * 初始化抽象类
     *
     * @param taskExecutor 抽象线程
     * @param goods        物料
     * @param inventory    库存
     * @param task         任务、有就不创建，没有就创建
     */
    public void TaskExecutorInit(TaskExecutor taskExecutor, Goods goods, Inventory inventory, Task task) {
        if (StringUtil.isEmpty(task) || StringUtil.isEmpty(task.getId())) {
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
