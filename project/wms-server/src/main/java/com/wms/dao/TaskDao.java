package com.wms.dao;

import com.wms.pojo.Task;
import com.wms.service.base.IBaseMapper;
import com.wms.vo.TaskVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TaskDao extends IBaseMapper<Task, TaskVo> {
    String lastCode();

    Task queryByCode(@Param("code") String code);
}
