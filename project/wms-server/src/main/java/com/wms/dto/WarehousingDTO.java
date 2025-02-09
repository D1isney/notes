package com.wms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WarehousingDTO {
    private String goodsCode;
    private Long goodsId;
    private String inventoryCode;
    private String storageCode;
    private Integer type;
}
