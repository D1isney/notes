package com.wms.service.impl;

import com.wms.dao.TaskDao;
import com.wms.pojo.Task;
import com.wms.service.TaskService;
import com.wms.service.base.IBaseServiceImpl;
import com.wms.utils.CodeUtils;
import com.wms.vo.TaskVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TaskServiceImpl extends IBaseServiceImpl<TaskDao, Task, TaskVo> implements TaskService {
    @Resource
    private TaskDao taskDao;

    @Override
    public String lastCode() {
        return CodeUtils.getString((taskDao.lastCode()));
    }
}
