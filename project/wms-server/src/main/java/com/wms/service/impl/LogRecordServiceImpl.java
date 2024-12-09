package com.wms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wms.dao.LogRecordDao;
import com.wms.pojo.LogRecord;
import com.wms.service.LogRecordService;
import org.springframework.stereotype.Service;

@Service
public class LogRecordServiceImpl extends ServiceImpl<LogRecordDao, LogRecord> implements LogRecordService {
}
