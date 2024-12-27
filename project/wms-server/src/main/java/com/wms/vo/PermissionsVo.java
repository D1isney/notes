package com.wms.vo;



import com.wms.pojo.Permissions;
import io.swagger.annotations.ApiModel;
import lombok.*;


@ToString
@Setter
@Getter
@ApiModel(value="PermissionsVo", description="包装类")
public class PermissionsVo extends Permissions {
    private String createUsername;
    private String updateUsername;

}
