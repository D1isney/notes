package com.wms.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@TableName("role_permissions")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="RolePermissions", description="")
public class RolePermissions implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "`id`", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "角色")
    @TableField("`role_id`")
    private Long roleId;

    @ApiModelProperty(value = "权限")
    @TableField("`permissions_id`")
    private Long permissionsId;

    @ApiModelProperty(value = "创建时间")
    @TableField("`create_time`")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("`update_time`")
    private Date updateTime;

    @ApiModelProperty(value = "创建人")
    @TableField("`create_member`")
    private Long createMember;

    @ApiModelProperty(value = "更新人")
    @TableField("`update_member`")
    private Long updateMember;

    @ApiModelProperty(value = "描述")
    @TableField("`remark`")
    private String remark;
}
