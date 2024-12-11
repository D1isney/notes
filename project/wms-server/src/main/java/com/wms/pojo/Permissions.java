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

@TableName("permissions")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="Permissions", description="")
public class Permissions implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "权限id")
    @TableId(value = "`id`", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "权限名称")
    @TableField("`name`")
    private String name;

    @ApiModelProperty(value = "权限编码（唯一的）")
    @TableField("`code`")
    private String code;

    @ApiModelProperty(value = "权限（wms:list，格式：角色code:权限code）")
    @TableField("`path`")
    private String path;

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
