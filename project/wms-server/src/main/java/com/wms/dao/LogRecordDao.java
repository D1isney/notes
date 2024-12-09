package com.wms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wms.pojo.LogRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogRecordDao extends BaseMapper<LogRecord> {
}
