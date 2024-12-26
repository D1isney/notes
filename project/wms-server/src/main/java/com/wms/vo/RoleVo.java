package com.wms.vo;


import com.wms.pojo.Role;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@ApiModel(value = "RoleVo", description = "包装类")
public class RoleVo extends Role {
    private String createUsername;
    private String updateUsername;
}
