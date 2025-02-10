package com.wms.vo;

import com.wms.pojo.Goods;
import com.wms.pojo.Inventory;
import com.wms.pojo.Task;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TaskVo extends Task {
    private Inventory inventory;
    private Goods goods;

    private String createUsername;
    private String updateUsername;
}
