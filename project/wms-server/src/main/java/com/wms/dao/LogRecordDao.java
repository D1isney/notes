package com.wms.dao;

import com.wms.pojo.LogRecord;
import com.wms.service.base.IBaseMapper;
import com.wms.vo.LogRecordVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogRecordDao extends IBaseMapper<LogRecord, LogRecordVo> {
}
