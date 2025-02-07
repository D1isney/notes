package com.wms.service.impl;

import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.wms.connect.plc.PlcConnect;
import com.wms.constant.InOrOutConstant;
import com.wms.dao.InventoryDao;
import com.wms.dto.WarehousingDTO;
import com.wms.enums.InventoryEnum;
import com.wms.enums.TaskEnum;
import com.wms.exception.EException;
import com.wms.filter.login.LoginMember;
import com.wms.handler.TaskExceptionHandler;
import com.wms.pojo.Goods;
import com.wms.pojo.Inventory;
import com.wms.pojo.Storage;
import com.wms.pojo.Task;
import com.wms.service.*;
import com.wms.service.base.IBaseServiceImpl;
import com.wms.task.TaskExecutor;
import com.wms.task.intTask.InTaskExecutor;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

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
    public R<?> warehousing(WarehousingDTO warehousingDTO) {
        /*
            1、物料编码
            2、库位编码（有、无）
            3、库存编码（有、无）
         */
        if (!StringUtil.isEmpty(warehousingDTO)) {
            Goods goodsByCode = getGoodsByCode(warehousingDTO.getGoodsCode());
            Inventory inventoryByCode = getInventoryByCode(warehousingDTO.getInventoryCode());
            Storage storageByCode = getStorageByCode(warehousingDTO.getStorageCode(),inventoryByCode);
            if (warehousingDTO.getType().equals(InOrOutConstant.in)) {
                in(goodsByCode, inventoryByCode, storageByCode);
                return R.ok("正在下发入库任务！");
            } else {
                return R.ok("正在下发出库任务！");
            }
        } else {
            return R.error("无效入库信息！！！");
        }
    }

    private void in(Goods goods, Inventory inventory, Storage storage) {
        if (!inventory.getStatus().equals(InventoryEnum.EMPTY.getType())) {
            throw new EException("该库位" + storage.getRow() + "-" + inventory.getLayer() + "无法已有物料，无法再次入库！！！");
        }
        TaskExecutor taskExecutor = null;
        taskExecutor = new InTaskExecutor();
        TaskExecutorInit(taskExecutor, goods, inventory);

        taskExecutor.setExceptionHandler(e -> {
            throw new EException(e.getMessage());
        });
        threadPoolExecutor.execute(taskExecutor);
    }

    public void test(String message) {
        throw new EException(message);
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

    public Inventory getInventoryByCode(String code) {
        Map<String, Object> map = new HashMap<>();
        List<Inventory> inventories;
        if (StringUtil.isEmpty(code)) {
            //  随便生成一个
            map.put("status", InventoryEnum.EMPTY.getType());
            inventories = inventoryService.queryList(map);
            if (inventories.isEmpty()) {
                throw new EException("没有合适的库存！！！");
            }
            return inventories.get(0);
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
                throw new EException("库位查询失败！！！");
            }
        }

    }

    public Storage getStorageByCode(String code,Inventory inventoryByCode) {
        if (StringUtil.isEmpty(code)) {
            Long storageId = inventoryByCode.getStorageId();
            return storageService.queryById(storageId);
        }else {
            Map<String, Object> map = new HashMap<>();
            map.put("code", code);
            List<Storage> storages = storageService.queryList(map);
            if (storages.isEmpty()) {
                throw new EException("不存在库位：" + code);
            } else {
                return storages.get(0);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
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
        task = taskService.saveOrModify(task);
        return task;
    }

    public void TaskExecutorInit(TaskExecutor taskExecutor, Goods goods, Inventory inventory) {
        Task task = createTask(goods, inventory);
        taskExecutor.setTask(task);
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
