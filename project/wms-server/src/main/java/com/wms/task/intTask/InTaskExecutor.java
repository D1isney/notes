package com.wms.task.intTask;

import com.wms.connect.plc.PlcConnect;
import com.wms.constant.PlcConstant;
import com.wms.enums.InventoryEnum;
import com.wms.enums.PLCEnum;
import com.wms.enums.TaskEnum;
import com.wms.exception.EException;
import com.wms.pojo.*;
import com.wms.service.GoodsService;
import com.wms.service.StorageService;
import com.wms.task.TaskExecutor;
import com.wms.utils.StringUtil;


/**
 * 入库任务执行器
 */
public class InTaskExecutor extends TaskExecutor {
    private Goods goods = new Goods();
    private Storage storage = new Storage();
    private Inventory inventory = new Inventory();
    private PlcConnect plcConnect;

    private LogRecord logRecord;

    //  准备数据
    @Override
    public void prepare() {
        //  拿到任务
        Task task = getTask();
        inventory = getInventory();
        inventory.setStatus(InventoryEnum.COMING_IN.getType());
        StorageService storageServiceInt = getStorageService();
        GoodsService goodsServiceInt = getGoodsService();
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
        logRecord = createLog(task, TaskEnum.INIT_IN, InventoryEnum.COMING_IN);
        log(logRecord, inventory, task, InventoryEnum.COMING_IN, TaskEnum.INIT_IN, false);
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
        //  物料入库
        inventory.setGoodsId(goods.getId());
        operation(inventory, task, InventoryEnum.COMING_IN, TaskEnum.ONGOING_IN);
        //  创建日志并推送
        log(logRecord, inventory, task, InventoryEnum.COMING_IN, TaskEnum.ONGOING_IN, false);
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
        Task task = getTask();
        operation(inventory, task, InventoryEnum.STORAGE_COMPLETED, TaskEnum.ACCOMPLISH_IN);
        log(logRecord, inventory, task, InventoryEnum.STORAGE_COMPLETED, TaskEnum.ACCOMPLISH_IN, false);
    }

    //  入库完成，更改库存，任务状态
    @Override
    public void refresh() throws InterruptedException {
        //  睡一秒
        Thread.sleep(getSleepTime());
        Task task = getTask();
        operation(inventory, task, InventoryEnum.HAVE, TaskEnum.ACCOMPLISH_IN);
        log(logRecord, inventory, task, InventoryEnum.HAVE, TaskEnum.ACCOMPLISH_IN, true);
        //  睡一秒
        Thread.sleep(getSleepTime());
        //  可以写入了
        plcConnect.writePlc(PLCEnum.PLC_ACCOMPLISH_IN, PlcConstant.canBeWritten);
        plcConnect.writePlc(PLCEnum.PLC_INT, PlcConstant.canBeWritten);
    }
}
