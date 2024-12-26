package com.wms.vo;


import com.wms.pojo.LogRecord;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel(value="LogVo", description="包装类")
public class LogRecordVo extends LogRecord {
    //  调用用户名称
    private String username;
}
