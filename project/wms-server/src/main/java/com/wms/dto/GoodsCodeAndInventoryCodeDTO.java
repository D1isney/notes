package com.wms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsCodeAndInventoryCodeDTO {
    private String goodsCode;
    private String inventoryCode;
}
