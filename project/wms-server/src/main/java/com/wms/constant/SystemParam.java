package com.wms.constant;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Component
public class SystemParam {
    //  PLC是否连接
    private boolean isConnect = false;


    public static final boolean isConnect_YES = true;
    public static final boolean isConnect_NO = false;
}
