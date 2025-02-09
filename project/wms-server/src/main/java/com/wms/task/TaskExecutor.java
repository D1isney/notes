package com.wms.task;

import com.wms.connect.plc.PlcConnect;
import com.wms.connect.websocket.WebSocketServer;
import com.wms.connect.websocket.WebSocketServerWeb;
import com.wms.enums.InventoryEnum;
import com.wms.enums.TaskEnum;
import com.wms.enums.WebSocketEnum;
import com.wms.exception.EException;
import com.wms.filter.login.LoginMember;
import com.wms.handler.TaskExceptionHandler;
import com.wms.pojo.Inventory;
import com.wms.pojo.LogRecord;
import com.wms.pojo.Task;
import com.wms.service.*;
import com.wms.thread.MemberThreadLocal;
import com.wms.utils.FastJsonUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

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
    public abstract void refresh() throws InterruptedException;

    //  任务
    private Task task;
    //  库存情况
    private Inventory inventory;
    private Integer inventoryOldStatus;

    //  Plc连接工具
    private PlcConnect plcConnect;

    private TaskService taskService;
    private InventoryService inventoryService;
    private StorageService storageService;
    private GoodsService goodsService;
    private LogRecordService logRecordService;

    private Integer sleepTime;
    private Long startTime;
    private Long endTime;

    private LoginMember memberThreadLocal;

    private LogRecord logRecord = new LogRecord();

    //  任务执行
    @Override
    public synchronized void run() {
        startTime = System.currentTimeMillis();
        try {
            prepare();
            attempt();
            write();
            results();
            endTime = System.currentTimeMillis();
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
            //TODO: 报错推送
            if (exceptionHandler != null) {
                endTime = System.currentTimeMillis();
                recover(e.getMessage());
                exceptionHandler.handleException(e);
            }
        }
    }

    @Setter
    protected TaskExceptionHandler exceptionHandler;

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
        inventory.setUpdateMember(memberThreadLocal.getMember().getId());
        task.setStatus(taskEnum.getStatus());
        task.setUpdateTime(new Date());
        task.setUpdateMember(memberThreadLocal.getMember().getId());
        inventoryService.saveOrModify(inventory);
        taskService.saveOrModify(task);
    }


    public void log(LogRecord logRecord, Inventory inventory, Task task, InventoryEnum inventoryEnum, TaskEnum taskEnum, boolean success) {
        //  创建日志
        logRecord.setMemberId(memberThreadLocal.getMember().getId());
        logRecord.setCreateTime(new Date());
        String str = "任务：" + task.getCode() + "，目前状态：" + taskEnum.getMessage() + "，库位状态：" + inventoryEnum.getMessage() + "。";
        logRecord.setMessage(str);
        Map<String, Object> map = new HashMap<>();
        map.put("task", task);
        map.put("inventory", inventory);
        logRecord.setParams(FastJsonUtils.collectToString(map));
        if (success) {
            //  结束时间
            logRecord.setExecuteTime((endTime - startTime));
            //  结果
            logRecord.setResult(taskEnum.getMessage());
        }
        //TODO: 推送
        logRecordService.saveOrModify(logRecord);
//        if (taskEnum.getType().equals(TaskEnum.INIT_IN.getType()) || taskEnum.getType().equals(TaskEnum.INIT_OUT.getType())) {
//            WebSocketServerWeb.send(WebSocketEnum.TASK_MESSAGE_ISSUED);
//        } else {
        WebSocketServerWeb.send(WebSocketEnum.LOG);
//        }
    }

    public LogRecord createLog(Task task, TaskEnum taskEnum, InventoryEnum inventoryEnum) {
        logRecord = new LogRecord();
        logRecord.setCreateTime(new Date());
        logRecord.setPath("/inventory/saveOrUpdateInventory");
        logRecord.setMemberId(memberThreadLocal.getMember().getId());
        String str = "任务：" + task.getCode() + "，目前状态：" + taskEnum.getMessage() + "，库位状态：" + inventoryEnum.getMessage() + "。";
        logRecord.setMessage(str);
        Map<String, Object> map = new HashMap<>();
        map.put("inventory", inventoryEnum);
        map.put("task", task);
        logRecord.setParams(FastJsonUtils.collectToString(map));
        logRecord.setType(taskEnum.getType());
        logRecordService.saveOrModify(logRecord);
        return logRecord;
    }


    public void recover(String result) {
        logRecord.setMessage("任务：" + task.getCode() + "操作任务失败！！");
        logRecord.setResult(result);
        logRecord.setExecuteTime((endTime - startTime));
        logRecordService.saveOrModify(logRecord);
        //  删除任务
        taskService.delete(task.getId());

        //  恢复库存情况
        inventory.setStatus(inventoryOldStatus);
        inventory.setUpdateTime(new Date());
        inventory.setUpdateMember(memberThreadLocal.getMember().getId());
        inventoryService.saveOrModify(inventory);
    }
}
