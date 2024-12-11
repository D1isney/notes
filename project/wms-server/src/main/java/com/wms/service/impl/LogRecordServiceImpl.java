package com.wms.service.impl;

import com.wms.dao.LogRecordDao;
import com.wms.pojo.LogRecord;
import com.wms.service.LogRecordService;
import com.wms.service.base.IBaseServiceImpl;
import com.wms.vo.LogRecordVo;
import org.springframework.stereotype.Service;

@Service
public class LogRecordServiceImpl extends IBaseServiceImpl<LogRecordDao, LogRecord, LogRecordVo> implements LogRecordService{

    @Override
    public LogRecord insertOrUpdate(LogRecord log) {
        return saveOrModify(log);
    }
}
