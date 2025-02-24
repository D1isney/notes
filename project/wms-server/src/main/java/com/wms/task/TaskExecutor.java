package com.wms.task;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.wms.connect.plc.PlcConnect;
import com.wms.connect.websocket.Push;
import com.wms.connect.websocket.WebSocketServerWeb;
import com.wms.enums.InventoryEnum;
import com.wms.enums.LogRecordEnum;
import com.wms.enums.TaskEnum;
import com.wms.enums.WebSocketEnum;
import com.wms.filter.login.LoginMember;
import com.wms.handler.TaskExceptionHandler;
import com.wms.pojo.Inventory;
import com.wms.pojo.LogRecord;
import com.wms.pojo.Task;
import com.wms.service.*;
import com.wms.utils.FastJsonUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

        try {
            prepare();
            //  计算工作时间
            attempt();
            startTime = System.currentTimeMillis();
            write();
            results();
            endTime = System.currentTimeMillis();
            refresh();
            //  操作成功之后 推送任务状态
            Push push = new Push(WebSocketEnum.TASK_MESSAGE_SUCCESS.getType(), WebSocketEnum.TASK_MESSAGE_SUCCESS.getMessage(), getTask());
            WebSocketServerWeb.send(push);
        } catch (Exception e) {
            e.printStackTrace();
            if (exceptionHandler != null) {
                endTime = System.currentTimeMillis();
                //  回滚任务
                recover(e.getMessage());
                exceptionHandler.handleException(e);
            }
        }
    }

    @Setter
    protected TaskExceptionHandler exceptionHandler;

    /**
     * 分库COde
     *
     * @param code 编码
     * @return 分割后的数组
     */
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
    public void operation(Inventory inventory, Task task, InventoryEnum inventoryType, TaskEnum taskEnum, Boolean flag) {
        //  更新库位，任务状态，推送日志
        //  正在入库
        inventory.setStatus(inventoryType.getType());
        inventory.setUpdateTime(new Date());
        inventory.setUpdateMember(memberThreadLocal.getMember().getId());
        task.setStatus(taskEnum.getStatus());
        task.setUpdateMember(memberThreadLocal.getMember().getId());
        task.setUpdateTime(new Date());
        if (flag) {
            float activation = getActivation(task);
            task.setActivation(activation);
        }
        inventoryService.saveOrModify(inventory);
        taskService.saveOrModify(task);
    }

    private float getActivation(Task task) {
        long all = DateUtil.between(task.getCreateTime(), task.getUpdateTime(), DateUnit.MS);
        //  工作时间
        float word = endTime - startTime;
        float activation = word / all;
        activation = activation * 100;
        if (activation > 0) {
            activation = (Math.round(activation) * 100.00f) / 100.00f;
        } else {
            activation = 0;
        }
        return activation;
    }


    /**
     * 修改日志状态
     *
     * @param logRecord     日志
     * @param inventory     库存
     * @param task          任务
     * @param inventoryEnum 库存当前的状态
     * @param taskEnum      任务当前的状态
     * @param success       该任务是否完成了
     */
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
        logRecordService.saveOrModify(logRecord);

        //推送操作日志
        WebSocketServerWeb.send(WebSocketEnum.OPERATION);
    }

    /**
     * 创建全局使用的日志，后续会一直操作这个日志
     *
     * @param task          当前的任务
     * @param taskEnum      当前任务状态
     * @param inventoryEnum 当前库存状态
     * @return 日志
     */
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
        logRecord.setType(LogRecordEnum.WARNING_LOG.getCode());
        logRecordService.saveOrModify(logRecord);
        //  删除任务
        if (TaskEnum.RESOURCE_AUTO.getStatus().equals(task.getResource())) {
            taskService.delete(task.getId());
        }

        //  恢复库存情况
        inventory.setStatus(inventoryOldStatus);
        inventory.setUpdateTime(new Date());
        inventory.setUpdateMember(memberThreadLocal.getMember().getId());
        inventoryService.saveOrModify(inventory);


    }
}
