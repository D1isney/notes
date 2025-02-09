package com.wms.task.outTask;

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

public class OutTaskExecutor extends TaskExecutor {
    private Goods goods = new Goods();
    private Storage storage = new Storage();
    private Inventory inventory = new Inventory();
    private PlcConnect plcConnect;

    private LogRecord logRecord;


    @Override
    public void prepare() {
        //  拿到任务
        Task task = getTask();
        StorageService storageServiceInt = getStorageService();
        GoodsService goodsServiceInt = getGoodsService();
        inventory = getInventory();
        inventory.setStatus(InventoryEnum.LEAVING_THE_WAREHOUSE.getType());
        if (!StringUtil.isEmpty(task.getGoodsId())) {
            goods = goodsServiceInt.queryById(task.getGoodsId());
        } else {
            throw new EException("任务=======》物料ID不能为空！");
        }
        if (!StringUtil.isEmpty(inventory.getStorageId())) {
            storage = storageServiceInt.queryById(inventory.getStorageId());
        } else {
            throw new EException("库存=======》库位ID不能为空！");
        }
        logRecord = createLog(task, TaskEnum.INIT_OUT, InventoryEnum.LEAVING_THE_WAREHOUSE);
        log(logRecord, inventory, task, InventoryEnum.LEAVING_THE_WAREHOUSE, TaskEnum.INIT_OUT, false);
    }

    @Override
    public void attempt() throws InterruptedException {
        plcConnect = getPlcConnect();
        int i = plcConnect.readPlc(PLCEnum.PLC_OUT);
        //  等待plc可以写入 如果不可以写入就一直等
        while (i != PlcConstant.canBeWritten) {
            i = plcConnect.readPlc(PLCEnum.PLC_OUT);
            //  睡眠指定之间再次执行
            Thread.sleep(getSleepTime());
        }
    }

    @Override
    public void write() {
        //  让PLC不能再次写入
        plcConnect.writePlc(PLCEnum.PLC_OUT, PlcConstant.cannotBeWritten);
        //  开始出库
        //  开始入库
        plcConnect.writePlc(PLCEnum.PLC_ACCOMPLISH_OUT, PlcConstant.cannotBeWritten);
        //  拿到任务
        Task task = getTask();
        //  把任务code分割成两个数据
        Integer[] taskInt = splitCode(task.getCode());
        plcConnect.writePlc(PLCEnum.PLC_TASK1_OUT, taskInt[0]);
        plcConnect.writePlc(PLCEnum.PLC_TASK2_OUT, taskInt[1]);
        //  将两个物料code都存进RFID
        Integer[] goodsInt = splitCode(goods.getCode());
        plcConnect.writePlc(PLCEnum.PLC_RFID_OUT1, goodsInt[0]);
        plcConnect.writePlc(PLCEnum.PLC_RFID_OUT2, goodsInt[1]);
        //  水平位置
        plcConnect.writePlc(PLCEnum.PLC_STORAGE_LEVEL_OUT, inventory.getLayer());
        //  垂直位置
        plcConnect.writePlc(PLCEnum.PLC_STORAGE_VERTICAL_OUT, storage.getRow());
        //  物料出库
        inventory.setGoodsId(0L);
        operation(inventory, task, InventoryEnum.LEAVING_THE_WAREHOUSE, TaskEnum.ONGOING_OUT);
        //  创建日志并推送
        log(logRecord, inventory, task, InventoryEnum.LEAVING_THE_WAREHOUSE, TaskEnum.ONGOING_OUT, false);
    }

    @Override
    public void results() throws InterruptedException {
        int i = plcConnect.readPlc(PLCEnum.PLC_ACCOMPLISH_OUT);
        //  等待plc入库完成
        while (i != PlcConstant.canBeWritten) {
            i = plcConnect.readPlc(PLCEnum.PLC_ACCOMPLISH_OUT);
            //  睡眠指定之间再次执行
            Thread.sleep(getSleepTime());
        }
        Task task = getTask();
        operation(inventory, task, InventoryEnum.ISSUE_COMPLETED, TaskEnum.ACCOMPLISH_OUT);
        log(logRecord, inventory, task, InventoryEnum.ISSUE_COMPLETED, TaskEnum.ACCOMPLISH_OUT, false);
    }

    @Override
    public void refresh() throws InterruptedException {
        //  睡一秒
        Thread.sleep(getSleepTime());
        Task task = getTask();
        operation(inventory, task, InventoryEnum.EMPTY, TaskEnum.ACCOMPLISH_OUT);
        log(logRecord, inventory, task, InventoryEnum.EMPTY, TaskEnum.ACCOMPLISH_OUT, true);
        //  睡一秒
        Thread.sleep(getSleepTime());
        //  可以写入了
        plcConnect.writePlc(PLCEnum.PLC_ACCOMPLISH_OUT, PlcConstant.canBeWritten);
        plcConnect.writePlc(PLCEnum.PLC_OUT, PlcConstant.canBeWritten);
    }
}
