package com.wms.task;

import com.wms.connect.plc.PlcConnect;
import com.wms.enums.InventoryEnum;
import com.wms.enums.TaskEnum;
import com.wms.pojo.Inventory;
import com.wms.pojo.LogRecord;
import com.wms.pojo.Task;
import com.wms.service.GoodsService;
import com.wms.service.InventoryService;
import com.wms.service.StorageService;
import com.wms.service.TaskService;
import com.wms.thread.MemberThreadLocal;
import com.wms.utils.FastJsonUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public abstract class TaskExecutor implements Runnable {

    //  准备数据
    public abstract void prepare();

    // 尝试写入
    public abstract void attempt() throws InterruptedException;

    //  写入
    public abstract void write();

    //  等待结果
    public abstract void results() throws InterruptedException;

    //  刷新任务状态
    public abstract void refresh();

    //  任务
    private Task task;

    //  Plc连接工具
    private PlcConnect plcConnect;

    private TaskService taskService;
    private InventoryService inventoryService;
    private StorageService storageService;
    private GoodsService goodsService;

    private Integer sleepTime;
    private Long startTime;
    private Long endTime;

    //  线程池
    private static final Pool pool = new Pool(1, 1, 5000, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    //  任务执行
    @Override
    public synchronized void run() {
        startTime = System.currentTimeMillis();
        try {
            prepare();
            attempt();
            write();
            results();
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
        endTime = System.currentTimeMillis();

    }

    public Integer[] splitCode(String code) {
        int split = code.length() / 2;

        int start = Integer.parseInt(code.substring(0, split));
        int end = Integer.parseInt(code.substring(split));
        return new Integer[]{start, end};
    }


    /**
     * 出入库状态修改
     *
     * @param inventory     库位存情况
     * @param task          任务
     * @param inventoryType 出入库
     */
    public void operation(Inventory inventory, Task task, InventoryEnum inventoryType, TaskEnum taskEnum) {
        //  更新库位，任务状态，推送日志
        //  正在入库
        inventory.setStatus(inventoryType.getType());
        inventory.setUpdateTime(new Date());
        inventory.setUpdateMember(MemberThreadLocal.get().getMember().getId());

        task.setStatus(taskEnum.getStatus());
        task.setUpdateTime(new Date());
        task.setUpdateMember(MemberThreadLocal.get().getMember().getId());
        inventoryService.saveOrModify(inventory);
        taskService.saveOrModify(task);
    }


    public void log(Inventory inventory, Task task, InventoryEnum inventoryEnum, TaskEnum taskEnum) {
        //  创建日志
        LogRecord logRecord = new LogRecord();
        logRecord.setMemberId(MemberThreadLocal.get().getMember().getId());
        logRecord.setCreateTime(new Date());
        String str = "任务：" + task.getCode() + "，目前状态：" + taskEnum.getMessage() + "，库位状态：" + inventoryEnum.getMessage() + "。";
        logRecord.setMessage(str);

        Map<String,Object> map = new HashMap<>();
        map.put("task",task);
        map.put("inventory",inventory);
        logRecord.setParams(FastJsonUtils.collectToString(map));
        //  结束时间
        logRecord.setExecuteTime((startTime - endTime));

    }
}
