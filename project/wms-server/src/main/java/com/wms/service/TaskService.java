package com.wms.service;

import com.wms.pojo.Task;
import com.wms.service.base.BaseService;
import com.wms.vo.TaskVo;

public interface TaskService extends BaseService<Task, TaskVo> {
    String lastCode();
}
