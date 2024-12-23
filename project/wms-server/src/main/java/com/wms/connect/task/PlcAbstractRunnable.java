package com.wms.connect.task;

import lombok.extern.slf4j.Slf4j;

/**
 * PLC 操作抽象线程
 */
@Slf4j
public abstract class PlcAbstractRunnable  implements Runnable{


    /**
     * 订单处理
     * 包含创建订单、更新订单
     */
    abstract void orderProcessing();

    /**
     * 任务处理
     * 包含订单状态更新、任务状态更新、库位分配、库位更新、提交任务日志
     */
    abstract void taskProcessing();


    /**
     * PLC处理
     * 包含PLC下发、任务状态更新
     */
    abstract void PLCProcessing();

    /**
     * PLC反馈
     * 包含：任务状态更新、任务更新
     */
    abstract void feedback();


    /**
     * 更新所有状态，提交日志
     */
    abstract void submit();


    /**
     * 出入库操作线程
     */
    @Override
    public void run() {
        log.info("开启线程==========》ID：{}，Name：{}", Thread.currentThread().getId(), Thread.currentThread().getName());
        orderProcessing();
        taskProcessing();
        PLCProcessing();
        feedback();
        submit();
        log.info("线程结束==========》ID：{}，Name：{}", Thread.currentThread().getId(), Thread.currentThread().getName());
    }
}
