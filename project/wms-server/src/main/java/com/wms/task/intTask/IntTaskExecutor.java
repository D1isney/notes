package com.wms.task.intTask;

import com.wms.connect.plc.PlcConnect;
import com.wms.constant.PlcConstant;
import com.wms.enums.InventoryEnum;
import com.wms.enums.PLCEnum;
import com.wms.enums.TaskEnum;
import com.wms.exception.EException;
import com.wms.pojo.Goods;
import com.wms.pojo.Inventory;
import com.wms.pojo.Storage;
import com.wms.pojo.Task;
import com.wms.service.GoodsService;
import com.wms.service.InventoryService;
import com.wms.service.StorageService;
import com.wms.task.TaskExecutor;
import com.wms.thread.MemberThreadLocal;
import com.wms.utils.StringUtil;

import java.util.Date;

/**
 * 入库任务执行器
 */
public class IntTaskExecutor extends TaskExecutor {
    private Goods goods = new Goods();
    private Storage storage = new Storage();
    private Inventory inventory = new Inventory();
    private InventoryService inventoryServiceInt;
    private StorageService storageServiceInt;
    private GoodsService goodsServiceInt;
    private PlcConnect plcConnect;

    //  准备数据
    @Override
    public void prepare() {
        //  拿到任务
        Task task = getTask();
        inventoryServiceInt = getInventoryService();
        storageServiceInt = getStorageService();
        goodsServiceInt = getGoodsService();
        if (!StringUtil.isEmpty(task.getInventoryId())) {
            inventory = inventoryServiceInt.queryById(task.getInventoryId());
        } else {
            throw new EException("任务=======》库存ID不能为空！");
        }
        if (!StringUtil.isEmpty(inventory.getStorageId())) {
            storage = storageServiceInt.queryById(inventory.getStorageId());
        } else {
            throw new EException("库存=======》库位ID不能为空！");
        }
        if (!StringUtil.isEmpty(task.getGoodsId())) {
            goods = goodsServiceInt.queryById(task.getGoodsId());
        } else {
            throw new EException("任务=======》物料ID不能为空！");
        }

    }

    @Override
    public void attempt() throws InterruptedException {
        plcConnect = getPlcConnect();
        int i = plcConnect.readPlc(PLCEnum.PLC_INT);
        //  等待plc可以写入 如果不可以写入就一直等
        while (i != PlcConstant.canBeWritten) {
            i = plcConnect.readPlc(PLCEnum.PLC_INT);
            //  睡眠指定之间再次执行
            Thread.sleep(getSleepTime());
        }
    }

    //  可以写入了
    @Override
    public void write() {
        //  让PLC不能再次写入
        plcConnect.writePlc(PLCEnum.PLC_INT, PlcConstant.cannotBeWritten);
        //  开始入库
        plcConnect.writePlc(PLCEnum.PLC_ACCOMPLISH_IN, PlcConstant.cannotBeWritten);
        //  拿到任务
        Task task = getTask();
        //  把任务code分割成两个数据
        Integer[] taskInt = splitCode(task.getCode());
        plcConnect.writePlc(PLCEnum.PLC_TASK1_INT, taskInt[0]);
        plcConnect.writePlc(PLCEnum.PLC_TASK2_INT, taskInt[1]);
        //  将两个物料code都存进RFID
        Integer[] goodsInt = splitCode(goods.getCode());
        plcConnect.writePlc(PLCEnum.PLC_RFID_IN1, goodsInt[0]);
        plcConnect.writePlc(PLCEnum.PLC_RFID_IN2, goodsInt[1]);
        //  水平位置
        plcConnect.writePlc(PLCEnum.PLC_STORAGE_LEVEL_IN, inventory.getLayer());
        //  垂直位置
        plcConnect.writePlc(PLCEnum.PLC_STORAGE_VERTICAL_IN, storage.getRow());
        operation(inventory,task,InventoryEnum.COMING_IN, TaskEnum.ONGOING_IN);
        //  创建日志并推送
        log(inventory,task,InventoryEnum.COMING_IN,TaskEnum.ONGOING_IN);
    }

    @Override
    public void results() throws InterruptedException {
        int i = plcConnect.readPlc(PLCEnum.PLC_ACCOMPLISH_IN);
        //  等待plc入库完成
        while (i != PlcConstant.canBeWritten) {
            i = plcConnect.readPlc(PLCEnum.PLC_ACCOMPLISH_IN);
            //  睡眠指定之间再次执行
            Thread.sleep(getSleepTime());
        }
    }

    //  入库完成，更改库存，任务状态
    @Override
    public void refresh() {
        Task task = getTask();
        operation(inventory,task,InventoryEnum.HAVE, TaskEnum.ACCOMPLISH_IN);

    }
}
