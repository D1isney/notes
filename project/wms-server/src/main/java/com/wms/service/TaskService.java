package com.wms.service;

import com.wms.pojo.Task;
import com.wms.service.base.BaseService;
import com.wms.utils.R;
import com.wms.vo.TaskVo;

public interface TaskService extends BaseService<Task, TaskVo> {
    String lastCode();

    R<?> saveOrUpdateTask(Task task);

    R<?> manualOperationIssued(Task task);

    R<?> getGoodsAndInventory(Task task);

    R<?> deleteTask(Long[] ids);

    R<?> weeklyWorkload();

    R<?> inboundAndOutboundVolume();

    R<?> averageRateOfActivity();
}
