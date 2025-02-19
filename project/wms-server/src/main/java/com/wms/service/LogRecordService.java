package com.wms.service;

import com.wms.pojo.LogRecord;
import com.wms.service.base.BaseService;
import com.wms.utils.R;
import com.wms.vo.LogRecordVo;

public interface LogRecordService extends BaseService<LogRecord, LogRecordVo> {
    LogRecord insertOrUpdate(LogRecord log);

    R<?> alarmStatistics();
}
