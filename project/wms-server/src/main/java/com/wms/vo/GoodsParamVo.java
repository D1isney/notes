package com.wms.vo;

import com.wms.pojo.GoodsParam;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GoodsParamVo extends GoodsParam {
    private String createUsername;
    private String updateUsername;
}
