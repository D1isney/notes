package com.wms.vo;

import com.wms.pojo.Inventory;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InventoryVo extends Inventory {
    private String createUsername;
    private String updateUsername;
}
