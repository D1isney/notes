package com.wms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryNum {
    //  总数量
    private Integer InventoryNum;
    // 余额
    private Integer InventoryResidue;

    //  已经使用了的
    private Integer InventoryUsed;

    //  库位名称
    private String storageName;

}
