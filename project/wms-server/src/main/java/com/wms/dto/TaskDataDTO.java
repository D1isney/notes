package com.wms.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

//  任务需要的DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskDataDTO {
    @ApiModelProperty(value = "物料Code")
    private String goodsCode;

    @ApiModelProperty(value = "库存Code")
    private String inventoryCode;

    @ApiModelProperty(value = "库位Code")
    private String storageCode;
}
