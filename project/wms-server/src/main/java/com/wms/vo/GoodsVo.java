package com.wms.vo;

import com.wms.pojo.Goods;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GoodsVo extends Goods {
    private String createUsername;
    private String updateUsername;
}
