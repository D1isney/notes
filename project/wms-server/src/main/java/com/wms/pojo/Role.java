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

/**
 * 角色表
 * @author Disney
 * @since 2024年12月10日23:18:32
 */
@TableName("role")
@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="Role", description="")
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色id")
    @TableId(value = "`id`", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "角色名称")
    @TableField("`name`")
    private String name;

    @ApiModelProperty(value = "角色编码")
    @TableField("`code`")
    private String code;

    @ApiModelProperty(value = "该角色状态（0：不可用角色，1：可用角色）")
    @TableField("`status`")
    private Integer status;

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
