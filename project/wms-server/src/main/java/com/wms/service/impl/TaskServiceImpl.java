package com.wms.service.impl;

import com.wms.dao.TaskDao;
import com.wms.enums.TaskEnum;
import com.wms.exception.EException;
import com.wms.pojo.Task;
import com.wms.service.TaskService;
import com.wms.service.base.IBaseServiceImpl;
import com.wms.thread.MemberThreadLocal;
import com.wms.utils.CodeUtils;
import com.wms.utils.R;
import com.wms.utils.StringUtil;
import com.wms.vo.TaskVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TaskServiceImpl extends IBaseServiceImpl<TaskDao, Task, TaskVo> implements TaskService {
    @Resource
    private TaskDao taskDao;

    @Override
    public String lastCode() {
        return CodeUtils.getString((taskDao.lastCode()));
    }

    /**
     * 更新任务信息
     *
     * @param task 任务
     * @return R
     */
    @Override
    public R<?> saveOrUpdateTask(Task task) {
        //  保存新的任务
        if (StringUtil.isEmpty(task.getId())) {

            return R.ok("保存成功！");
        } else {
            //  修改任务信息
            updateTask(task);
            return R.ok("修改成功！");
        }
    }

    /**
     * 修改任务
     *
     * @param task 任务
     */
    public void updateTask(Task task) {
        if (!TaskEnum.INIT_IN.getStatus().equals(task.getStatus())) {
            throw new EException("该任务已下发或已挂起，无法修改任务信息！");
        }
        task.setUpdateMember(getCurrentMember());
        task.setUpdateTime(new Date());
        saveOrModify(task);
        if (task.isDirectlyIssued()) {
            //  直接下发
        } else {
            //  不下发
        }

    }


    /**
     * 创建任务
     *
     * @param task 任务
     */
    public void createTask(Task task) {
        task.setCreateMember(getCurrentMember());
        task.setUpdateMember(getCurrentMember());
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());
        task.setStatus(TaskEnum.INIT_IN.getStatus());
        task.setCode(lastCode());
        saveOrModify(task);
        //  是否直接下发
        if (task.isDirectlyIssued()) {
            //  是
        } else {
            //  否

        }
    }

    public Long getCurrentMember() {
        return MemberThreadLocal.get().getMember().getId();
    }
}
