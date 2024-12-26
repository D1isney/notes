package com.wms.vo;

import com.wms.pojo.ParamKey;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ParamKeyVo extends ParamKey {

    private String createUsername;
    private String updateUsername;
}
