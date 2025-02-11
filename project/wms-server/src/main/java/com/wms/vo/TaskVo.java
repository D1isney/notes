package com.wms.vo;

import com.wms.pojo.Inventory;
import com.wms.pojo.Task;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TaskVo extends Task {
    private String createUsername;
    private String updateUsername;


    private String inventoryName;
    private String inventoryLayer;
    private String inventoryStatus;


    private String goodsName;
    private Integer goodsType;
}
