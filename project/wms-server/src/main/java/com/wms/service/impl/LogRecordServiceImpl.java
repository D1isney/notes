package com.wms.service.impl;

import com.wms.dao.LogRecordDao;
import com.wms.dto.TypeAndValue;
import com.wms.enums.LogRecordEnum;
import com.wms.enums.TaskEnum;
import com.wms.pojo.LogRecord;
import com.wms.pojo.Task;
import com.wms.service.LogRecordService;
import com.wms.service.base.IBaseServiceImpl;
import com.wms.utils.DateUtil;
import com.wms.utils.R;
import com.wms.vo.LogRecordVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class LogRecordServiceImpl extends IBaseServiceImpl<LogRecordDao, LogRecord, LogRecordVo> implements LogRecordService{

    @Override
    public LogRecord insertOrUpdate(LogRecord log) {
        return saveOrModify(log);
    }


    @Value("${data.queryDays}")
    private Integer queryDays;

    /**
     * 报警日志统计
     * @return R
     */
    @Override
    public R<?> alarmStatistics() {
        Map<String, List<TypeAndValue>> mapList = new HashMap<>();
        // 获取警告日志统计
        List<TypeAndValue> warning = getLogStatistics(LogRecordEnum.WARNING_LOG);
        // 获取报警日志统计
        List<TypeAndValue> alarm = getLogStatistics(LogRecordEnum.ALARM_LOG);
        mapList.put("warning", warning);
        mapList.put("alarm", alarm);
        return R.ok(mapList);
    }

    /**
     * 获取指定类型的日志统计信息
     *
     * @param logType 日志类型
     * @return 日志统计信息列表
     */
    private List<TypeAndValue> getLogStatistics(LogRecordEnum logType) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<TypeAndValue> logStatistics = new ArrayList<>();
        for (int i = -queryDays; i <= 0; i++) {
            Date date = DateUtil.currentAdd(i);
            date = DateUtil.resetTimeToStartOfDay(date);
            Map<String, Object> map = new HashMap<>();
            map.put("statisticsTime", date);
            map.put("type", logType.getCode());
            List<LogRecord> logRecords = queryList(map);
            TypeAndValue typeAndValue = new TypeAndValue();
            typeAndValue.setName(sdf.format(date));
            typeAndValue.setValue(String.valueOf(logRecords.size()));
            logStatistics.add(typeAndValue);
        }

        return logStatistics;
    }
}
