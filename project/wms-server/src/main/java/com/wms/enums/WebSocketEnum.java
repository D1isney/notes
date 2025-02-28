package com.wms.enums;

import com.wms.constant.InOrOutConstant;
import lombok.Getter;
import lombok.Setter;

@Getter
public enum WebSocketEnum {
    LOG("更新日志", null, "log",200),
    TASK("更新任务", null, "task",200),
    PLC_CONNECT_ERROR("请先连接PLC！！！", null, "PlcConnectError",200),
    TASK_MESSAGE_ISSUED("任务下发", null, "TaskMessageIssued",200),
    TASK_MESSAGE_SUCCESS("任务完成", null, "TaskMessageSuccess",200),
    OPERATION("Push操作",null, "operation",200),

    SURPLUS_VIEW("刷新surplusView",null,"surplusView",200),
    WEEKLY_WORKLOAD_VIEW("刷新weeklyWorkloadView",null,"weeklyWorkloadView",200),
    INBOUND_AND_OUTBOUND_VOLUME_VIEW("刷新inboundAndOutboundVolumeView",null,"inboundAndOutboundVolumeView",200),
    ACTIVATION_VIEW("刷新activationView",null,"activationView",200),
    LOG_STATISTICS_VIEW("刷新logStatisticsView",null,"logStatisticsView",200),



    CONTINUE_WAREHOUSING_TASK("是否继续入库任务", InOrOutConstant.in,"continueWarehousingTask",200),
    CONTINUE_ISSUE_TASK("是否继续出库任务",InOrOutConstant.out,"continueIssueTask",200),

    PASSWORD_VERIFICATION_SUCCESSFUL("确认修改",true,"passwordVerificationSuccessful",200),
    PASSWORD_VERIFICATION_FAILED("密码错误，校验密码失败！",false,"passwordVerificationFailed",200),
    PASSWORD_SAVE_SUCCESSFUL("保存成功",true,"passwordSaveSuccessful",200),
    PASSWORD_SAVE_FAILED("修改失败",false,"passwordSaveFailed",200),
    ;

    private final String message;
    private final Object data;
    private final String type;
    private final Integer code;

    WebSocketEnum(String message, Object data, String type,Integer code) {
        this.message = message;
        this.data = data;
        this.type = type;
        this.code = code;
    }

    WebSocketEnum(String type, Object data, String message) {
        this.type = type;
        this.data = data;
        this.message = message;
        this.code = 200;
    }
}
